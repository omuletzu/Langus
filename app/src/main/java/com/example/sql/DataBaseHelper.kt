package com.example.sql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.Calendar

class DataBaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "baza"
        private val TABLE_ID = "tabel"
        private val ID = "id"
        private val NAME = "nume"
        private val PASSWORD = "parola"
        private val DATE_DAY = "ziua"
        private val DATE_MONTH = "luna"
        private val DATE_YEAR = "anul"
        private val CONTOR = "contor"
        private val WORD = "word"
        private val WORD_AD = "descriere"
        private val LANGUAGE1 = "limba1"
        private val LANGUAGE2 = "limba2"
        private val HOURS = "ore"
        private val MINUTES = "minute"
        private val WORD_TYPE = "tipcuv"
        private val ON_OFF = "on_off"
        private val NOTIF = "notif"
        private val CUSTOM_WORD = "custom_word"
        private val LIMIT = "limita"
        private val CONNECTED = "conectat"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_ID ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $NAME VARCHAR(20), $PASSWORD VARCHAR(20), $DATE_DAY INTEGER , $DATE_MONTH INTEGER , $DATE_YEAR INTEGER , $CONTOR INTEGER , $WORD VARCHAR(20) , $WORD_AD VARCHAR(25), " +
                "$LANGUAGE1 VARCHAR(20) , $LANGUAGE2 VARCHAR(20) , $HOURS INTEGER , $WORD_TYPE INTEGER , $ON_OFF INTEGER , $NOTIF INTEGER, $CUSTOM_WORD VARCHAR(20), $MINUTES VARCHAR(10), $LIMIT INTEGER, $CONNECTED INTEGER)"

        db.execSQL(createTable)

        //db.execSQL("INSERT INTO tabel (nume, parola, ziua, luna, anul, contor, word, descriere, limba1, limba2, ore, tipcuv , on_off, notif, custom_word, minute, limita) " +
        //        "VALUES ('root', 'admin', 1, 1, 1970, 0, '-', '-', 'English', 'French', 1 , 0 , 0 , 0, '-', '00', 0)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, OLD_VERSION: Int, NEW_VERSION : Int) {

    }



}