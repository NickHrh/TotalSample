package com.example.totalsample.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger

class MessengerService : Service() {
    val messengerHandler = Handler(Looper.getMainLooper(), object : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                1 -> {
                    val bundler = msg.data
                    var value = bundler?.getInt("C2S", 0) ?: 0
                    val clientMsger = msg.replyTo
                    val sendMsg2Client = Message.obtain()
                    sendMsg2Client.what = 2
                    sendMsg2Client.data = Bundle().also {
                        it.putInt("S2C", ++value)
                    }
                    clientMsger.send(sendMsg2Client)
                }

                else -> {}
            }
            return true
        }
    })

    val messenger = Messenger(messengerHandler)

    override fun onBind(intent: Intent): IBinder {
        return messenger.binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        messengerHandler.removeCallbacksAndMessages(null)
        return super.onUnbind(intent)
    }
}