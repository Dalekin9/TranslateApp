package com.example.translateapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.translateapp.databinding.ActivitySauvegardeBinding
import com.example.translateapp.database.entity.Dictionnaire
import com.example.translateapp.database.entity.Mot

class SauvegardeActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySauvegardeBinding.inflate(layoutInflater) }

    private val model by lazy { ViewModelProvider(this)[ViewModel::class.java] }

    private var dicoID: Long = -1
    private lateinit var dicoURL : String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if( intent.action.equals( "android.intent.action.SEND" ) ){
            val url = intent.extras?.getString( "android.intent.extra.TEXT" )
            // si txt différent de null alors txt contient maintenant une référence vers un String
            // avec l’adresse Url
            // de la page qui s’affichait dans le navigateur
            if (url != null) {
                binding.url.text = url
                dicoURL = url
            }
        }
    }

    fun addMot(view: View) {
        //verif mot init non vide
        val motInit = binding.word.text.toString().trim()
        //verif mot trad nn vide
        val motTrad = binding.wordTrad.text.toString().trim()
        if (motInit != "" && motTrad != "") {
            //check if dico existe
            val langueInit = binding.languesourc.selectedItem.toString()
            val langueTrad = binding.languedest.selectedItem.toString()
            val isDico = model.loadDictionnaire(dicoURL, langueInit, langueTrad)
            // si oui mettre a jour les vars
            if (isDico.value != null) {
                dicoID = isDico.value!!.idDico
            } else {
                // sinon creer un dico + insertDico + mettre a jour les vars
                val dico = Dictionnaire(dicoURL, langueInit, langueTrad)
                dicoID = 2
                val resultInsertion = model.insertDico(dico)
                Log.d("INSERT", "Insertion dico : ${model.insertInfo.value}")
            }
            // TODO check si le mot n'a pas deja ete add
            // ajout dans la bdd
            val mot = Mot(motInit, motTrad, dicoURL, dicoID,true, 0)
            val resultInsertion = model.insertMot(mot)
            Log.d("INSERT", "Insertion mot : ${model.insertInfo.value}")

            val act = Intent(this, MainActivity::class.java)
            startActivity(act)
        }
        // TODO affiche message pour pas que les champs soient vides
        //Log.d("INSERT", "Insertion mot : $resultInsertion")
    }
}