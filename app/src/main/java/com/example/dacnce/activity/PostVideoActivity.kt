package com.example.dacnce.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.example.dacnce.DanceApplication
import com.example.dacnce.MainActivity
import com.example.dacnce.R
import com.example.dacnce.adapter.PostPictureAdapter
import com.example.dacnce.bean.PostModel
import com.example.dacnce.bean.User
import com.example.dacnce.utils.*
import kotlinx.android.synthetic.main.activity_post_video.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class PostVideoActivity : AppCompatActivity() {

    lateinit var adapter: PostPictureAdapter

    private lateinit var videoViewUri: Uri

    private var mSaveDialog: ProgressDialog? = null

    private lateinit var user: User

    /*private val messageHandler = object : Handler(){
        override fun handleMessage(msg: Message) {
            mSaveDialog!!.dismiss()
            Toast.makeText(
                this@PostVideoActivity,
                "发帖成功",
                Toast.LENGTH_SHORT
            ).show()
        }
    }*/

    private val messageHandler =
        Handler(Handler.Callback {
            mSaveDialog!!.dismiss()
            Toast.makeText(
                this@PostVideoActivity,
                "发帖成功",
                Toast.LENGTH_SHORT
            ).show()
            false })




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_video)
        postVideoAdd.setOnClickListener {
            //打开文件显示器，指定只显示视频
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "video/*"
            startActivityForResult(intent, 1)
        }
        postVideoCancel.setOnClickListener {
            finish()
        }

        if(isNetwork(this)){
            user = BmobUser.getCurrentUser(User::class.java)
        }

        postVideoCommit.setOnClickListener {
            /*有网络，可以发帖啦*/
            var videoPath="NoPath"
            var previewPath="NoPath"
            /*如果视频uri不为空*/
            if(videoViewUri.toString().isNotEmpty()){
                videoPath = getPathByUri4kitkat(this@PostVideoActivity, videoViewUri)!!

                val media = MediaMetadataRetriever()

                media.setDataSource(videoPath)

                previewPath=saveVideoPreview(media.frameAtTime)
            }

            if(isNetwork(this)){
                thread {

                    runOnUiThread {

                        postVideoView.pause()
                        mSaveDialog = ProgressDialog.show(
                            this@PostVideoActivity,
                            "上传视频",
                            "视频正在上传，请稍等...",
                            true
                        ) }

                    /*当前用户昵称*/
                    val user:User=BmobUser.getCurrentUser(User::class.java)
                    mp4Upload(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            runOnUiThread {
                                Toast.makeText(
                                    this@PostVideoActivity,
                                    "上传失败！",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            messageHandler.sendMessage(messageHandler.obtainMessage())
                        }

                        override fun onResponse(call: Call, response: Response) {
                            var pathOnWeb = "NoPath"

                            val userName = user.objectId.toString()

                            val pathList=ArrayList<String>()

                            val stringArray = ArrayList<String>()

                            //然后将其转为gb2312
                            pathOnWeb = String(response.body!!.bytes(), Charset.forName("GB2312"))

                            if(pathOnWeb.isNotEmpty()){
                                pathList.addAll(pathOnWeb.split("@@"))
                            }

                            for(i in 0 until pathList.size-1){
                                var str=pathList[i]

                                str=str.substring(str.lastIndexOf(userName))

                                str = NetworkUtils.PIC_PRE_PATH + "/$str,"

                                stringArray.add(str)

                            }


                            /*写入云端数据库*/
                            val post = PostModel(
                                userName,
                                postVideoText.text.toString(),
                                stringArray[0],
                                stringArray[1],
                                "mov",
                                user
                            )
                            createOne(post)

                            messageHandler.sendMessage(messageHandler.obtainMessage())

                        }
                    }, previewPath,videoPath,user.objectId.toString())
                }
            }
        }
    }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            when (requestCode) {
                1 -> {
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        data.data?.let { uri ->
                            //将隐藏的ViedeoView显示
                            postVideoView.visibility = View.VISIBLE
                            postVideoView.setVideoURI(uri)
                            postVideoView.start()

                            videoViewUri = uri

                            postVideoAdd.visibility = View.GONE


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
                        Toast.makeText(this@PostVideoActivity, "帖子发布成功！", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        val count = SPUtils.get(DanceApplication.context,"dynamic_count",0).toString().toInt() + 1
                        SPUtils.put(DanceApplication.context,"dynamic_count",count)
                    } else {
                        //Log.e("CREATE", "新增数据失败：" + ex.message)
                        AlertDialog.Builder(this@PostVideoActivity)
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

        private fun saveVideoPreview(bm: Bitmap?): String {
            val dirFile = getExternalFilesDir(null)!!.path

            val fileName = UUID.randomUUID().toString() + ".jpg"
            val myCaptureFile = File(dirFile + fileName)
            val bos = BufferedOutputStream(FileOutputStream(myCaptureFile))
            bm?.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bos.flush()
            bos.close()


            return myCaptureFile.absolutePath


        }


    }


