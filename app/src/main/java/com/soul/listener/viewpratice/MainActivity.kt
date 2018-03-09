package com.soul.listener.viewpratice

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //判断是否在心跳
    var isJump = false
    //判断是哪张图片
    var jumpState = false
    var handler  = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler = Handler {
            when (it.arg1) {
                1 -> {
                    button.setBackgroundResource(R.drawable.love_small)
                    jumpState = true
                    val newMessage = handler.obtainMessage()
                    newMessage.arg1 = 2
                    handler.sendMessageDelayed(newMessage,300)

                    true
                }
                2 -> {
                    button.setBackgroundResource(R.drawable.love_big)
                    jumpState = false
                    val newMessage = handler.obtainMessage()
                    newMessage.arg1 = 1
                    handler.sendMessageDelayed(newMessage,300)
                    true
                }
                else -> {
                    false
                }
            }
        }

        button.setOnLongClickListener {
            val message = Message()
            message.arg1 = if (jumpState) 1 else 2
            isJump = true
            handler.sendMessage(message)
            true
        }

    }

}
