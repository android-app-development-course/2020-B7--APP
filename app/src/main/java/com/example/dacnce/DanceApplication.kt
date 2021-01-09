package com.example.dacnce

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import cn.bmob.v3.Bmob
import com.example.dacnce.utils.NetworkUtils

class DanceApplication: Application() {

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        if(NetworkUtils.isConnected()){
            Bmob.initialize(context, NetworkUtils.APP_ID)
        }
    }
}