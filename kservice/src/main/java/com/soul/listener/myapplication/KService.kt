package com.soul.listener.myapplication

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

/**
 * @description
 * @author kuan
 * Created on 2018/1/24.
 */
class KService : Service() {

    private var myBInder : MyBInder? = null

    override fun onCreate() {
        super.onCreate()
        myBInder = MyBInder()
        Log.e("KService--->onCreate","服务开启")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e("KService--->onCreate","服务关闭")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder {
        return myBInder!!
    }

    class MyBInder : Binder() {
        fun getBInder():MyBInder{
            return MyBInder()
        }

        fun getNumber(): Int {
            return 1
        }
    }


}