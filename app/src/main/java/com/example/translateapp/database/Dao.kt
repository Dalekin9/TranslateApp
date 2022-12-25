package com.example.translateapp.database
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.translateapp.database.entity.Dictionnaire
import com.example.translateapp.database.entity.Mot

@Dao
interface Dao {

    /*
    **************
    * INSERTIONS *
    **************
     */

    @Insert(entity = Dictionnaire::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertDico(vararg dictionnaire: Dictionnaire): List<Long>

    @Insert(entity = Mot::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertMot(vararg mot: Mot): List<Long>

    /*
    **********
    * UPDATE *
    **********
     */

    @Update
    fun updateMot(mot: Mot): Int


    /*
    **********
    * REMOVE *
    **********
     */

    @Delete
    fun deleteMot(mot: Mot): Int

    /*
    *********
    * LOADS *
    *********
     */

    @Query("SELECT * FROM Dictionnaire")
    fun loadAllDictionnaires(): LiveData<List<Dictionnaire>>

    @Query("SELECT * FROM Mot")
    fun loadAllMots(): LiveData<List<Mot>>

    @Query("SELECT * FROM Mot WHERE toLearn = :learn")
    fun loadAllMotsNeedToBeLearn(learn: Boolean): LiveData<List<Mot>>

    //@Query("SELECT * FROM Dictionnaire WHERE url LIKE 'http_//%' || :url || '.%' AND startLanguage = :startLang AND endLanguage = :endLang ")
    @Query("SELECT * FROM Dictionnaire WHERE url LIKE :url AND startLanguage = :startLang AND endLanguage = :endLang ")
    fun loadDictionnaire(url: String, startLang: String, endLang: String): LiveData<Dictionnaire>

    @Query("SELECT * FROM Dictionnaire INNER JOIN Mot WHERE word = :mot AND idDico = dictionnary AND endLanguage = :endLang")
    fun loadDictionnaireDeMot(mot: String, endLang: String): List<Dictionnaire>

    @Query("SELECT * FROM Mot INNER JOIN Dictionnaire WHERE word = :mot AND idDico = dictionnary AND endLanguage = :endLang")
    fun loadMot(mot: String, endLang: String): List<Mot>

    @Query("SELECT * FROM Mot WHERE initLanguage = :startLang AND tradLanguage = :endLang AND toLearn = :learn")
    fun loadAllMotsNeedToBeLearnWithSpecificLanguages(
        learn: Boolean,
        startLang: String,
        endLang: String
    ): List<Mot>

    @Query("SELECT * FROM Mot WHERE initLanguage = :startLang AND tradLanguage = :endLang")
    fun loadCertainsMot(startLang: String, endLang: String): List<Mot>

}
