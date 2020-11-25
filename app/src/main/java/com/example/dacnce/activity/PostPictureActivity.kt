
package com.example.dacnce.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dacnce.R
import com.example.dacnce.activity.PostPicture
import com.example.dacnce.adapter.PostPictureAdapter

import kotlinx.android.synthetic.main.activity_post_picture.*

class PostPictureActivity : AppCompatActivity() {
    val PostPictures = mutableListOf<PostPicture>()
    val PostPictureList = ArrayList<PostPicture>()
    lateinit var adapter: PostPictureAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_picture)
        val layoutManager = GridLayoutManager(this, 3)
        postPictureRecyclerView.layoutManager = layoutManager
        adapter = PostPictureAdapter(this, PostPictureList)
        postPictureRecyclerView.adapter = adapter

        postPictureAdd.setOnClickListener {
            //从相册选择图片，但图片数量为9是，隐藏按钮
            //打开文件选择器
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            //指定只显示图片
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
        postPictureCancel.setOnClickListener {
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
//                        val bitmap = getBitmapFromUri(uri)
//                        PostPictureList.add(bitmap)
                        val postPicture = PostPicture(uri)
                        PostPictureList.add(postPicture)
                        adapter.notifyDataSetChanged()
                        if (PostPictureList.size == 9) {
                            postPictureAdd.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    }
}

