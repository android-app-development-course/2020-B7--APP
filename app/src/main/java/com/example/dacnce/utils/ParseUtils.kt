package com.example.dacnce.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import cn.bmob.v3.Bmob.getCacheDir
import java.io.*


class ParseUtils {

    companion object{

        fun getFileFromUri(uri: Uri?, context: Context): File? {
            return if (uri == null) {
                null
            } else when (uri.scheme) {
                "content" -> getFileFromContentUri(uri, context)
                "file" -> File(uri.getPath())
                else -> null
            }
        }

        /**
         * Gets the corresponding path to a file from the given content:// URI
         * @param contentUri The content:// URI to find the file path from
         * @param context    Context
         * @return the file path as a string
         */
        private fun getFileFromContentUri(contentUri: Uri?, context: Context): File? {
            if (contentUri == null) {
                return null
            }
            var file: File? = null
            var filePath: String? = null
            val fileName: String
            val filePathColumn = arrayOf(
                MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.DISPLAY_NAME
            )
            val contentResolver = context.contentResolver
            val cursor: Cursor? = contentResolver.query(
                contentUri, filePathColumn, null,
                null, null
            )
            if (cursor != null) {
                cursor.moveToFirst()
                try {
                    filePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]))
                } catch (e: Exception) {
                }
                fileName = cursor.getString(cursor.getColumnIndex(filePathColumn[1]))
                cursor.close()
                if (!TextUtils.isEmpty(filePath)) {
                    file = File(filePath)
                }
                if (!file!!.exists() || file.length() <= 0 || TextUtils.isEmpty(filePath)) {
                    filePath = getPathFromInputStreamUri(context, contentUri, fileName)
                }
                if (!TextUtils.isEmpty(filePath)) {
                    file = File(filePath)
                }
            }
            return file
        }

        /**
         * 用流拷贝文件一份到自己APP目录下
         *
         * @param context
         * @param uri
         * @param fileName
         * @return
         */
        private fun getPathFromInputStreamUri(context: Context, uri: Uri, fileName: String): String? {
            var inputStream: InputStream? = null
            var filePath: String? = null
            if (uri.authority != null) {
                try {
                    inputStream = context.contentResolver.openInputStream(uri)
                    val file = createTemporalFileFrom(context, inputStream, fileName)
                    filePath = file!!.path
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                } finally {
                    try {
                        inputStream?.close()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }
            return filePath
        }

        @Throws(IOException::class)
        private fun createTemporalFileFrom(context: Context, inputStream: InputStream?, fileName: String): File? {
            var targetFile: File? = null
            if (inputStream != null) {
                var read: Int
                val buffer = ByteArray(8 * 1024)
                //自己定义拷贝文件路径
                targetFile = File(getCacheDir(), fileName)

                if (targetFile!!.exists()) {
                    targetFile.delete()
                }
                val outputStream: OutputStream = FileOutputStream(targetFile)
                while (inputStream.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }
                outputStream.flush()
                try {
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return targetFile
        }


        /**
         * 根据Uri获取文件的绝对路径，解决Android4.4以上版本Uri转换
         * @param fileUri
         */
        @TargetApi(19)
        fun getPath2uri(context: Activity?, fileUri: Uri?): String? {
            if (context == null || fileUri == null) return null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(
                    context,
                    fileUri
                )
            ) {
                if (isExternalStorageDocument(fileUri)) {
                    val docId = DocumentsContract.getDocumentId(fileUri)
                    val split =
                        docId.split(":".toRegex()).toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory()
                            .toString() + "/" + split[1]
                    }
                } else if (isDownloadsDocument(fileUri)) {
                    val id = DocumentsContract.getDocumentId(fileUri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )
                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(fileUri)) {
                    val docId = DocumentsContract.getDocumentId(fileUri)
                    val split =
                        docId.split(":".toRegex()).toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = MediaStore.Images.Media._ID + "=?"
                    val selectionArgs =
                        arrayOf(split[1])
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }
            } // MediaStore (and general)
            else if ("content".equals(fileUri.scheme, ignoreCase = true)) {
                // Return the remote address
                return if (isGooglePhotosUri(fileUri)) fileUri.lastPathSegment else getDataColumn(
                    context,
                    fileUri,
                    null,
                    null
                )
            } else if ("file".equals(fileUri.scheme, ignoreCase = true)) {
                return fileUri.path
            }
            return null
        }

        /**
         * Android4.4 （<19）以下版本获取uri地址方法
         * @param context           上下文
         * @param uri               返回的uri
         * @param selection         条件
         * @param selectionArgs     值
         * @return                  uri文件所在的路径
         */
        private fun getDataColumn(
            context: Context,
            uri: Uri?,
            selection: String?,
            selectionArgs: Array<String>?
        ): String? {
            var cursor: Cursor? = null
            val projection =
                arrayOf(MediaStore.Images.Media.DATA)
            try {
                cursor = context.contentResolver
                    .query(uri!!, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    return cursor.getString(index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }

        /**
         * @param uri  The Uri to check.
         * @return
         * URI权限是否为ExternalStorageProvider
         * Whether the Uri authority is ExternalStorageProvider.
         */
        private fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        /**
         * @param uri  The Uri to check.
         * @return
         * URI权限是否为google图片
         * Whether the Uri authority is Google Photos.
         */
        private fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority
        }

        /**
         * @param uri   The Uri to check.
         * @return
         * URI权限是否为DownloadsProvider.
         * Whether the Uri authority is DownloadsProvider.
         */
        private fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        /**
         * @param uri  The Uri to check.
         * @return
         * URI权限是否为MediaProvider.
         * Whether the Uri authority is MediaProvider.
         */
        private fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }


    }


}