package com.example.translateapp.fragments.mainActivity.statistiques

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.translateapp.database.DicoBD
import com.example.translateapp.database.entity.Mot
import kotlin.concurrent.thread

class StatistiquesViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text


    private val dao = DicoBD.getDatabase(application).MyDao()

    val insertInfo = MutableLiveData<Long>(0)
    fun insertMot(vararg mot: Mot) {
        thread {
            val l = dao.insertMot(*mot)
            insertInfo.postValue(if (l[0] == -1L) -1L else l[0])
        }
    }

    fun loadAllDicos() = dao.loadAllDictionnaires()
}