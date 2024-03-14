package com.example.sql

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.text.HtmlCompat
import java.io.File
import java.sql.DatabaseMetaData
import kotlin.random.Random

val CHANNEL_ID = "0"
var NOTIFICATION_ID = 0

class notification () : BroadcastReceiver() {

    lateinit var alarm_man : AlarmManager
    lateinit var pend_int : PendingIntent

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {

        val db = DataBaseHelper(context).writableDatabase
        val cursor = db.rawQuery("SELECT * FROM tabel WHERE nume = '${intent.getStringExtra("nume")}' ", null)
        cursor.moveToFirst()

        val lang1 = cursor.getString(cursor.getColumnIndexOrThrow("limba1"))
        val lang2 = cursor.getString(cursor.getColumnIndexOrThrow("limba2"))
        var str_spanned : Spanned? = null

        if(cursor.getInt(cursor.getColumnIndexOrThrow("limita")) == 0) {

            val rand_nr = Random.nextInt(
                0,
                680
            )                                                       //updating words and langs
            val word1 = lang_lists().ret_lang(
                cursor.getString(cursor.getColumnIndexOrThrow("limba1")),
                cursor.getInt(cursor.getColumnIndexOrThrow("tipcuv")),
                rand_nr,
                context
            )
            val word2 = lang_lists().ret_lang(
                cursor.getString(cursor.getColumnIndexOrThrow("limba2")),
                cursor.getInt(cursor.getColumnIndexOrThrow("tipcuv")),
                rand_nr,
                context
            )

            var mod = "COMMON"
            if(cursor.getInt(cursor.getColumnIndexOrThrow("tipcuv")) == 1)
                mod = "ADVANCED"

            var str = "<b>${word1}</b>" + " (${lang1})" + " | <b>${word2}</b>" + " (${lang2})" + " ($mod WORD)"

            str_spanned = HtmlCompat.fromHtml(
                str,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )                    //str_spanned for customized string
        }
        else{
            val dir = File(context.filesDir, "dir").bufferedReader()
            var limita = cursor.getInt(cursor.getColumnIndexOrThrow("limita"))
            var word : String = "p"

            db.execSQL("UPDATE tabel SET limita = ${limita - 1} WHERE nume = '${intent.getStringExtra("nume")}' ")

            while(limita > 0) {
                word = dir.readLine().toString()
                limita--

                Log.i("tagy", word)
            }

            str_spanned = HtmlCompat.fromHtml("<b>${cursor.getString(cursor.getColumnIndexOrThrow("custom_word"))}</b> (English) | <b>${word}</b> (RELATED WORD)",
                HtmlCompat.FROM_HTML_MODE_LEGACY)

            dir.close()
        }

        val notif = NotificationCompat.Builder(context, CHANNEL_ID)             //se creeaza notificarea
            .setContentTitle("LANGOS WORD")
            .setContentText(str_spanned)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(androidx.core.R.drawable.notification_icon_background)
            .setColor(Color.MAGENTA)
            .setColorized(true)

        val notif_man = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager           //se creeaza managerul

        create_channel(notif_man)

        notif_man.notify(NOTIFICATION_ID, notif.build())

        pend_int = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val conectat = cursor.getInt(cursor.getColumnIndexOrThrow("conectat"))

        if(conectat == 1) {
            alarm_man = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarm_man.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() +
                        (cursor.getInt(cursor.getColumnIndexOrThrow("ore")) * 3600000) + (cursor.getInt(
                    cursor.getColumnIndexOrThrow("minute")
                ) * 60000),
                pend_int
            )

            NOTIFICATION_ID++
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun create_channel(notif_man : NotificationManager){


        val notif_channel = NotificationChannel(CHANNEL_ID, "canal1", NotificationManager.IMPORTANCE_HIGH)          //se creeaza canalul
        notif_man.createNotificationChannel(notif_channel)
    }
}