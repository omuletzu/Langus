package com.example.sql

import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = DataBaseHelper(this)
        val db_write = db.writableDatabase

        val sign : Button = findViewById(R.id.login)
        val input : TextInputEditText = findViewById(R.id.input)
        val parola : TextInputEditText = findViewById(R.id.parola)
        val btn : Button = findViewById(R.id.buton)
        val mod : TextView = findViewById(R.id.mod)

        input.setText("")
        parola.setText("")

        val cursor = db_write.rawQuery("SELECT * FROM tabel WHERE conectat = 1", null)
        cursor.moveToFirst()

        if(cursor.count != 0){
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("key", cursor.getString(cursor.getColumnIndexOrThrow("nume")))
            startActivity(intent)
            finish()
        }

        sign.setOnClickListener(){
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }

        btn.setOnClickListener() {
            val input_text = input.text.toString()
            val parola_text = parola.text.toString()

            val cursor : Cursor? = db_write.rawQuery("SELECT * FROM tabel WHERE nume = '$input_text' AND parola = '$parola_text' ", null)

            if(cursor?.isAfterLast == true)
                mod.text = "*Wrong password or\n user not found"
            else{
                val intent = Intent(this, MenuActivity::class.java)
                intent.putExtra("key", input.text.toString())
                db_write.execSQL("UPDATE tabel SET conectat = 1 WHERE nume = '$input_text' ")
                startActivity(intent)
                finish()
            }

            if(input_text == "")
                mod.setText("*Incorrect name")
        }
    }
}