package com.example.translateapp.database.entity
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.translateapp.database.entity.Dictionnaire

@Entity()
data class Mot(
    var word:String,
    var translation:String,
    var urlTransl:String,
    var dictionnary:Long,
    var toLearn:Boolean,
    //var learningEnding:Date,
    var knowledge:Int,
    @PrimaryKey(autoGenerate = true) val idMot: Long = 0
)