package com.soul.listener.myapplication

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.Slide
import android.view.Gravity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("NewApi", "RtlHardcoded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val slide = Slide()
        slide.slideEdge = Gravity.LEFT
        window.enterTransition = slide.setDuration(500)
        window.exitTransition = slide.setDuration(500)

        setContentView(R.layout.activity_main)

        button1.setOnClickListener{
            startActivity(Intent(this,MainActivity2::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this,it,"mybtn").toBundle())
        }

    }
}
