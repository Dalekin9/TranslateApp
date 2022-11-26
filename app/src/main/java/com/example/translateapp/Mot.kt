package com.example.translateapp
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(foreignKeys =
[ForeignKey(entity = Dictionnaire::class,
    parentColumns = ["idDico"],
    childColumns = [ "dictionnary" ],
    deferred = true,
    onDelete = ForeignKey.CASCADE)])

data class Mot(
    @PrimaryKey val idMot:Long = 0,
    var word:String,
    var translation:String,
    var dictionnary:Long,
    var toLearn:Boolean,
    //var learningEnding:Date,
    var knowledge:Int
)