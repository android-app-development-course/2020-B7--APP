package com.example.dacnce.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import cn.bmob.v3.Bmob
import com.example.dacnce.utils.NetworkUtils

class NetworkReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        //判断当前的网络连接状态是否可用
        if(NetworkUtils.isConnected()){
            //当前网络状态可用
            Bmob.initialize(context,NetworkUtils.APP_ID)
            Log.i("网络状态","网络已连接")
        }else{
            //当前网络不可用
            Log.i("网络状态","无网络连接")
        }
    }

}