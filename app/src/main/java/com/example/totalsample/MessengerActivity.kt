package com.example.totalsample

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.widget.Toast
import com.example.totalsample.databinding.ActivityMessagerBinding
import com.example.totalsample.service.MessengerService
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class MessengerActivity : BaseActivity<ActivityMessagerBinding>() {
    var mValue = -1
    var mIsBound = false
    val TAG = MessengerActivity::class.simpleName

    val messengerHandler = Handler(Looper.getMainLooper(), object : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                2 -> {
                    val value = msg.data.getInt("S2C")
                    mValue = value
                    viewBinding.tvMsgContent.text = "Received from Service:$mValue"

                    runBlocking {
                        delay(1000)
                        val sendMsg2Server = toServerMsg()
                        messengerServer?.send(sendMsg2Server)
                    }
                }

                else -> {}
            }

            return true
        }
    })

    var messengerServer: Messenger? = null
    val clientMsger = Messenger(messengerHandler)

    val connection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            Log.i(TAG, "---onServiceConnected")
            messengerServer = Messenger(service)
            val sendMsg2Server = toServerMsg()
            messengerServer?.send(sendMsg2Server)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "---onServiceDisconnected")
            viewBinding.tvMsgContent.text = "disconnected!!!"
            Toast.makeText(
                this@MessengerActivity,
                "Messenger service Disconnected",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.btnMsgIncrement.setOnClickListener {
            val intent = Intent(this, MessengerService::class.java)
            bindService(intent, connection, BIND_AUTO_CREATE)
            mIsBound = true
        }
        viewBinding.btnMsgStop.setOnClickListener {
            if (mIsBound) {
                // 先解绑，阻止后续消息进入
                unbindService(connection)
                // 再清理本地消息队列，避免残留消息
                messengerHandler.removeCallbacksAndMessages(null)
                mIsBound = false;
                viewBinding.tvMsgContent.text = "Unbinded……"
                mValue = -1
            }
        }

    }

    private fun toServerMsg(): Message? {
        val sendMsg2Server = Message.obtain().also {
            it.replyTo = clientMsger
            it.what = 1
            it.data = Bundle().also {
                it.putInt("C2S", mValue)
            }
        }
        return sendMsg2Server
    }
}