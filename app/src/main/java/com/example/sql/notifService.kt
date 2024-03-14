package com.example.sql

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.Calendar

class notifService : Service() {

    private lateinit var alarm_man : AlarmManager
    private lateinit var pend_int : PendingIntent

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val timp = System.currentTimeMillis() + ( intent.getIntExtra("ore", 1) * 3600000 )

        val notif_int = Intent(this, notification::class.java)
        notif_int.putExtra("ore", timp)
        pend_int = PendingIntent.getBroadcast(
            this,
            0,
            notif_int,
            PendingIntent.FLAG_IMMUTABLE
        )

        alarm_man = this.getSystemService(ALARM_SERVICE) as AlarmManager
        alarm_man.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timp, pend_int)

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}