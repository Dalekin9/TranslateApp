package com.example.translateapp
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Dao {

    @Insert(entity = Dictionnaire::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertDico(vararg dictionnaire: Dictionnaire):List<Long>

    @Insert(entity = Mot::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertMot(vararg mot: Mot):List<Long>

    @Query("SELECT * FROM Dictionnaire")
    fun loadAllDictionnaries(): LiveData<List<Dictionnaire>>

   @Query("SELECT * FROM Dictionnaire WHERE url LIKE 'http_//%' || :nom || '.%' AND startLanguage = :startLang AND endLanguage = :endLang ")
    fun loadDictionnaire(nom:String, startLang:String, endLang:String ): List<Dictionnaire>

    @Query("SELECT * FROM Dictionnaire INNER JOIN Mot WHERE word = :mot AND idDico = dictionnary AND endLanguage = :endLang")
    fun loadDictionnaireDeMot(mot:String, endLang:String): List<Dictionnaire>

    @Query("SELECT * FROM Mot WHERE word LIKE :mot")
    fun loadMot(mot:String): List<Dictionnaire>

}
