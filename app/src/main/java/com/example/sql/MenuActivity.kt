package com.example.sql

import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.Data
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isInvisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import kotlin.random.Random
import com.example.sql.lang_lists
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import edu.mit.jwi.Dictionary
import edu.mit.jwi.item.POS
import edu.mit.jwi.item.Pointer
import edu.mit.jwi.item.Word
import org.w3c.dom.Text

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val JWI_DICT = WordNetHelper().getWrd(this)

        val toolbar : Toolbar = findViewById(R.id.toolbar)
        val draw_lay : DrawerLayout = findViewById(R.id.draw_id)
        val naview : NavigationView = findViewById(R.id.navigation_view)

        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("")

        val drawToggle = ActionBarDrawerToggle(this, draw_lay, toolbar, R.string.openString, R.string.closeString)
        draw_lay.addDrawerListener(drawToggle)
        drawToggle.syncState()

        //toolbar.setNavigationIcon(R.drawable.navigator)                                                       //centrare hamburger
        for(ind in 0 until 2) {
            var ham_lay = toolbar.getChildAt(ind).layoutParams as Toolbar.LayoutParams
            ham_lay.gravity = android.view.Gravity.CENTER
            toolbar.getChildAt(ind).layoutParams = ham_lay
        }

        val fragment = supportFragmentManager
        var transaction = fragment.beginTransaction()

        transaction.replace(R.id.frame, CustomFragment(R.layout.language_select, intent.getStringExtra("key"), fragment, JWI_DICT))
        transaction.commit()

        naview.setNavigationItemSelectedListener { menuItem ->
            transaction = fragment.beginTransaction()

            if(menuItem.itemId == R.id.lang_sel){
                transaction.replace(R.id.frame, CustomFragment(R.layout.language_select, intent.getStringExtra("key"), fragment, JWI_DICT))
                transaction.commit()
            }

            if(menuItem.itemId == R.id.community){
                transaction.replace(R.id.frame, CustomFragment(R.layout.community, intent.getStringExtra("key"), fragment, JWI_DICT))
                transaction.commit()
            }

            if(menuItem.itemId == R.id.profile){
                transaction.replace(R.id.frame, CustomFragment(R.layout.profile, intent.getStringExtra("key"), fragment, JWI_DICT))
                transaction.commit()
            }

            if(menuItem.itemId == R.id.info){
                transaction.replace(R.id.frame, definition_fragment(R.layout.activity_definitions, intent.getStringExtra("key"), fragment, JWI_DICT))
                transaction.commit()
            }

            if(menuItem.itemId == R.id.saved_words){
                transaction.replace(R.id.frame, saved_frament(R.layout.activity_saved_words, intent.getStringExtra("key")))
                transaction.commit()
            }

            if(menuItem.itemId == R.id.close_app){
                val db_write = DataBaseHelper(this).writableDatabase
                db_write.execSQL("UPDATE tabel SET conectat = 0")
                db_write.execSQL("UPDATE tabel SET on_off = 0 WHERE nume = '${intent.getStringExtra("key")}' ")

                finish()
            }

            draw_lay.closeDrawer(GravityCompat.START)
            true
        }
    }
}

class CustomFragment(val xml_layout : Int, val text : String?, val supportFragmentManager: FragmentManager, val JWI_DICT : Dictionary) : Fragment() {

    var lang_mod: String? = ""
    var lang_mod_aux: TextView? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val inf_lay = inflater.inflate(xml_layout, container, false)

        val db = DataBaseHelper(context)
        val db_write = db.writableDatabase

        var cursor = db_write.rawQuery("SELECT * FROM tabel WHERE nume = '$text' ", null)
        cursor.moveToFirst()

