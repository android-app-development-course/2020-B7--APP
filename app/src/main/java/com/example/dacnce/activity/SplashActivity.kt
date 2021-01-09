package com.example.dacnce.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobUser
import com.example.dacnce.DanceApplication
import com.example.dacnce.MainActivity
import com.example.dacnce.utils.NetworkUtils
import site.gemus.openingstartanimation.LineDrawStrategy
import site.gemus.openingstartanimation.OpeningStartAnimation
import com.example.dacnce.R
import com.example.dacnce.bean.User
import com.example.dacnce.utils.SPUtils
import es.dmoral.toasty.Toasty

class SplashActivity : BaseActivity() {

    companion object{
        const val SPLASH_DISPLAY_LENGTH = 1800L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)

        if(NetworkUtils.isConnected()){
            Bmob.initialize(this,NetworkUtils.APP_ID)
        }

        val openingStartAnimation = OpeningStartAnimation.Builder(this)
            .setDrawStategy(LineDrawStrategy())
            .setAppIcon(resources.getDrawable(R.drawable.ic_begin))
            .setColorOfAppName(R.color.icon_color)
            .setAppStatement("这就是街舞")
            .setColorOfAppStatement(R.color.red0)
            .create()
        openingStartAnimation.show(this)

        Handler().postDelayed(Runnable {

            if(NetworkUtils.isConnected()){
                if(BmobUser.getCurrentUser(User::class.java) == null ){
                    val mainIntent = Intent(this,LoginActivity::class.java)
                    startActivity(mainIntent)
                    SPUtils.put(DanceApplication.context,"isLogin",false)
                    finish()
                }else if(!(SPUtils.get(DanceApplication.context,"isLogin",false) as Boolean)){
                    val mainIntent = Intent(this,LoginActivity::class.java)
                    startActivity(mainIntent)
                    SPUtils.put(DanceApplication.context,"isLogin",false)
                    finish()
                }else{
                    val mainIntent = Intent(this,MainActivity::class.java)
                    startActivity(mainIntent)
                    //SPUtils.put(DanceApplication.context,"isLogin",false)
                    finish()
                }
            }else{
                Toasty.error(this,"无网络连接",Toast.LENGTH_SHORT).show()
                val mainIntent = Intent(this,MainActivity::class.java)
                startActivity(mainIntent)
                SPUtils.put(DanceApplication.context,"isLogin",false)
                finish()
            }
//            val mainIntent = Intent(this,LoginActivity::class.java)
//            startActivity(mainIntent)
//            SPUtils.put(DanceApplication.context,"isLogin",false)
//            finish()
        },SPLASH_DISPLAY_LENGTH)
    }
}