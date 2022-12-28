package com.example.translateapp.service

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.translateapp.database.DicoApplication
import com.example.translateapp.database.entity.Mot

class ServiceModel(application: Application) : AndroidViewModel(application) {

    val dao = (application as DicoApplication).database.MyDao()

    var updateInfo = MutableLiveData<Int>(0)
    fun updateMot(mot: Mot) {
        Thread {
            val l = dao.updateMot(mot)
            Log.d("REMOVE", "dans update mot, value $l")
            updateInfo.postValue(l)
        }.start()
    }

    var loadMotsLearnLanguages = MutableLiveData<List<Mot>>()
    fun loadAllMotNeedToBeLearnWithLanguages(bool: Boolean, langue1: String, langue2: String) {
        Thread {
            Log.d("REMOVE", "load all mots with languages")
            loadMotsLearnLanguages.postValue(
                dao.loadAllMotsNeedToBeLearnWithSpecificLanguages(
                    bool,
                    langue1,
                    langue2
                )
            )
        }.start()
    }

    var loadMotsLearn = MutableLiveData<List<Mot>>()
    fun loadAllMotNeedToBeLearn(bool: Boolean) {
        Thread {
            Log.d("REMOVE", "load all mots")
            loadMotsLearn.postValue(dao.loadAllMotsNeedToBeLearn(bool))
        }.start()
    }

}