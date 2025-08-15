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
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.example.totalsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val bump_msg = 1
    var mIsBound = false
    private lateinit var binding: ActivityMainBinding
    private var mService: IRemoteService? = null

    val mHandler = Handler(Looper.getMainLooper(), object : Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                bump_msg ->
                    binding.tvContent.text = "Received from Service:${msg.arg1}"

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
            binding.tvContent.text = "Attached!!!"
            mService = IRemoteService.Stub.asInterface(service)
            mService?.registerCallback(mCallback)


            Toast.makeText(this@MainActivity, "remote service connected", Toast.LENGTH_SHORT)
                .show()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mService = null
            binding.tvContent.text = "disconnected!!!"
            Toast.makeText(this@MainActivity, "remote service Disconnected", Toast.LENGTH_SHORT)
                .show()
        }
    }


    @SuppressLint("ImplicitSamInstance")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIncrement.setOnClickListener {
            val intent = Intent(this, RemoteService::class.java).also {
                it.setAction(IRemoteService::class.java.name)
                mIsBound = true
            }
            bindService(intent, serviceCollection, BIND_AUTO_CREATE)
        }

        binding.btnStop.setOnClickListener {
            if (mIsBound) {
                mService?.unregisterCallback(mCallback)
                unbindService(serviceCollection)
                mIsBound = false;
                binding.tvContent.text = "Unbinded……"
            }
        }
    }
}