    package com.example.sql

    import android.content.Context
    import android.os.Bundle
    import android.util.Log
    import androidx.appcompat.app.AppCompatActivity
    import java.io.File
    import java.io.FileOutputStream
    import edu.mit.jwi.Dictionary
    import edu.mit.jwi.item.POS
    import edu.mit.jwi.item.Pointer
    import edu.mit.jwi.morph.WordnetStemmer

    class WordNetHelper {

        fun getWrd(context: Context): Dictionary {

            val dir = File(context.filesDir, "dict_files")
            dir.mkdir()

            val dir_list = context.assets.list("dict")

            if (dir_list != null) {
                for (ind in dir_list) {

                    val file_aset = context.assets.open("dict/${ind}")

                    val file_obj = File(dir, ind)

                    file_aset?.use { input ->
                        FileOutputStream(file_obj).use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }

            val dict = Dictionary(dir)
            dict.open()

            return dict
        }

        fun getDefinition(JWI_DICT : Dictionary, text_inp : String, flag : Int) : String? {

            if(text_inp.isBlank() == true)
                return "---"

            val pos_values = ArrayList<POS>()
            pos_values.add(POS.NOUN)
            pos_values.add(POS.ADJECTIVE)
            pos_values.add(POS.ADVERB)
            pos_values.add(POS.VERB)

            val str_array = ArrayList<String>()

            for(POS in pos_values){

                val word_index = JWI_DICT.getIndexWord(text_inp, POS)

                word_index?.let { wd ->
                    val wdId = wd.wordIDs.get(0)
                    val wd_gloss = JWI_DICT.getSynset(wdId.synsetID).gloss.toString()

                    if(flag == 1) {
                        val ind_stop = wd_gloss.indexOf("\"", 0)

                        if (ind_stop == -1)
                            str_array.add("-> ${wd_gloss}\n")
                        else
                            str_array.add("-> ${wd_gloss.substring(0, ind_stop)}\n")
                    }
                    else{
                        val ind_start = wd_gloss.indexOf("\"")

                        if(ind_start != -1)
                            str_array.add("-> ${wd_gloss.substring(ind_start)}\n") else {}
                    }
                }

                if(str_array.size == 3)
                    break
            }

            if(str_array.size != 0)
                return str_array.joinToString("")

            return "---"
        }

        fun getText(JWI_DICT : Dictionary, text_inp : String, pointer_list : ArrayList<Pointer>): String? {

            if(text_inp.isBlank() == true)
                return "---"

            val str_array_final = ArrayList<String>()                                 //temporary container

            for (ind in pointer_list) {

                for (POS in POS.values()) {

                    val word_index = JWI_DICT.getIndexWord(text_inp, POS)
                    val str_array = ArrayList<String>()

                    word_index?.let { wd ->

                        val wdId = wd.wordIDs.get(0)
                        val wd_synset = JWI_DICT.getSynset(wdId.synsetID)

                        val arr_array = wd_synset.getRelatedSynsets(ind)

                        for (x in arr_array) {
                            str_array.add(JWI_DICT.getSynset(x).words[0].lemma)

                            if (str_array.size == 8)
                                break
                        }
                    }

                    str_array_final.addAll(str_array)

                    if (str_array.size == 8)
                        break
                }
            }

            if(str_array_final.size != 0)
                return str_array_final.joinToString(" | ")

            return "---"
        }

        fun getAntonyms(JWI_DICT: Dictionary, text_inp: String) : String {

            if(text_inp.isBlank() == true)
                return "---"

            val str_array = ArrayList<String>()

            for(POS in POS.values()){

                val indexWord = JWI_DICT.getIndexWord(text_inp, POS)

                indexWord?.let{wd ->

                    val wdId = wd.wordIDs.get(0)
                    val wor_dict = JWI_DICT.getWord(wdId)

                    val relatedMap = wor_dict.relatedMap.get(Pointer.ANTONYM)

                    if(relatedMap != null){
                        for(ind in relatedMap){
                            val x = JWI_DICT.getWord(ind).lemma.toString()
                            str_array.add(x)

                            if(str_array.size == 8)
                                continue
                        }
                    }
                }
            }

            if(str_array.size != 0)
                return str_array.joinToString("")

            return "---"
        }

        fun getLemma(JWI_DICT: Dictionary, text_inp: String, POS : POS) : String {

            if(text_inp.isBlank() == true)
                return ""

            val stemmer = WordnetStemmer(JWI_DICT)
            val lemmas = stemmer.findStems(text_inp, POS)

            if(lemmas.size != 0)
                return lemmas[0]

            return ""
        }
    }
