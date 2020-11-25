package com.example.dacnce.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.dacnce.R
import kotlinx.android.synthetic.main.activity_post_video.*

class PostVideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_video)
        postVideoAdd.setOnClickListener {
            //打开文件显示器，指定只显示视频
            val intent= Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type="video/*"
            startActivityForResult(intent,1)
        }
        postVideoCancel.setOnClickListener{
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1->{
                if (resultCode==Activity.RESULT_OK&&data!=null){
                    data.data?.let { uri ->
                        //将隐藏的ViedeoView显示
                        postVideoVideoView.visibility = View.VISIBLE
                        postVideoVideoView.setVideoURI(uri)
                        postVideoVideoView.start()
                        postVideoAdd.visibility = View.GONE

                    }

                }
            }
        }
    }
}