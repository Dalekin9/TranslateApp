package com.example.translateapp
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlin.concurrent.thread

class AjoutViewModel(application: Application) : AndroidViewModel(application) {
    val dao = (application as DicoApplication).database.MyDao()

    val insertInfo = MutableLiveData<Long>(0)

    fun insertDico(vararg dictionnaire: Dictionnaire){
        thread{
            val l = dao.insertDico(*dictionnaire)
            insertInfo.postValue(if (l[0] == -1L) -1L else l[0])
        }
    }

    fun insertMot(vararg mot: Mot){
        thread{
            val l = dao.insertMot(*mot)
            insertInfo.postValue(if(l[0] == -1L) -1L else l[0])
        }
    }

    fun loadDictionnaire(nom:String, startLang:String, endLang:String) = dao.loadDictionnaire(nom, startLang, endLang)

    fun loadDictionnaireDeMot(mot:String,  endLang:String) = dao.loadDictionnaireDeMot(mot, endLang)

    fun loadMot(mot: String) = dao.loadMot(mot)
}