package com.example.totalsample

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.Handler.Callback
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import com.example.totalsample.databinding.ActivityIncrementBinding
import com.example.totalsample.service.RemoteService

class IncrementActivity : BaseActivity<ActivityIncrementBinding>() {
    val bump_msg = 1
    var mIsBound = false
    val TAG = IncrementActivity::class.simpleName
    private var mService: IRemoteService? = null

    val mHandler = Handler(Looper.getMainLooper(), object : Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                bump_msg ->
                    vBinding.tvContent.text = "Received from Service:${msg.arg1}"

                else -> {}
            }
            return true
        }
    })

    val mCallback = object : IRemoteServiceCallback.Stub() {
        override fun valueChanged(value: Int) {
            mHandler.sendMessage(mHandler.obtainMessage(bump_msg, value, 0))
        }
    }

    val serviceCollection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "---onServiceConnected")
            vBinding.tvContent.text = "Attached!!!"
            mService = IRemoteService.Stub.asInterface(service)
            mService?.registerCallback(mCallback)


            Toast.makeText(this@IncrementActivity, "remote service connected", Toast.LENGTH_SHORT)
                .show()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "---onServiceDisconnected")

            mService = null
            vBinding.tvContent.text = "disconnected!!!"
            Toast.makeText(this@IncrementActivity, "remote service Disconnected", Toast.LENGTH_SHORT)
                .show()
        }
    }


    @SuppressLint("ImplicitSamInstance")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vBinding.btnIncrement.setOnClickListener {
            val intent = Intent(this, RemoteService::class.java).also {
                it.setAction(IRemoteService::class.java.name)
                mIsBound = true
            }
            bindService(intent, serviceCollection, BIND_AUTO_CREATE)
        }

        vBinding.btnStop.setOnClickListener {
            if (mIsBound) {
                mService?.unregisterCallback(mCallback)
                unbindService(serviceCollection)
                mIsBound = false;
                vBinding.tvContent.text = "Unbinded……"
            }
        }
    }
}