package com.example.translateapp.database.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Mot(
    var word: String,
    var translation: String,
    var urlTransl: String,
    var dictionnary: Long,
    var toLearn: Boolean,
    var initLanguage: String,
    var tradLanguage: String,
    //var learningEnding:Date,
    var knowledge: Int,
    @PrimaryKey(autoGenerate = true) val idMot: Long = 0
)