package com.example.translateapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.translateapp.databinding.ActivitySauvegardeBinding

class SauvegardeActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySauvegardeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if( intent.action.equals( "android.intent.action.SEND" ) ){
            val txt = intent.extras?.getString( "android.intent.extra.TEXT" )
            // si txt différent de null alors txt contient maintenant une référence vers un String
            // avec l’adresse Url
            // de la page qui s’affichait dans le navigateur
            if (txt != null) {
                binding.url.text = txt
            }
        }
    }
}