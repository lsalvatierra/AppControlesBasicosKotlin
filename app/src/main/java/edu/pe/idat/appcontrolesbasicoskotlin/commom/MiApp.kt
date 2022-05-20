package edu.pe.idat.appcontrolesbasicoskotlin.commom

import android.app.Application

class MiApp : Application() {
    companion object{
        lateinit var instance: MiApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


}