        if (xml_layout == R.layout.profile) {
            val profile_name: TextView = inf_lay.findViewById(R.id.profile_name)

            profile_name.setText(text)

            inf_lay.findViewById<TextView>(R.id.profile_date)?.setText(
                "${cursor?.getInt(cursor.getColumnIndexOrThrow("ziua"))}/${cursor?.getInt(cursor.getColumnIndexOrThrow("luna"))?.plus(1)}/${cursor?.getInt(cursor.getColumnIndexOrThrow("anul"))}"
            )

            val word: TextInputEditText = inf_lay.findViewById(R.id.favourite_word)
            val descript: TextInputEditText = inf_lay.findViewById(R.id.word_description)
            val score : TextView = inf_lay.findViewById(R.id.profile_score)
            val btn: Button = inf_lay.findViewById(R.id.btn_set)

            var cursor_aux = db_write.rawQuery("SELECT * FROM tabel ORDER BY contor DESC", null)
            cursor_aux.moveToLast()

            var x = 1

            while (cursor_aux.moveToPrevious()) {
                if (cursor_aux.getString(cursor_aux.getColumnIndexOrThrow("nume")) == text)
                    break

                x++
            }

            btn.setOnClickListener() {

                when (btn.background) {
                    resources.getDrawable(R.drawable.profile_back_set) -> btn.background =
                        resources.getDrawable(R.drawable.profile_back_set2)

                    resources.getDrawable(R.drawable.profile_back_set2) -> btn.background =
                        resources.getDrawable(R.drawable.profile_back_set)
                }

                Toast.makeText(context, "WORD SET", Toast.LENGTH_SHORT).show()

                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))

