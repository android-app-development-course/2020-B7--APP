package com.example.dacnce.activity

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import cn.jzvd.Jzvd
import com.bumptech.glide.Glide
import com.example.dacnce.R
import com.example.dacnce.utils.MyJzvdStd


class ShowMovActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_show_mov)

        val previewUrl= intent.getStringExtra("preview").toString()
        var videoUrl= intent.getStringExtra("url").toString()

        videoUrl=videoUrl.substring(0,videoUrl.length-1)

        val jzPlayerStd: MyJzvdStd = findViewById(R.id.videoplayer)



        jzPlayerStd.setUp(videoUrl,"视频动态")
        Glide.with(this)
            .load(previewUrl)
            .into(jzPlayerStd.posterImageView)

        jzPlayerStd.startVideo()
    }

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()

    }

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos()

        Log.d("diao","onPause())")
    }

    override fun onStart() {
        super.onStart()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        Log.d("diao","onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d("diao","onResume()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("diao","onRestart()")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("diao","onRestoreInstanceState")
    }
}