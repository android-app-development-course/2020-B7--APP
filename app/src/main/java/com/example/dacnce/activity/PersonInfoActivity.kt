package com.example.dacnce.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import com.bumptech.glide.Glide
import com.example.dacnce.R
import com.example.dacnce.bean.User
import com.example.dacnce.utils.*
import kotlinx.android.synthetic.main.activity_person_info.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import pub.devrel.easypermissions.EasyPermissions
import java.io.*
import java.util.*
import kotlin.concurrent.thread

class PersonInfoActivity : BaseActivity(),View.OnClickListener {

    private var choiceSex = -1
    private val c: Calendar = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)

    private var mSaveDialog: ProgressDialog? = null

    private val messageHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            mSaveDialog!!.dismiss()
            //Toasty.success(this@PersonInfoActivity,"上传图片成功",Toast.LENGTH_SHORT).show()
        }
    }

    /*相机和相册权限相关的函数*/
    private val permissions = arrayOf<String>(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    //获取权限
    private fun getPermission() {
        if (EasyPermissions.hasPermissions(this, *permissions)) {
            //已经打开权限
            //Toast.makeText(this, "已经申请相关权限", Toast.LENGTH_SHORT).show()
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的相册、照相使用权限", 1, *permissions)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //框架要求必须这么写
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    //成功打开权限
    fun onPermissionsGranted(requestCode: Int, perms: List<String?>) {
        Toast.makeText(this, "相关权限获取成功", Toast.LENGTH_SHORT).show()
    }

    //用户未同意权限
    fun onPermissionsDenied(requestCode: Int, perms: List<String?>) {
        Toast.makeText(this, "请同意相关权限，否则功能无法使用", Toast.LENGTH_SHORT).show()
    }

    companion object{
        const val GET_BACKGROUND_FROM_CAPTURE_RESOULT = 1
        const val RESULT_REQUEST_CODE = 2
        const val TAKE_PHOTO = 3
        const val INTENT_NICKNAME = 4
        const val INTENT_SIGNATURE = 5
        val MEDIA_TYPE_PNG: MediaType? = "image/png".toMediaTypeOrNull()
    }
    private var photoUri //相机拍照返回图片路径
            : Uri? = null
    private var outputImage: File? = null

    //选择相机
    private fun selectCamera() {
        //创建file对象，用于存储拍照后的图片，这也是拍照成功后的照片路径
        outputImage = File(this.externalCacheDir, "camera_photos.jpg")
        if(outputImage!=null){
            //Log.d("PersonInfoActivity", outputImage!!.absolutePath)
            //:  /storage/emulated/0/Android/data/com.example.dacnce/cache/camera_photos.jpg
        }
        try {
            //判断文件是否存在，存在删除，不存在创建
            if (outputImage!!.exists()) {
                outputImage!!.delete()
            }
            outputImage!!.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        photoUri = Uri.fromFile(outputImage)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, TAKE_PHOTO)
    }

    val STR_IMAGE = "image/*"

    //选择相册
    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK, null)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STR_IMAGE)
        startActivityForResult(intent, GET_BACKGROUND_FROM_CAPTURE_RESOULT)
    }

    private var cropImgUri: Uri? = null

    //裁剪图片
    private fun cropRawPhoto(uri: Uri?) {
        //创建file文件，用于存储剪裁后的照片
        val cropImage = File(getExternalFilesDir(null), "crop_image.jpg")
        //:  /storage/emulated/0/Android/data/com.example.dacnce/files/crop_image.jpg
        //Log.d("PersonInfoActivity",cropImage.absolutePath)
        val path = cropImage.absolutePath
        try {
            if (cropImage.exists()) {
                cropImage.delete()
            }
            cropImage.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        cropImgUri = Uri.fromFile(cropImage)
        val intent = Intent("com.android.camera.action.CROP")
        //设置源地址uri
        intent.setDataAndType(photoUri, "image/*")
        intent.putExtra("crop", "true")
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        intent.putExtra("outputX", 200)
        intent.putExtra("outputY", 200)
        intent.putExtra("scale", true)
        //设置目的地址uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImgUri)
        //设置图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("return-data", false)
        intent.putExtra("noFaceDetection", true) // no face detection
        startActivityForResult(intent, RESULT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            GET_BACKGROUND_FROM_CAPTURE_RESOULT -> {
                photoUri = data?.data
                cropRawPhoto(photoUri)
            }
            TAKE_PHOTO -> {
                cropRawPhoto(photoUri)
            }
            RESULT_REQUEST_CODE -> {
                if (cropImgUri != null) {
                    val headImage = BitmapFactory.decodeStream(
                        this.contentResolver.openInputStream(cropImgUri!!)
                    )
                    //rv_edit_head.setImageBitmap(headImage)
                    Glide.with(this).load(headImage).into(rv_edit_head)
                    //getFile(headImage) //把Bitmap转成File
                    //UpLoadIcon(getFile(headImage));   //上传图片文件到服务器
                    //Log.d("onActivityResult",photoUri.toString())
                    val str = ParseUtils.getPath2uri(this@PersonInfoActivity,photoUri)
                    //: /storage/emulated/0/Android/data/com.example.dacnce/cache/camera_photos.jpg
//                    if(str!= null){
//                        runOnUiThread {
//                            mSaveDialog = ProgressDialog.show(
//                                this@PersonInfoActivity,
//                                "上传图片",
//                                "图片正在上传，请稍等...",
//                                true
//                            )
//                        }
//
//                        fileUpload(object : Callback {
//                            override fun onFailure(call: Call, e: IOException) {
//                                Toast.makeText(this@PersonInfoActivity, "上传失败！", Toast.LENGTH_SHORT).show()
//                                e.printStackTrace()
//                            }
//                            override fun onResponse(call: Call, response: Response) {
//                                Toast.makeText(this@PersonInfoActivity, "上传成功！", Toast.LENGTH_SHORT).show()
//                                val message = response.body()?.string()
//                                if(message != null){
//                                    Log.d("onActivityResult",message)
//                                    BmobUtils.updateUserImageURL(message)
//                                }
//                                Thread.sleep(1000)
//                                messageHandler.sendMessage(messageHandler.obtainMessage())
//                            }
//                        },str)

//                    }
                    thread {
                        if(str!=null){
                            //Log.d("pathhh",str)
                            runOnUiThread {
                                mSaveDialog = ProgressDialog.show(
                                    this@PersonInfoActivity,
                                    "上传图片",
                                    "图片正在上传，请稍等...",
                                    true
                                )
                            }

                            fileUpload(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    runOnUiThread {
                                        Toast.makeText(this@PersonInfoActivity, "上传失败！", Toast.LENGTH_SHORT).show()
                                        e.printStackTrace()
                                        messageHandler.sendMessage(messageHandler.obtainMessage())
                                    }
                                }

                                override fun onResponse(call: Call, response: Response) {
                                    runOnUiThread {
                                        Toast.makeText(this@PersonInfoActivity, "上传成功！", Toast.LENGTH_SHORT).show()
                                        val message = response.body?.string()
                                        if(message != null){
                                            //Log.d("onActivityResult",message)
                                            BmobUtils.updateUserImageURL(message)
                                        }
                                        messageHandler.sendMessage(messageHandler.obtainMessage())
                                    }
                                }
                            },str)
                        }
                    }

                } else {
                    Toast.makeText(this, "cropImgUri为空！", Toast.LENGTH_SHORT).show()
                }
            }
            INTENT_NICKNAME -> {
                val newNickName = data?.getStringExtra("changeNickName")
                //Log.d("onActivityResult",newNickName.toString())
                if(newNickName!= null){
                    tv_name.text = newNickName
                    BmobUtils.updateUserNickName(newNickName)
                }
            }
            INTENT_SIGNATURE -> {
                val newSignature = data?.getStringExtra("changeSignature")
                //Log.d("onActivityResult",newSignature.toString())
                if(newSignature != null){
                    tv_signature.text = newSignature
                    BmobUtils.updateUserSignature(newSignature)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //把bitmap转成file
    private fun getFile(bitmap: Bitmap): File? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val file =
            File(Environment.getExternalStorageDirectory().toString() + "/temp.jpg")
        try {
            file.createNewFile()
            val fos = FileOutputStream(file)
            val `is`: InputStream = ByteArrayInputStream(baos.toByteArray())
            var x = 0
            val b = ByteArray(1024 * 100)
            while (`is`.read(b).also({ x = it }) != -1) {
                fos.write(b, 0, x)
            }
            fos.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return file
    }

    private fun showListDialog() {
        val items = arrayOf("拍照", "从相册中选择")
        val listDialog =
            AlertDialog.Builder(this)
        listDialog.setTitle("更改头像")
        listDialog.setItems(items) { dialog, which -> // which 下标从0开始
            // ...To-do
            when (which) {
                0 -> selectCamera()
                1 -> selectPhoto()
            }
        }
        listDialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_info)

        //初始化Toolbar
        setSupportActionBar(toolbarPeoInfo)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "编辑信息"

        getPermission()

        //获取权限
        initPermission()

        //设置监听器
        initView()

        //初始化内容
        initContent()

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //保存按钮
        //menuInflater.inflate(R.menu.bar_title_with_save,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                //toolBar点击返回按钮，销毁单前activity
                finish()
            }
            R.id.toolbarSave -> {
                //数据库操作
                Toast.makeText(this,"保存数据",Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //设置监听事件
    private fun initView(){
        ll_name.setOnClickListener(this)
        ll_sex.setOnClickListener(this)
        ll_birthday.setOnClickListener(this)
        ll_signature.setOnClickListener(this)
        rv_edit_head.setOnClickListener(this)
    }

    private fun initContent(){
        //登录情况下，才会能够进入这页面
        if(NetworkUtils.isConnected()){
            val user:User = BmobUser.getCurrentUser(User::class.java)
            if(user!=null){
                val bmobQuery:BmobQuery<User> = BmobQuery<User>()
                bmobQuery.getObject(user.objectId,object:QueryListener<User>(){
                    override fun done(p0: User, p1: BmobException?) {
                        if(p1 == null && p0 != null){
                            tv_id.text = p0.username
                            tv_name.text = p0.user_nickname
                            tv_sex.text = p0.user_sex
                            tv_birthday.text = p0.user_bd
                            tv_signature.text = p0.user_signature
                            //Log.d("PersonInfoActivity",NetworkUtils.PIC_PRE_PATH + "/images" + p0.image_url)
                            Glide.with(this@PersonInfoActivity)
                                .load(NetworkUtils.PIC_PRE_PATH + "/images" + p0.image_url)
                                .centerCrop()
                                .placeholder(R.drawable.nav_icon)
                                .into(rv_edit_head)
                        }
                    }
                })
            }
        }
    }


    //获取权限
    private fun initPermission(){

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ll_name -> {
                //编辑昵称的LinearLayout的点击事件
                val intent = Intent(this,EditNameActivity::class.java)
                //应该带 user.id ,user.name过去，显示已有的名字，在此基础修改
                intent.putExtra("nickName",tv_name.text.toString())
                startActivityForResult(intent, INTENT_NICKNAME);
            }
            R.id.ll_sex -> {
                val sexListItem = arrayOf("男","女","保密")
                AlertDialog.Builder(this)
                    .setTitle("你的性别是")
                    .setSingleChoiceItems(sexListItem,-1){ dialog: DialogInterface?, which: Int ->
                        choiceSex = which
                    }.setPositiveButton("确定",DialogInterface.OnClickListener { dialog, which ->
                        if(choiceSex!= -1)tv_sex.text = sexListItem[choiceSex]
                        BmobUtils.updateUserSex(sexListItem[choiceSex])
                    })
                    .setNegativeButton("取消",null)
                    .setCancelable(true) //可以通过回退按钮退出关闭Dialog
                    .create()
                    .show()
            }
            R.id.ll_birthday -> {
                DatePickerDialog(this,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    tv_birthday.text = ("" + year + "年" + (month+1) + "月" + dayOfMonth + "日")
                    BmobUtils.updateUserBirthday("" + year + "年" + (month+1) + "月" + dayOfMonth + "日")
                },year,month,day).show()
            }
            R.id.ll_signature -> {
                val intent = Intent(this,SignatureActivity::class.java)
                intent.putExtra("signature",tv_signature.text.toString())
                startActivityForResult(intent, INTENT_SIGNATURE);
            }
            R.id.rv_edit_head->{
                showListDialog()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //从数据库读取数据
        //从以前编辑过的图片中设置头像，若不存在则加载默认头像
//        val ImageFile = File(getExternalFilesDir(null), "crop_image.jpg")
//        if (ImageFile.exists()) {
//            try {
//                val bm = BitmapFactory.decodeFile(ImageFile.getPath())
//                rv_edit_head.setImageBitmap(bm)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        } else {
//            rv_edit_head.setImageDrawable(resources.getDrawable(R.drawable.nav_icon))
//            //Toast.makeText(this, "crop_image.jpg 为空！", Toast.LENGTH_SHORT).show()
//        }

    }


    private fun fileUpload(callback: Callback, path:String){
        val file = File(path)
        //Log.d("fileUpload",path)
        val client = OkHttpClient()
        val requestBody:RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username","test")
            .addFormDataPart("image",file.name, RequestBody.create(MEDIA_TYPE_PNG,file))
            .build()
        //POST请求
        val request = Request.Builder()
            .url(NetworkUtils.SERVLET_PRE_PATH+"/Dservlet")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(callback)
    }
}

