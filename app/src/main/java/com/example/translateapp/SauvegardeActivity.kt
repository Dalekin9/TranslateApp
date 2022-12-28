package com.example.translateapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.translateapp.database.entity.Dictionnaire
import com.example.translateapp.database.entity.Mot
import com.example.translateapp.databinding.ActivitySauvegardeBinding

class SauvegardeActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySauvegardeBinding.inflate(layoutInflater) }

    private val model by lazy { ViewModelProvider(this)[ViewModel::class.java] }

    private var dicoID: Long = -1
    private lateinit var dicoURL : String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val button = binding.button
        button.setOnClickListener { addMot() }

        if (intent.action.equals("android.intent.action.SEND")) {
            val url = intent.extras?.getString("android.intent.extra.TEXT")
            // si txt différent de null alors txt contient maintenant une référence vers un String
            // avec l’adresse Url
            // de la page qui s’affichait dans le navigateur
            if (url != null) {
                binding.url.text = url
                dicoURL = url
            }
        }

        model.allMots

        model.allDictionnaires

        model.allMots.observe(this) {
            Log.i("INSERT TEST", "j'ai ${it.size} mots")
            for (mot: Mot in it) {
                Log.i(
                    "INSERT TEST",
                    "mot ${mot.idMot}: ${mot.word}, ${mot.translation}, ${mot.dictionnary}, ${mot.urlTransl}"
                )
            }
        }

        model.allDictionnaires.observe(this) {
            Log.i("INSERT TEST", "j'ai ${it.size} dictionnaires")
            for (mot: Dictionnaire in it) {
                Log.i("INSERT TEST", "dico ${mot.idDico}: ${mot.url}")
            }
        }

        model.certainsDictionnaires.observe(this) {
            Log.i("INSERT TEST", "j'ai ${it.size} dictionnaires")
            val motInit = binding.word.text.toString().trim()
            val motTrad = binding.wordTrad.text.toString().trim()
            val langueInit = binding.languesourc.selectedItem.toString()
            val langueTrad = binding.languedest.selectedItem.toString()
            // si oui mettre a jour les vars
            if (it.isNotEmpty()) {
                dicoID = model.certainsDictionnaires.value!![0].idDico
                model.loadMot(motInit, langueTrad)
            } else {
                // sinon creer un dico + insertDico + mettre a jour les vars
                val dico = Dictionnaire(dicoURL, langueInit, langueTrad)
                val mot = Mot(motInit, motTrad, dicoURL, dicoURL, true, langueInit, langueTrad, 0)
                model.insertMotAndDictionnaireOfMot(mot, dico)
                Log.d("INSERT TEST", "on a pas de dico")
                val act = Intent(this, MainActivity::class.java)
                startActivity(act)
            }
        }

        model.certainsMots.observe(this) {
            Log.i("INSERT TEST", "j'ai ${it.size} mots")
            val motInit = binding.word.text.toString().trim()
            val motTrad = binding.wordTrad.text.toString().trim()
            val langueInit = binding.languesourc.selectedItem.toString()
            val langueTrad = binding.languedest.selectedItem.toString()
            if (it.isEmpty()) {
                //Le mot n'a pas encore été ajouté à la bdd
                val mot = Mot(motInit, motTrad, dicoURL, dicoURL, true, langueInit, langueTrad, 0)
                model.insertMot(mot)
                Log.d("INSERT TEST", "on a un dico qui existe, et pas de mot existant")
            } else {
                //TODO: Afficher un toast "d'erreur" ?
                Log.d("INSERT TEST", "on a un dico qui existe, et un mot existant")
            }
            val act = Intent(this, MainActivity::class.java)
            startActivity(act)
        }
    }

    private fun addMot() {
        val motInit = binding.word.text.toString().trim()
        val motTrad = binding.wordTrad.text.toString().trim()
        if (motInit != "" && motTrad != "") {
            val langueInit = binding.languesourc.selectedItem.toString()
            val langueTrad = binding.languedest.selectedItem.toString()
            model.loadDictionnaire(dicoURL, langueInit, langueTrad)
        } else {
            //TODO: Afficher un toast mots vide ?
        }
    }

}