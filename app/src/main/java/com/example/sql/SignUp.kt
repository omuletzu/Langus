package com.example.sql

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val user : TextInputEditText = findViewById(R.id.input)
        val parola : TextInputEditText = findViewById(R.id.parola)
        val conf_parola : TextInputEditText = findViewById(R.id.conf_parola)
        val btn : Button = findViewById(R.id.buton)
        val login : Button = findViewById(R.id.login)
        val mod : TextView = findViewById(R.id.mod)

        user.setText("")
        parola.setText("")
        conf_parola.setText("")

        login.setOnClickListener(){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val db = DataBaseHelper(this)
        val db_write = db.writableDatabase

        btn.setOnClickListener(){
            val user_text = user.text.toString()
            val parola_text = parola.text.toString()
            val conf_parola_text = conf_parola.text.toString()

            if(parola_text == conf_parola_text && parola_text != ""){
                val cursor = db_write.rawQuery("SELECT * FROM tabel", null)
                cursor.moveToFirst()

                var ok = 0

                while(cursor.moveToNext()){
                    if(user_text == cursor.getString(cursor.getColumnIndexOrThrow("nume"))){
                        ok = 1
                        break
                    }
                }

                if(ok == 0){
                    val calendar : Calendar = Calendar.getInstance()

                    db_write.execSQL("INSERT INTO tabel (nume, parola, ziua, luna, anul, contor, word, descriere, limba1, limba2, ore, tipcuv, on_off, notif, custom_word, minute, limita, conectat) " +
                            "VALUES ('$user_text', '$parola_text', ${calendar.get(Calendar.DAY_OF_MONTH).toInt()}, ${calendar.get(Calendar.MONTH).toInt()}, ${calendar.get(Calendar.YEAR).toInt()}, 0, '-', '-', " +
                            "'English' , 'French' , 1 , 0 , 0, 0, '-', '00', 0, 0) ")

                    val createWordID = "CREATE TABLE ${user_text} (ID INTEGER PRIMARY KEY AUTOINCREMENT, cuvant VARCHAR(20) )"          //tabel custom pentru cuvinte salvate
                    db_write.execSQL(createWordID)

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                else{
                    mod.setText("*User already existing")
                }
            }
            else{
                if(parola_text != conf_parola_text)
                    mod.setText("*Passwords don't match")
                else
                    mod.setText("*Pass valide inputs")
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}