package com.example.translateapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.translateapp.databinding.ActivityRechercheTradBinding

class RechercheTradActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRechercheTradBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }

    fun search(view: View){
        val intent = Intent(Intent.ACTION_VIEW)
        val dico = binding.dictionnary.selectedItem.toString()
        /*val url : String
        if(dico == "Google"){
            url = "http://www.google.fr/search?q=traduction+" + binding.word.text.toString() + binding.languedest.selectedItem.toString()
        }else{

        }*/
        // TODO val urlDico -> get url pour le dico selectionné
        val url = dico + binding.word.text.toString()
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

}