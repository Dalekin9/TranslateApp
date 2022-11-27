package com.example.translateapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.translateapp.databinding.ActivityRechercheTradBinding

class RechercheTradActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRechercheTradBinding.inflate(layoutInflater) }

    val model by lazy {
        ViewModelProvider(this).get(AjoutViewModel::class.java)
    }
    val app = DicoApplication()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }

    fun search(view: View){
        val intent = Intent(Intent.ACTION_VIEW)
        val dico = binding.dictionnary.selectedItem.toString()
        val startLang = binding.languesourc.selectedItem.toString()
        val endLang = binding.languedest.selectedItem.toString()
        val mot = binding.word.text.toString()

        var url = ""
        var dicosAvailable:List<Dictionnaire>
        if(dico == "Google"){
            url = "http://www.google.fr/search?q=traduction+" + mot + endLang
        }else{
            dicosAvailable = model.loadDictionnaireDeMot(dico, endLang)
            if(dicosAvailable.size != 0){   //Cas
                url = dicosAvailable[0].url
            }else{
                dicosAvailable = model.loadDictionnaire(dico, startLang, endLang)
                if(dicosAvailable.size != 0){
                    url = dicosAvailable[0].url + mot
                }
            }


        }
        // TODO val urlDico -> get url pour le dico selectionné

        intent.data = Uri.parse(url)
        startActivity(intent)
    }

}