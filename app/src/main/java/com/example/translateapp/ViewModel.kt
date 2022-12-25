package com.example.translateapp
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.translateapp.database.DicoApplication
import com.example.translateapp.database.entity.Dictionnaire
import com.example.translateapp.database.entity.Mot

class ViewModel(application: Application) : AndroidViewModel(application) {

    val dao = (application as DicoApplication).database.MyDao()

    var insertInfo = MutableLiveData<Long>(0)

    fun insertDico(vararg dictionnaire: Dictionnaire){
        Thread{
            val l = dao.insertDico(*dictionnaire)
            Log.d("INSERT", "dans Insertion dico")
            insertInfo.postValue(if (l[0] == -1L) -1L else l[0])
        }.start()
    }

    fun insertMot(vararg mot: Mot){
        Thread{
            val l = dao.insertMot(*mot)
            Log.d("INSERT", "dans Insertion mot")
            insertInfo.postValue(if(l[0] == -1L) -1L else l[0])
        }.start()
    }

    fun loadDictionnaire(url: String, startLang: String, endLang: String) =
        dao.loadDictionnaire(url, startLang, endLang)

}