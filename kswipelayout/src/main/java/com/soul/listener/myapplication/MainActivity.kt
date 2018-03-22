package com.soul.listener.myapplication

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val shareperference = this.getSharedPreferences("name", Context.MODE_PRIVATE)

    }
}