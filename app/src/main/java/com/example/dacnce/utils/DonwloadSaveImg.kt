package com.example.dacnce.utils

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.util.*


/**
 * Created by YuShuangPing on 2018/12/12.
 */
object DonwloadSaveImg {
    private var context: Context? = null
    private var filePath: String? = null
    private var mBitmap: Bitmap? = null
    private var mSaveMessage = "失败"
    private const val TAG = "PictureActivity"
    private var mSaveDialog: ProgressDialog? = null

    fun donwloadImg(contexts: Context?, filePaths: String?) {
        context = contexts
        filePath = filePaths
        mSaveDialog =
            ProgressDialog.show(context, "保存图片", "图片正在保存中，请稍等...", true)
        Thread(saveFileRunnable).start()
    }

    private val saveFileRunnable = Runnable {
        try {
            if (!TextUtils.isEmpty(filePath)) { //网络图片
                // 对资源链接
                val url = URL(filePath)
                // 打开输入流
                val inputStream = url.openStream()
                //对网上资源进行下载转换位图图片
                mBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
            }
            saveFile(mBitmap)
            mSaveMessage = "图片保存成功！"
        } catch (e: IOException) {
            mSaveMessage = "图片保存失败！"
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        messageHandler.sendMessage(messageHandler.obtainMessage())
    }

    private val messageHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            mSaveDialog!!.dismiss()
            //Log.d(TAG, mSaveMessage)
            Toast.makeText(
                context,
                mSaveMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * 保存图片
     * @param bm
     * @throws IOException
     */
    @Throws(IOException::class)
    fun saveFile(bm: Bitmap?) {
        val dirFile = context!!.getExternalFilesDir(null)!!.path

        val fileName = UUID.randomUUID().toString() + ".jpg"
        val myCaptureFile = File(dirFile + fileName)
        val bos = BufferedOutputStream(FileOutputStream(myCaptureFile))
        bm!!.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        bos.flush()
        bos.close()

    }
}