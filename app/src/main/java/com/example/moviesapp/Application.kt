package com.example.moviesapp

import android.app.Application
import com.example.moviesapp.localdatabase.LocalDataBase

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Database
        LocalDataBase.init(this)
    }

}