package com.example.totalsample.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.RemoteCallbackList
import android.util.Log
import android.widget.Toast
import com.example.totalsample.IRemoteService
import com.example.totalsample.IRemoteServiceCallback

class RemoteService : Service() {
    val TAG = RemoteService::class.simpleName
    val msg_what = 66
    var mValue = 0
    val mCallbackList = RemoteCallbackList<IRemoteServiceCallback>();

    val mHandler = Handler(Looper.getMainLooper(), object : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                msg_what -> {
                    val tValue = mValue++
                    Log.d(TAG, "increment value:$tValue")
                    val broadcastSize = mCallbackList.beginBroadcast()
                    for (i in 0..broadcastSize) {
                        runCatching {
                            mCallbackList.getBroadcastItem(i)?.valueChanged(tValue)
                        }
                    }
                    mCallbackList.finishBroadcast()
                    msg.target.apply {
                        sendMessageDelayed(obtainMessage(msg_what), 1 * 1000)
                    }
                }

                else -> {}
            }
            return true
        }
    })

    private val mBinder = object : IRemoteService.Stub() {
        override fun registerCallback(cb: IRemoteServiceCallback?) {
            if (cb != null) mCallbackList.register(cb)
        }

        override fun unregisterCallback(cb: IRemoteServiceCallback?) {
            if (cb != null) mCallbackList.unregister(cb)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "---RemoteService onCreate")
        mHandler.sendEmptyMessage(msg_what)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "---RemoteService onStartCommand")
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        intent?.apply {
            if (action?.isEmpty() == false && IRemoteService::class.java.name == action) {
                return mBinder
            }
        }
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "RemoteService Destroy!!!", Toast.LENGTH_SHORT).show();
        mCallbackList.kill()
        mHandler.removeCallbacksAndMessages(null)
    }

}