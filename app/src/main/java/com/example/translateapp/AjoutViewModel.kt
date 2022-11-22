package com.example.translateapp
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlin.concurrent.thread

class AjoutViewModel(application: Application) : AndroidViewModel(application) {
    val dao = (application as DicoApplication).database.MyDao()

    val insertInfo = MutableLiveData<Long>(0)
    fun insertTrad(vararg dictionnaire: Dictionnaire){
        thread{
            val l = dao.insertDico(*dictionnaire)
            insertInfo.postValue(if (l[0] == -1L) -1L else l[0])
        }
    }
}