                db_write.execSQL("UPDATE tabel SET word = '${word.text.toString()}' , descriere = '${descript.text.toString()}' WHERE id = $id")
            }

            score.text = cursor.getInt(cursor.getColumnIndexOrThrow("contor")).toString()

            val cursor_aux_count = db_write.rawQuery("SELECT COUNT(*) FROM tabel", null)
            cursor_aux_count.moveToFirst()

            inf_lay.findViewById<TextView>(R.id.profile_place)
                .setText("${x}/${cursor_aux_count.getInt(0)}")
        }

        if (xml_layout == R.layout.community) {

            val btn_search: Button = inf_lay.findViewById(R.id.buton2)
            val btn_people: Button = inf_lay.findViewById(R.id.buton1)

            getCurs(db_write)

            btn_people.setOnClickListener() {                                                        //schimba fragmentul
                getCurs(db_write)
            }

            btn_search.setOnClickListener() {
                val search_fragment = supportFragmentManager.beginTransaction()
                search_fragment.replace(
                    R.id.community_frame,
                    search_fragment(R.layout.community_search, db_write)
                )
                search_fragment.commit()
            }
        }

        if (xml_layout == R.layout.language_select) {

            var cursor_aux = db_write.rawQuery("SELECT * FROM tabel WHERE nume = '$text'", null)
            cursor_aux.moveToFirst()

            val lang_type1: TextView =
                inf_lay.findViewById(R.id.lang_lang1)                   //initializing languages
            val lang_type2: TextView = inf_lay.findViewById(R.id.lang_lang3)
            val lang1: String = cursor.getString(cursor.getColumnIndexOrThrow("limba1"))
            val lang2: String = cursor.getString(cursor.getColumnIndexOrThrow("limba2"))

            lang_type1.text = lang1
            lang_type1.background = btn_lay_changeBack(lang1)
            lang_type2.text = lang2
            lang_type2.background = btn_lay_changeBack(lang2)

            val btn1: Button = inf_lay.findViewById(R.id.lang_btn)                                                         //first lang
            btn1.setOnClickListener() {
                val intent = Intent(inf_lay.context, lang_menu::class.java)
                intent.putExtra("mod", 0)
                intent.putExtra("mod_nume", text)
                startActivityForResult(intent, 123)                         //cod 123
                lang_mod_aux = lang_type1
            }

            val btn2: Button = inf_lay.findViewById(R.id.lang_btn1)                                                        //second lang
            btn2.setOnClickListener() {
                val intent = Intent(inf_lay.context, lang_menu::class.java)
                intent.putExtra("mod", 1)
                intent.putExtra("mod_nume", text)
                startActivityForResult(intent, 123)
                lang_mod_aux = lang_type2
            }

            val timer_minus: Button = inf_lay.findViewById(R.id.lang_timer_minus)                          //timer change
            val timer_plus: Button = inf_lay.findViewById(R.id.lang_timer_plus)
            val timer_h: TextView = inf_lay.findViewById(R.id.lang_timer_h)
            val timer_m: TextView = inf_lay.findViewById(R.id.lang_timer_m)

            timer_h.text = cursor_aux.getInt(cursor_aux.getColumnIndexOrThrow("ore")).toString()
            timer_m.text = cursor_aux.getInt(cursor_aux.getColumnIndexOrThrow("minute")).toString()
            var timp_h = timer_h.text.toString().toInt()
            var timp_m = timer_m.text.toString().toInt()

            if(timp_m == 0)
                timer_m.text = "00"

            timer_minus.setOnClickListener() {
                if(timp_h > 0) {
                    if(timp_m == 0) {
                        timp_m = 50
                        timp_h--
                    }
                    else
                        timp_m -= 10
                }
                else
                    if(timp_m > 0)
                        timp_m -= 10

                timer_h.text = timp_h.toString()
                if(timp_m != 0)
                    timer_m.text = timp_m.toString()
                else
                    timer_m.text = "00"

                db_write.execSQL("UPDATE tabel set ore = ${timp_h} WHERE nume = '$text'")
                db_write.execSQL("UPDATE tabel set minute = ${timp_m} WHERE nume = '$text'")
            }

            timer_plus.setOnClickListener() {
                if (timp_h < 7) {
                    if(timp_m == 50) {
                        timp_m = 0
                        timp_h++
                    }
                    else
                        timp_m += 10
                }
                else
                    if(timp_m < 50)
                        timp_m += 10

                timer_h.text = timp_h.toString()
                if(timp_m != 0)
                    timer_m.text = timp_m.toString()
                else
                    timer_m.text = "00"

                db_write.execSQL("UPDATE tabel set ore = ${timp_h} WHERE nume = '$text'")
                db_write.execSQL("UPDATE tabel set minute = ${timp_m} WHERE nume = '$text'")
            }

            val word_type =
                cursor_aux.getInt(cursor_aux.getColumnIndexOrThrow("tipcuv"))                       // word type

            val word_type_common: Button = inf_lay.findViewById(R.id.lang_word_type_common)
            val word_type_advanced: Button = inf_lay.findViewById(R.id.lang_word_type_random)

            if (word_type == 0) {
                word_type_common.background = resources.getDrawable(R.drawable.profile_back_set)
                word_type_advanced.background = resources.getDrawable(R.drawable.back_lang_random)
            } else {
                word_type_advanced.background = resources.getDrawable(R.drawable.profile_back_set)
                word_type_common.background = resources.getDrawable(R.drawable.back_lang_random)
            }

            word_type_common.setOnClickListener() {
                db_write.execSQL("UPDATE tabel SET tipcuv = 0, limita = 0 WHERE nume = '$text' ")

                word_type_common.background = resources.getDrawable(R.drawable.profile_back_set)
                word_type_advanced.background = resources.getDrawable(R.drawable.back_lang_random)
                Toast.makeText(inf_lay.context, "COMMON WORDS", Toast.LENGTH_SHORT).show()
            }

            word_type_advanced.setOnClickListener() {
                db_write.execSQL("UPDATE tabel SET tipcuv = 1, limita = 0 WHERE nume = '$text' ")
                word_type_advanced.background = resources.getDrawable(R.drawable.profile_back_set)
                word_type_common.background = resources.getDrawable(R.drawable.back_lang_random)
                Toast.makeText(inf_lay.context, "ADVANCED WORDS", Toast.LENGTH_SHORT).show()
            }

            val toggle_not: ToggleButton =
                inf_lay.findViewById(R.id.lang_on_off)                              //notif toggler

            if (cursor_aux.getInt(cursor_aux.getColumnIndexOrThrow("on_off")) == 1)
                toggle_not.isChecked = true
            else
                toggle_not.isChecked = false

            toggle_not.setOnClickListener() {

                db_write.execSQL("UPDATE tabel SET limita = 0 WHERE nume = '$text' ")

                val intent = Intent(inf_lay.context, notification::class.java)
                intent.putExtra("nume", text)

                if (toggle_not.isChecked == true) {

                    Toast.makeText(inf_lay.context, "NOTIFICATIONS ENABLED", Toast.LENGTH_SHORT).show()

                    db_write.execSQL("UPDATE tabel SET on_off = 1 WHERE nume = '$text' ")

                    val pend_int = PendingIntent.getBroadcast(
                        inf_lay.context,
                        0,
                        intent,
                        PendingIntent.FLAG_MUTABLE
                    )

                    cursor_aux = db_write.rawQuery("SELECT * FROM tabel WHERE nume = '$text' ", null)
                    cursor_aux.moveToFirst()

                    val alarm_man = inf_lay.context.getSystemService(Service.ALARM_SERVICE) as AlarmManager

                    alarm_man.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            System.currentTimeMillis() +
                                    (cursor_aux.getInt(cursor_aux.getColumnIndexOrThrow("ore")) * 3600000) + (cursor_aux.getInt(cursor_aux.getColumnIndexOrThrow("minute")) * 60000),
                        pend_int
                        )
                }
                else {
                    Toast.makeText(inf_lay.context, "NOTIFICATIONS DISABLED", Toast.LENGTH_SHORT).show()
                    db_write.execSQL("UPDATE tabel SET on_off = 0 WHERE nume = '$text' ")

                    val alarm_man = inf_lay.context.getSystemService(Service.ALARM_SERVICE) as AlarmManager
                    val pend_int = PendingIntent.getBroadcast(
                        inf_lay.context,
                        0,
                        intent,
                        PendingIntent.FLAG_MUTABLE
                    )

                    alarm_man.cancel(pend_int)

                    var custom_dir = File(inf_lay.context.filesDir, "dir")                                                      //sterge fisierul dir
                    custom_dir.writeText("")

                    val tipcuv = cursor_aux.getInt(cursor_aux.getColumnIndexOrThrow("tipcuv"))

                    if(tipcuv == 0) {
                        word_type_common.background = resources.getDrawable(R.drawable.profile_back_set)
                        word_type_advanced.background = resources.getDrawable(R.drawable.back_lang_random)
                    }
                    else{
                        word_type_advanced.background = resources.getDrawable(R.drawable.profile_back_set)
                        word_type_common.background = resources.getDrawable(R.drawable.back_lang_random)
                    }

                }
            }

            val lang_alt_btn: Button = inf_lay.findViewById(R.id.lang_alt_btn)
            val lang_input_alt: TextInputEditText = inf_lay.findViewById(R.id.lang_input_alt)

            val lang_alt_text_id : TextView = inf_lay.findViewById(R.id.lang_alt_text_id)
            lang_alt_text_id.text = cursor_aux.getString(cursor_aux.getColumnIndexOrThrow("custom_word"))

            lang_alt_btn.setOnClickListener() {

                val word_related = ArrayList<String>()
                val pointer_list_syn = ArrayList<Pointer>()

                pointer_list_syn.add(Pointer.SIMILAR_TO)
                pointer_list_syn.add(Pointer.HYPERNYM)
                pointer_list_syn.add(Pointer.HOLONYM_MEMBER)
                pointer_list_syn.add(Pointer.HYPONYM)
                pointer_list_syn.add(Pointer.HOLONYM_SUBSTANCE)
                pointer_list_syn.add(Pointer.HOLONYM_PART)
                pointer_list_syn.add(Pointer.ATTRIBUTE)
                pointer_list_syn.add(Pointer.DERIVATIONALLY_RELATED)
                pointer_list_syn.add(Pointer.MERONYM_MEMBER)
                pointer_list_syn.add(Pointer.MERONYM_PART)

                val word_related_aux = WordNetHelper().getText(JWI_DICT, lang_input_alt.text.toString(), pointer_list_syn)
                word_related_aux?.split(" | ")?.let { it1 -> word_related.addAll(it1) }

                if (word_related != null) {

                    var custom_dir = File(inf_lay.context.filesDir, "dir")                                                  ////sterge fisierul dir
                    custom_dir.writeText("")

                    for (ind in word_related)
                        custom_dir.appendText("${ind}\n")

                    Toast.makeText(inf_lay.context, "SET", Toast.LENGTH_SHORT).show()

                    word_type_advanced.background = resources.getDrawable(R.drawable.back_lang_random)
                    word_type_common.background = resources.getDrawable(R.drawable.back_lang_random)
                    db_write.execSQL("UPDATE tabel SET custom_word = '${lang_input_alt.text.toString()}' WHERE nume = '$text' ")
                    db_write.execSQL("UPDATE tabel SET limita = ${word_related.size} WHERE nume = '$text' ")
                    lang_alt_text_id.text = lang_input_alt.text.toString()
                }
                else {
                    Toast.makeText(inf_lay.context, "WORDS NOT FOUND", Toast.LENGTH_SHORT).show()
                    val tipcuv = cursor_aux.getInt(cursor_aux.getColumnIndexOrThrow("tipcuv"))

                    if(tipcuv == 0) {
                        word_type_common.background = resources.getDrawable(R.drawable.profile_back_set)
                        word_type_advanced.background = resources.getDrawable(R.drawable.back_lang_random)
                    }
                    else{
                        word_type_advanced.background = resources.getDrawable(R.drawable.profile_back_set)
                        word_type_common.background = resources.getDrawable(R.drawable.back_lang_random)
                    }
                }
            }
        }

        return inf_lay
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 123 && resultCode == Activity.RESULT_OK){
            lang_mod = data?.getStringExtra("key")
            lang_mod_aux?.text = lang_mod
            lang_mod_aux?.background = btn_lay_changeBack(lang_mod)
        }
    }

    private fun disable_notif(db_write : SQLiteDatabase, inf_lay : View, text : String?, intent : Intent, context: Context){

        db_write.execSQL("UPDATE tabel SET on_off = 0 WHERE nume = '$text' ")

        val alarm_man = inf_lay.context.getSystemService(Service.ALARM_SERVICE) as AlarmManager
        val pend_int = PendingIntent.getBroadcast(
            inf_lay.context,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE)

        alarm_man.cancel(pend_int)
    }

    private fun enable_notif(db_write : SQLiteDatabase, inf_lay : View, text : String?, context : Context, cursor_aux : Cursor, intent : Intent) {

            intent.putExtra("nume", text)

            Toast.makeText(inf_lay.context, "NOTIFICATIONS ENABLED", Toast.LENGTH_SHORT).show()

            db_write.execSQL("UPDATE tabel SET on_off = 1 WHERE nume = '$text' ")

            val pend_int = PendingIntent.getBroadcast(
                inf_lay.context,
                0,
                intent,
                PendingIntent.FLAG_MUTABLE
            )

            val alarm_man = inf_lay.context.getSystemService(Service.ALARM_SERVICE) as AlarmManager
            alarm_man.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() +
                        (cursor_aux.getInt(cursor_aux.getColumnIndexOrThrow("ore")) * 3600000) + (cursor_aux.getInt(cursor_aux.getColumnIndexOrThrow("minute")) * 60000),
                pend_int
            )
    }

    private fun btn_lay_changeBack(mod : String?) : Drawable{
        when(mod){
            "English" -> return resources.getDrawable(R.drawable.back_lang_english)
            "French" -> return resources.getDrawable(R.drawable.back_lang_french)
            "German" -> return resources.getDrawable(R.drawable.back_lang_german)
            "Spanish" -> return resources.getDrawable(R.drawable.back_lang_spanish)
            "Romanian" -> return resources.getDrawable(R.drawable.back_lang_romanian)
        }

        return resources.getDrawable(R.drawable.back_lang_english)
    }

    private fun btn_lay_changeText(mod : Int) : String{
        when(mod){
            1 -> return "English"
            2 -> return "French"
            3 -> return "German"
            4 -> return "Spanish"
            5 -> return "Romanian"
        }
        return "English"
    }

    private fun getCurs(db_write : SQLiteDatabase){
        val cursor = db_write.rawQuery("SELECT nume , word , descriere FROM tabel", null)
        cursor.moveToFirst()

        val arr_user = ArrayList<String>()
        val arr_word = ArrayList<String>()
        val arr_desc = ArrayList<String>()

        while(cursor.isAfterLast == false){
            arr_user.add(cursor.getString(cursor.getColumnIndexOrThrow("nume")))
            arr_word.add(cursor.getString(cursor.getColumnIndexOrThrow("word")))
            arr_desc.add(cursor.getString(cursor.getColumnIndexOrThrow("descriere")))
            cursor.moveToNext()
        }

        var community_fragment = supportFragmentManager.beginTransaction()
        community_fragment.replace(R.id.community_frame, Community_fragment(R.layout.community_people, arr_user, arr_word, arr_desc))
        community_fragment.commit()
    }
}

