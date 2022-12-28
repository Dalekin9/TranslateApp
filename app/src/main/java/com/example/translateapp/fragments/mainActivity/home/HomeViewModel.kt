package com.example.translateapp.fragments.mainActivity.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.translateapp.database.DicoBD
import com.example.translateapp.database.entity.Dictionnaire
import com.example.translateapp.database.entity.Mot
import kotlin.concurrent.thread

class HomeViewModel(application: Application) : AndroidViewModel(application) {


    private val dao = DicoBD.getDatabase(application).MyDao()

    /*
    ---------------
     */

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    /*
    ---------------
    */

    var certainsMots = MutableLiveData<List<Mot>>()

    fun loadMot(mot: String, endLang: String) = thread{ certainsMots.postValue(dao.loadMot(mot, endLang))}

    /*
    ---------------
     */

    var dictionnaires : LiveData<List<Dictionnaire>> = dao.loadAllDictionnaires()

    var certainsDictionnaires = MutableLiveData<List<Dictionnaire>>()


    fun loadDictionnaire(url: String, startLang: String, endLang: String) = thread{ certainsDictionnaires.postValue(dao.loadDictionnaire(url, startLang, endLang))}
}