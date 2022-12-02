package com.example.translateapp.database.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Dictionnaire(
        var url:String,
        var startLanguage:String,
        var endLanguage:String,
        @PrimaryKey(autoGenerate = true) val idDico: Long = 0
)
