package com.example.translateapp
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Dictionnaire(
        @PrimaryKey val idDico:Long = 0,
        var url:String,
        var startLanguage:String,
        var endLanguage:String
)
