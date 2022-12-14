package com.example.translateapp
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.translateapp.database.DicoApplication
import com.example.translateapp.database.entity.Dictionnaire
import com.example.translateapp.database.entity.Mot
import kotlin.concurrent.thread

class ViewModel(application: Application) : AndroidViewModel(application) {

    val dao = (application as DicoApplication).database.MyDao()

    //val allMots : LiveData<List<Mot>> = dao.loadAllMots()

    var insertInfo = MutableLiveData<Long>(0)
    var certainsDictionnaires = MutableLiveData<List<Dictionnaire>>()
    var certainsMots = MutableLiveData<List<Mot>>()

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

    fun loadAllDicos() = dao.loadAllDictionnaires()

    fun loadAllMots() = dao.loadAllMots()

    fun loadDictionnaire(url: String, startLang: String, endLang: String) =
        dao.loadDictionnaire(url, startLang, endLang)

    fun loadDictionnaireDeMot(mot: String, endLang: String) =
        thread { certainsDictionnaires.postValue(dao.loadDictionnaireDeMot(mot, endLang)) }

    fun loadMot(mot: String, endLang: String) =
        thread { certainsMots.postValue(dao.loadMot(mot, endLang)) }

    fun loadAllMotsNeedToBeLearn(bool: Boolean) = dao.loadAllMotsNeedToBeLearn(bool)


}