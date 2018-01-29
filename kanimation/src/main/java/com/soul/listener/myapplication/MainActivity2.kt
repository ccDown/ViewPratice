package com.soul.listener.myapplication

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main2.*

/**
 * @description
 * @author kuan
 * Created on 2018/1/19.
 */
class MainActivity2 : AppCompatActivity() {
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        button2.setOnClickListener {

//            startActivity(Intent(this,MainActivity::class.java),
//                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            startActivity(Intent(this,MainActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this,it,"mybtn").toBundle())
        }
    }
}