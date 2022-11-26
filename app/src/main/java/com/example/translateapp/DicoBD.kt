package com.example.translateapp
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.jvm.Volatile

@Database(entities = [Dictionnaire::class, Mot::class], version = 1)
abstract class DicoBD : RoomDatabase() {
        abstract fun MyDao() : Dao
        companion object {
                @Volatile
                private var instance: DicoBD? = null

                        fun getDatabase(context : Context): DicoBD {
                        if(instance != null) return instance!!
                        val db = Room.databaseBuilder(context.applicationContext, DicoBD::class.java, "dictionnaire")
                        .fallbackToDestructiveMigration().build()
                        instance = db
                        return instance!!
                }
        }
}
