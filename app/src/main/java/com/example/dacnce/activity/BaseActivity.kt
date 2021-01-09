package com.example.dacnce.activity

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cn.bmob.v3.Bmob
import com.example.dacnce.receiver.NetworkReceiver
import com.example.dacnce.utils.NetworkUtils

/**
 * 基类
 */
open class BaseActivity : AppCompatActivity() {

    private var isRegistered: Boolean = false
    private lateinit var networkReceiver: NetworkReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Log.d("BaseActivity",javaClass.simpleName)

        ActivityCollector.addActivity(this)

        //初始化
        if(NetworkUtils.isConnected()){
            Bmob.initialize(this,NetworkUtils.APP_ID)
        }
        //注册网络状态监听广播
        networkReceiver = NetworkReceiver()
        //过滤器
        val filter = IntentFilter()
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        //注册
        registerReceiver(networkReceiver,filter)
        isRegistered = true

    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)

        //解绑
        if(isRegistered)
            unregisterReceiver(networkReceiver)
    }

}