class Community_fragment(val xml_layout : Int, val arr_user : ArrayList<String>, val arr_word : ArrayList<String>, val arr_desc : ArrayList<String>) : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val community_lay_inf = inflater.inflate(xml_layout, container, false)

        val recycle : RecyclerView = community_lay_inf.findViewById(R.id.recycle)
        recycle.adapter = CommunityAdapter(arr_user, arr_word, arr_desc)
        recycle.layoutManager = LinearLayoutManager(community_lay_inf.context)

        return community_lay_inf
    }
}

class search_fragment(val xml_layout : Int, val db_write: SQLiteDatabase) : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inf_lay = inflater.inflate(xml_layout, container, false)

        val textinput : TextInputEditText = inf_lay.findViewById(R.id.community_search_input)

        textinput.addTextChangedListener(
            object : TextWatcher{
                override fun onTextChanged(str: CharSequence?, p1: Int, p2: Int, p3: Int){                      // live change
                    val cursor = db_write.rawQuery("SELECT nume , word , descriere " +
                            "FROM tabel WHERE " +
                            "nume LIKE '%${str.toString()}%' OR " +
                            "word LIKE '%${str.toString()}%' ", null)

                    cursor.moveToFirst()

                    val arr_user = ArrayList<String>()
                    val arr_word = ArrayList<String>()
                    val arr_desc = ArrayList<String>()

                    while(cursor.isAfterLast == false) {
                        arr_user.add(cursor.getString(cursor.getColumnIndexOrThrow("nume")))
                        arr_word.add(cursor.getString(cursor.getColumnIndexOrThrow("word")))
                        arr_desc.add(cursor.getString(cursor.getColumnIndexOrThrow("descriere")))
                        cursor.moveToNext()
                    }

                    if(str.toString() == ""){
                        arr_user.clear()
                        arr_desc.clear()
                        arr_word.clear()
                    }

                    val recycle : RecyclerView = inf_lay.findViewById(R.id.recycle_search)
                    recycle.adapter = CommunityAdapter(arr_user, arr_word, arr_desc)
                    recycle.layoutManager = LinearLayoutManager(inf_lay.context)
                }

                override fun afterTextChanged(p0: Editable?){
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){
                }
            }
        )

        return inf_lay
    }
}

