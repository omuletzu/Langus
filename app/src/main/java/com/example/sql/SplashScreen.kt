package com.example.sql

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val counter = counter()
        counter.start()
    }

    fun trigger(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    inner class counter() : CountDownTimer(500 , 500){

        override fun onTick(p0: Long) {
        }

        override fun onFinish() {
            trigger()
        }
    }
}