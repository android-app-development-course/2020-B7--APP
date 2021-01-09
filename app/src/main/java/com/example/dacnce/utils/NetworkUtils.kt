package com.example.dacnce.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.dacnce.DanceApplication

class NetworkUtils {

    companion object{

        const val APP_ID = "ca1186997e987d6205110e4c23547afb"
        private const val PROTOCOL = "http://"
        private const val SEVER_IP = "10.242.174.63"
        private const val PORT = "8080"
        private const val COM = "$PROTOCOL$SEVER_IP:$PORT"

        //IP1:10.243.129.166
        //IP2:192.168.43.178

        //文件保存路径
        const val PIC_PRE_PATH = "$COM/DanceFile"

        //web前缀目录
        const val SERVLET_PRE_PATH = "$COM/MyDanceFileWeb"

        //单个文件servlet
        const val POST_PIC_PATH_SERVLET = "$SERVLET_PRE_PATH/FileServlet"

        //多个文件servlet
        const val PIC_ARRAY_SERVLET = "$SERVLET_PRE_PATH/PicArrayServlet"

        //mp4 文件servlet
        const val MP4_SERVLET = "$SERVLET_PRE_PATH/Mp4Servlet"

        fun isConnected(): Boolean {
            val mConnectivityManager = DanceApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= 23) {
                //获取网络属性
                val networkCapabilities = mConnectivityManager.getNetworkCapabilities(mConnectivityManager.activeNetwork)
                if (networkCapabilities != null) {
                    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                }
            } else {
                val mNetworkInfo = mConnectivityManager.activeNetworkInfo
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isConnected
                }
            }
            return false
        }

    }
}