class definition_fragment(val xml_layout : Int, val text : String?, val Fragment: FragmentManager, val JWI_DICT: Dictionary) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val inf_lay = inflater.inflate(xml_layout, container, false)

        val def_btn1 : Button = inf_lay.findViewById(R.id.def_btn1)
        val def_btn2 : Button = inf_lay.findViewById(R.id.def_btn2)
        var fragment = Fragment.beginTransaction()
        fragment.replace(R.id.def_frame, definition_fragment_aux1(R.layout.definition_search, text, JWI_DICT, Fragment, 0, ""))
        fragment.commit()

        def_btn1.setOnClickListener() {
            fragment = Fragment.beginTransaction()
            fragment.replace(
                R.id.def_frame,
                definition_fragment_aux1(R.layout.definition_search, text, JWI_DICT, Fragment, 0, "")
            )
            fragment.commit()
        }

        def_btn2.setOnClickListener() {
            fragment = Fragment.beginTransaction()
            fragment.replace(
                R.id.def_frame,
                definition_fragment_aux1(R.layout.definition_guess, text, JWI_DICT, Fragment, 0, "")
            )
            fragment.commit()
        }

        return inf_lay
    }
}

class definition_fragment_aux1(val xml_layout : Int, val text : String?, val JWI_DICT: Dictionary, val Fragment: FragmentManager, var flag_auto : Int, val str_auto : String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inf_lay = inflater.inflate(xml_layout, container, false)
        val db_write = DataBaseHelper(inf_lay.context).writableDatabase

        if (xml_layout == R.layout.definition_search) {

            val def_btn: Button = inf_lay.findViewById(R.id.def_search_btn)
            val rand_btn : Button = inf_lay.findViewById(R.id.def_rand_word)
            val word: TextView = inf_lay.findViewById(R.id.def_word_out)
            val definition: TextView = inf_lay.findViewById(R.id.def_def_out)
            val synonym: TextView = inf_lay.findViewById(R.id.def_syn_out)
            val antonym: TextView = inf_lay.findViewById(R.id.def_ant_out)
            val related: TextView = inf_lay.findViewById(R.id.def_mer_out)
            val example: TextView = inf_lay.findViewById(R.id.def_ex_out)
            val save_btn : Button = inf_lay.findViewById(R.id.def_guess_save)

            var text_inp : String = ""
            var flag_ok : Int = 0

            def_btn.setOnClickListener() {

                if(text_inp == "")
                    text_inp = inf_lay.findViewById<TextInputEditText>(R.id.def_input).text.toString()

                if(text_inp.isBlank() == false) {
                    if (flag_ok == 0 && flag_auto == 0)
                        text_inp =
                            inf_lay.findViewById<TextInputEditText>(R.id.def_input).text.toString()

                    flag_ok = 0
                    flag_auto = 0

                    val pointer_list_syn = ArrayList<Pointer>()
                    pointer_list_syn.add(Pointer.SIMILAR_TO)
                    pointer_list_syn.add(Pointer.HYPERNYM)

                    word.text = text_inp
                    definition.text = WordNetHelper().getDefinition(JWI_DICT, text_inp, 1)
                    synonym.text = WordNetHelper().getText(JWI_DICT, text_inp, pointer_list_syn)
                    example.text = WordNetHelper().getDefinition(JWI_DICT, text_inp, 2)

                    pointer_list_syn.clear()
                    pointer_list_syn.add(Pointer.HOLONYM_MEMBER)
                    pointer_list_syn.add(Pointer.HYPONYM)
                    pointer_list_syn.add(Pointer.HOLONYM_SUBSTANCE)
                    pointer_list_syn.add(Pointer.HOLONYM_PART)
                    pointer_list_syn.add(Pointer.ATTRIBUTE)
                    pointer_list_syn.add(Pointer.DERIVATIONALLY_RELATED)
                    pointer_list_syn.add(Pointer.MERONYM_MEMBER)
                    pointer_list_syn.add(Pointer.MERONYM_PART)

                    related.text = WordNetHelper().getText(JWI_DICT, text_inp, pointer_list_syn)
                    antonym.text = WordNetHelper().getAntonyms(JWI_DICT, text_inp)
                }
            }

            save_btn.setOnClickListener(){

                val cursor = db_write.rawQuery("SELECT cuvant FROM $text WHERE cuvant = '${word.text.toString().capitalize()}' ", null)

                if(cursor.count == 0 && definition.text != "---")
                    db_write.execSQL("INSERT INTO $text (cuvant) VALUES ('${word.text.toString().capitalize()}')")
            }

            rand_btn.setOnClickListener() {

                text_inp = getRandWrd(inf_lay)
                flag_ok = 1
                def_btn.performClick()
            }

            if(flag_auto == 1) {
                text_inp = str_auto
                inf_lay.findViewById<TextInputEditText>(R.id.def_input).setText(text_inp)
                def_btn.performClick()
            }
        }
        else{

            val cursor = db_write.rawQuery("SELECT * FROM tabel WHERE nume = '${text}' ", null)
            cursor.moveToFirst()
            val text_inp : TextInputEditText = inf_lay.findViewById(R.id.def_guess_input)
            val btn_change : Button = inf_lay.findViewById(R.id.def_guess_change)
            val btn_info : Button = inf_lay.findViewById(R.id.def_guess_info)
            val btn_save : Button = inf_lay.findViewById(R.id.def_guess_save)
            val btn_check : Button = inf_lay.findViewById(R.id.def_verify_btn)
            val descrypt : TextView = inf_lay.findViewById(R.id.def_def_out)
            val verdict : TextView = inf_lay.findViewById(R.id.def_guess_final)
            var rand_wd = getRandWrd(inf_lay)

           descrypt.text = WordNetHelper().getDefinition(JWI_DICT, rand_wd, 1)

            btn_change.setOnClickListener(){
                rand_wd = getRandWrd(inf_lay)
                descrypt.text = WordNetHelper().getDefinition(JWI_DICT, rand_wd, 1)
            }

            btn_info.setOnClickListener(){
                val fragment = Fragment.beginTransaction()
                fragment.replace(R.id.def_frame, definition_fragment_aux1(R.layout.definition_search, text, JWI_DICT, Fragment, 1, rand_wd))
                fragment.commit()
            }

            btn_check.setOnClickListener(){

                var ok = 0
                var pos_list = ArrayList<POS>()
                pos_list.add(POS.NOUN)
                pos_list.add(POS.VERB)
                pos_list.add(POS.ADJECTIVE)
                pos_list.add(POS.ADVERB)

                rand_wd = rand_wd.lowercase()

                for(pos in pos_list){
                    var text_inp_lemma = WordNetHelper().getLemma(JWI_DICT, text_inp.text.toString(), pos)

                    text_inp_lemma = text_inp_lemma.lowercase()

                    if(text_inp_lemma == rand_wd){

                        ok = 1
                        break
                    }
                }

                val counter = cursor.getInt(cursor.getColumnIndexOrThrow("contor"))

                if(ok == 1) {
                    verdict.text = "Correct"
                    db_write.execSQL("UPDATE tabel SET contor = ${counter + 1} WHERE nume = '$text' ")
                }
                else
                    verdict.text = "Wrong"
            }

            btn_save.setOnClickListener(){
                val db_write = DataBaseHelper(inf_lay.context).writableDatabase
                val cursor = db_write.rawQuery("SELECT cuvant FROM $text WHERE cuvant = '$rand_wd' ", null)

                if(cursor.count == 0)
                    db_write.execSQL("INSERT INTO $text (cuvant) VALUES ('${rand_wd}')")
            }
        }

        return inf_lay
    }

    fun getRandWrd(inf_lay: View) : String {

        val buf = inf_lay.resources.openRawResource(R.raw.common_english).bufferedReader()
        var rand_nr = Random.nextInt(0, 700)
        var word : String = ""

        while(rand_nr > 0){
            word = buf.readLine()
            rand_nr--
        }

        buf.close()

        return word
    }
}

class saved_frament(val xml_layout: Int, val text : String?) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inf_lay = inflater.inflate(xml_layout, container, false)

        val recycler : RecyclerView = inf_lay.findViewById(R.id.recycle_search)
        val str_array = ArrayList<String>()
        val db_write = DataBaseHelper(inf_lay.context).writableDatabase

        val cursor = db_write.rawQuery("SELECT cuvant FROM ${text}", null)
        cursor.moveToFirst()

        if(cursor.count > 0) {
            while (cursor.isAfterLast == false) {
                str_array.add(cursor.getString(cursor.getColumnIndexOrThrow("cuvant")))
                cursor.moveToNext()
            }
        }

        recycler.adapter = adapter_saved(str_array)
        recycler.layoutManager = LinearLayoutManager(inf_lay.context)

        return inf_lay
    }
}