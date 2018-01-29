package com.soul.listener.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val kService = KService()
        val intent = Intent(this@MainActivity, kService::class.java)

        connect.setOnClickListener {
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }

        disconnect.setOnClickListener {
            unbindService(serviceConnection)
        }
    }

    object serviceConnection : ServiceConnection {
        /**
         * onServiceDisconnected()方法在连接正常关闭的情况下是不会被调用的, 该方法只在Service被破坏了或者被杀死的时候调用.
         */
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e("onServiceDisconnected", "onServiceDisconnected  ${name.toString()}")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            val myBInder = service as KService.MyBInder

            Log.e("onServiceConnected", "onServiceConnected  获取到的数字是    ${myBInder.getNumber()}  ${name.toString()}")
        }

    }

}
