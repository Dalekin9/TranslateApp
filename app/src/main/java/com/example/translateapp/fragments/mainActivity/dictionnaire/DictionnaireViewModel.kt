package com.example.translateapp.fragments.mainActivity.dictionnaire

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.translateapp.database.DicoBD

class DictionnaireViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private val dao = DicoBD.getDatabase(application).MyDao()

    fun loadAllMots() = dao.loadAllMots()
}