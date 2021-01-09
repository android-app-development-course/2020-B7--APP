package com.example.dacnce.activity

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.example.dacnce.DanceApplication
import com.example.dacnce.MainActivity
import com.example.dacnce.R
import com.example.dacnce.adapter.PostPictureAdapter
import com.example.dacnce.bean.PostModel
import com.example.dacnce.bean.PostPicture
import com.example.dacnce.bean.User
import com.example.dacnce.utils.*
import kotlinx.android.synthetic.main.activity_post_picture.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.nio.charset.Charset
import kotlin.concurrent.thread


class PostPictureActivity : AppCompatActivity() {
    private val postPictureList = ArrayList<PostPicture>()

    private var mSaveDialog: ProgressDialog? = null

    lateinit var adapter: PostPictureAdapter

    private lateinit var user: User

    /*private val messageHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            mSaveDialog!!.dismiss()
            Toast.makeText(
                this@PostPictureActivity,
                "发帖成功",
                Toast.LENGTH_SHORT
            ).show()
        }
    }*/

    private val messageHandler =
        Handler(Handler.Callback {
            mSaveDialog!!.dismiss()
            Toast.makeText(
                this@PostPictureActivity,
                "发帖成功",
                Toast.LENGTH_SHORT
            ).show()
            false })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_picture)
        val layoutManager = GridLayoutManager(this, 3)
        postPictureRecyclerView.layoutManager = layoutManager
        adapter = PostPictureAdapter(this, postPictureList)
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

        // 申请并获得权限
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }

        if(isNetwork(this)){
            user = BmobUser.getCurrentUser(User::class.java)
        }

        postPictureCommit.setOnClickListener {
            /*有网络，可以发帖啦*/
            if (isNetwork(this)) {
                /*先上传文件到服务器*/
                if (postPictureList.isNotEmpty()) {

                    val picAbsolutes = ArrayList<String>()
                    for (item in 0 until postPictureList.size) {
                        val picPath = getPathByUri4kitkat(
                            this@PostPictureActivity,
                            postPictureList[item].imageId
                        )
                        if (picPath != null) {
                            picAbsolutes.add(picPath)
                        }
                    }


                    thread {
                        runOnUiThread {
                            mSaveDialog = ProgressDialog.show(
                                this@PostPictureActivity,
                                "上传图片",
                                "图片正在上传，请稍等...",
                                true
                            )
                        }

                        /*当前用户昵称*/
                        val user:User=BmobUser.getCurrentUser(User::class.java)
                        multipleFileUpload(object :Callback{
                            override fun onFailure(call: Call, e: IOException) {
                                runOnUiThread {
                                    Toast.makeText(
                                        this@PostPictureActivity,
                                        "上传失败！",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    //Log.d("shibai",e.toString())

                                    messageHandler.sendMessage(messageHandler.obtainMessage())
                                }

                            }

                            override fun onResponse(call: Call, response: Response) {

                                var pathOnWeb = "NoPath"

                                val userName = user.objectId.toString()

                                val stringBuilder=StringBuilder()

                                val pathArray=ArrayList<String>()

                                //然后将其转为gb2312
                                pathOnWeb = String(response.body!!.bytes(), Charset.forName("GB2312"))

                                if(pathOnWeb.isNotEmpty()){
                                    pathArray.addAll(pathOnWeb.split("@@"))
                                }

                                for (i in 0 until pathArray.size-1){

                                    var str= pathArray[i]

                                    str=str.substring(str.lastIndexOf(userName))

                                    str = NetworkUtils.PIC_PRE_PATH + "/$str,"

                                    stringBuilder.append(str)
                                }

                                /*写入云端数据库*/
                                val post = PostModel(
                                    userName,
                                    postPictureText.text.toString(),
                                    stringBuilder.toString(),
                                    "NoPath",
                                    "pic",
                                    user
                                )

                                createOne(post)

                                messageHandler.sendMessage(messageHandler.obtainMessage())

                                finish()
                            }

                        },picAbsolutes,user.objectId.toString())
                    }

                }


            } else {
                /*没有网络发不了*/
                AlertDialog.Builder(this)
                    .setTitle("无法连接网络！")
                    .setPositiveButton(
                        "确定",
                        DialogInterface.OnClickListener { _, _ ->
                        }
                    ).setCancelable(true)
                    .create()
                    .show()
            }
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
                        val postPicture =
                            PostPicture(uri)
                        postPictureList.add(postPicture)
                        adapter.notifyDataSetChanged()
                        if (postPictureList.size == 9) {
                            postPictureAdd.visibility = View.GONE;
                        }
                    }
                }
            }
        }
    }

    private fun createOne(post: PostModel) {
        val intent = Intent(this, MainActivity::class.java)
        post.save(object : SaveListener<String>() {
            override fun done(objectId: String?, ex: BmobException?) {
                if (ex == null) {
                    Toast.makeText(this@PostPictureActivity, "帖子发布成功！", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    val count = SPUtils.get(DanceApplication.context,"dynamic_count",0).toString().toInt() + 1
                    SPUtils.put(DanceApplication.context,"dynamic_count",count)
                } else {
                    //Log.e("CREATE", "新增数据失败：" + ex.message)
                    AlertDialog.Builder(this@PostPictureActivity)
                        .setTitle("发布帖子失败！")
                        .setPositiveButton(
                            "确定",
                            DialogInterface.OnClickListener { _, _ ->
                            }
                        ).setCancelable(true)
                        .create()
                        .show()
                }
            }

        })
    }

}

