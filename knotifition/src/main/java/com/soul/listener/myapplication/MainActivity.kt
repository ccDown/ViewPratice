package com.soul.listener.myapplication

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationCompat.VISIBILITY_PRIVATE
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1.setOnClickListener {
            val notificationManager = getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager
            val pendingIntent = PendingIntent.getActivity(this,0,Intent(this@MainActivity,MainActivity::class.java),0)
            val builder  = NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("通知事件")
                    .setContentTitle("标题")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setFullScreenIntent(pendingIntent,false)
                    .setVisibility(VISIBILITY_PRIVATE)
            notificationManager.notify(1,builder.build())
//            val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//            intent = Intent(this@MainActivity, MainActivity::class.java)
//            val pintent = PendingIntent.getActivity(this, 0, intent, 0)
//            val id = "1"
//            val description = "123"
//            val importance = NotificationManager.IMPORTANCE_LOW
//            val mChannel = NotificationChannel(id, description, importance)
//            //  mChannel.setDescription(description);
//            //  mChannel.enableLights(true);
//            //  mChannel.setLightColor(Color.RED);
//            //  mChannel.enableVibration(true);
//            //  mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//            mNotificationManager.createNotificationChannel(mChannel)
//            val notification = Notification.Builder(this@MainActivity, id).setContentTitle("Title")
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setLargeIcon(BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher_round))
//                    .setContentTitle("您有一条新通知")
//                    .setContentText("这是一条逗你玩的消息")
//                    .setAutoCancel(true)
//                    .setContentIntent(pintent)
//                    .build()
//            mNotificationManager.notify(1, notification)
        }
    }
}
