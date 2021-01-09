package com.example.dacnce.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File

fun isNetwork(context: Context): Boolean {

    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo?.isAvailable ?: false
}

fun fileUpload(callback: Callback, path: String, name:String) {

    val file = File(path)

    val client = OkHttpClient()
    // 上传文件使用MultipartBody.Builder
    val requestBody: RequestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("username", name) // 提交普通字段
        .addFormDataPart(
            "image", file.name, RequestBody.create("image/png".toMediaTypeOrNull(), file)
        ) // 提交图片，第一个参数是键（name="第一个参数"），第二个参数是文件名，第三个是一个RequestBody
        .build()
    // POST请求
    val request: Request = Request.Builder()
        .url(NetworkUtils.POST_PIC_PATH_SERVLET)
        .post(requestBody)
        .build()
    client.newCall(request).enqueue(callback)

}

fun multipleFileUpload(callback: Callback, path: ArrayList<String>, name:String) {

    val client = OkHttpClient()
    val builder:MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
    builder.addFormDataPart("username",name)//提交普通字段
    for(i in path){
        val picFile:File = File(i)
        builder.addFormDataPart("image", picFile.name, RequestBody.create("image/png".toMediaTypeOrNull(), picFile))
    }
    val requestBody:RequestBody = builder.build()

    // POST请求
    val request: Request = Request.Builder()
        .url(NetworkUtils.PIC_ARRAY_SERVLET)
        .post(requestBody)
        .build()
    client.newCall(request).enqueue(callback)

}

fun mp4Upload(callback: Callback, picPath: String, mp4Path:String, name:String) {

    val picFile = File(picPath)
    val mp4File = File(mp4Path)

    val client = OkHttpClient()
    // 上传文件使用MultipartBody.Builder
    val requestBody: RequestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("username", name) // 提交普通字段
        .addFormDataPart(
            "image", picFile.name, RequestBody.create("image/png".toMediaTypeOrNull(), picFile)
        ) // 提交图片，第一个参数是键（name="第一个参数"），第二个参数是文件名，第三个是一个RequestBody
        .addFormDataPart("mp4",mp4File.name, RequestBody.create("video/mp4".toMediaTypeOrNull(),mp4File))
        .build()
    // POST请求
    val request: Request = Request.Builder()
        .url(NetworkUtils.MP4_SERVLET)
        .post(requestBody)
        .build()
    client.newCall(request).enqueue(callback)

}


// 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
@SuppressLint("NewApi")
fun getPathByUri4kitkat(
    context: Context,
    uri: Uri
): String? {
    val isKitKat: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        if (isExternalStorageDocument(uri)) { // ExternalStorageProvider
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }
        } else if (isDownloadsDocument(uri)) { // DownloadsProvider
            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                java.lang.Long.valueOf(id)
            )

            //Log.d("idd",id)
            return getDataColumn(context, contentUri, null, null)
        } else if (isMediaDocument(uri)) { // MediaProvider
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            when (type) {
                "image" -> {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                "video" -> {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
                "audio" -> {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
            }
            val selection = "_id=?"
            val selectionArgs =
                arrayOf(split[1])
            return getDataColumn(context, contentUri, selection, selectionArgs)
        }
    } else if ("content".equals(uri.scheme, ignoreCase = true)) { // MediaStore
        // (and
        // general)
        return getDataColumn(context, uri, null, null)
    } else if ("file".equals(uri.scheme, ignoreCase = true)) { // File
        return uri.path
    }
    return null
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 *
 * @param context
 * The context.
 * @param uri
 * The Uri to query.
 * @param selection
 * (Optional) Filter used in the query.
 * @param selectionArgs
 * (Optional) Selection arguments used in the query.
 * @return The value of the _data column, which is typically a file path.
 */
fun getDataColumn(
    context: Context,
    uri: Uri?,
    selection: String?,
    selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)
    try {
        cursor =
            context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex: Int = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(columnIndex)
        }
    } finally {
        cursor?.close()
    }
    return null
}

/**
 * @param uri
 * The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

/**
 * @param uri
 * The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

/**
 * @param uri
 * The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}