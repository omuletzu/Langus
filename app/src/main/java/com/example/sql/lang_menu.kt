package com.example.sql

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.widget.Button
import android.widget.TextView

class lang_menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lang_menu)

        val db_write = DataBaseHelper(this).writableDatabase

        val btn1 : Button = findViewById(R.id.lang_btn_s1)
        val btn2 : Button = findViewById(R.id.lang_btn_s2)
        val btn3 : Button = findViewById(R.id.lang_btn_s3)
        val btn4 : Button = findViewById(R.id.lang_btn_s4)
        val btn5 : Button = findViewById(R.id.lang_btn_s5)
        val ok : Button = findViewById(R.id.lang_sel_ok)

        var btn = "English"

        btn1.setOnClickListener(){
            btn1.background = resources.getDrawable(R.drawable.back_lang_english)
            btn2.background = resources.getDrawable(R.drawable.back_lang_random)
            btn3.background = resources.getDrawable(R.drawable.back_lang_random)
            btn4.background = resources.getDrawable(R.drawable.back_lang_random)
            btn5.background = resources.getDrawable(R.drawable.back_lang_random)
            btn = "English"
        }

        btn2.setOnClickListener(){
            btn1.background = resources.getDrawable(R.drawable.back_lang_random)
            btn2.background = resources.getDrawable(R.drawable.back_lang_french)
            btn3.background = resources.getDrawable(R.drawable.back_lang_random)
            btn4.background = resources.getDrawable(R.drawable.back_lang_random)
            btn5.background = resources.getDrawable(R.drawable.back_lang_random)
            btn = "French"
        }

        btn3.setOnClickListener(){
            btn1.background = resources.getDrawable(R.drawable.back_lang_random)
            btn2.background = resources.getDrawable(R.drawable.back_lang_random)
            btn3.background = resources.getDrawable(R.drawable.back_lang_german)
            btn4.background = resources.getDrawable(R.drawable.back_lang_random)
            btn5.background = resources.getDrawable(R.drawable.back_lang_random)
            btn = "German"
        }

        btn4.setOnClickListener(){
            btn1.background = resources.getDrawable(R.drawable.back_lang_random)
            btn2.background = resources.getDrawable(R.drawable.back_lang_random)
            btn3.background = resources.getDrawable(R.drawable.back_lang_random)
            btn4.background = resources.getDrawable(R.drawable.back_lang_spanish)
            btn5.background = resources.getDrawable(R.drawable.back_lang_random)
            btn = "Spanish"
        }

        btn5.setOnClickListener(){
            btn1.background = resources.getDrawable(R.drawable.back_lang_random)
            btn2.background = resources.getDrawable(R.drawable.back_lang_random)
            btn3.background = resources.getDrawable(R.drawable.back_lang_random)
            btn4.background = resources.getDrawable(R.drawable.back_lang_random)
            btn5.background = resources.getDrawable(R.drawable.back_lang_romanian)
            btn = "Romanian"
        }

        ok.setOnClickListener(){

            val intent_aux = Intent()
            intent_aux.putExtra("key", btn)
            setResult(Activity.RESULT_OK, intent_aux)

            val mod = intent.getIntExtra("mod", 0)
            if(mod == 0)
                db_write.execSQL("UPDATE tabel SET limba1 = '$btn' WHERE nume = '${intent.getStringExtra("mod_nume")}'")
            else
                db_write.execSQL("UPDATE tabel SET limba2 = '$btn' WHERE nume = '${intent.getStringExtra("mod_nume")}'")

            finish()
        }
    }

    private fun btn_lay_changeBack(mod : String) : Drawable {
        when(mod){
            "English" -> return resources.getDrawable(R.drawable.back_lang_english)
            "French" -> return resources.getDrawable(R.drawable.back_lang_french)
            "German" -> return resources.getDrawable(R.drawable.back_lang_german)
            "Spanish" -> return resources.getDrawable(R.drawable.back_lang_spanish)
            "Romanian" -> return resources.getDrawable(R.drawable.back_lang_romanian)
        }

        return resources.getDrawable(R.drawable.back_lang_english)
    }
}