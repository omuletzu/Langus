package com.example.sql

import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream

class lang_lists() {

    private lateinit var buf : InputStream

    public fun ret_lang(lang : String, tipcuv : Int, Rand_nr : Int, context : Context) : String{

        var rand_nr = Rand_nr

        if(tipcuv == 1)
            rand_nr %= 290

        if(tipcuv == 0){
            when(lang){
                "English" ->    buf = context.resources.openRawResource(R.raw.common_english)
                "French" ->    buf = context.resources.openRawResource(R.raw.common_french)
                "German" ->    buf = context.resources.openRawResource(R.raw.common_german)
                "Spanish" ->    buf = context.resources.openRawResource(R.raw.common_spanish)
                "Romanian" ->    buf = context.resources.openRawResource(R.raw.common_romanian)
            }
        }
        else{
            when(lang){
                "English" ->    buf = context.resources.openRawResource(R.raw.advanced_english)
                "French" ->    buf = context.resources.openRawResource(R.raw.advanced_french)
                "German" ->    buf = context.resources.openRawResource(R.raw.advanced_german)
                "Spanish" ->    buf = context.resources.openRawResource(R.raw.advanced_spanish)
                "Romanian" ->    buf = context.resources.openRawResource(R.raw.advanced_romanian)
            }
        }

        var str : String = "null"
        var buf_aux = buf.bufferedReader()

        while(rand_nr > 0){
            str = buf_aux.readLine().toString()
            rand_nr--
        }

        buf_aux.close()

        return str
    }
}