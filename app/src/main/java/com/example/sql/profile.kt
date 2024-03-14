package com.example.sql

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)


        //text_date.setText("You are a user since -> ${cursor.getInt(cursor.getColumnIndexOrThrow("ziua"))}/${cursor.getInt(cursor.getColumnIndexOrThrow("luna"))}/${cursor.getInt(cursor.getColumnIndexOrThrow("anul"))}")
    }
}