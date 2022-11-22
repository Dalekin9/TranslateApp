package com.example.translateapp
import android.app.Application

class DicoApplication : Application(){
    val database by lazy{
        DicoBD.getDatabase(this)
    }
}