package com.example.dacnce.utils

import android.content.Context
import android.content.SharedPreferences

class SPUtils {

    companion object{

        const val SHARE_DATA = "shara_data";

        fun put(context:Context,key:String,content:Any){

            val sp: SharedPreferences = context.getSharedPreferences(SHARE_DATA,Context.MODE_PRIVATE)
            val editor = sp.edit()
            if(content is String){
                editor.putString(key, content)
            }else if(content is Int){
                editor.putInt(key, content)
            }else if(content is Boolean){
                editor.putBoolean(key,content)
            }else if(content is Float){
                editor.putFloat(key,content)
            }else if(content is Long){
                editor.putLong(key,content)
            }
            editor.apply()
        }

        /**
         * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
         *
         * @param context
         * @param key
         * @param defaultObject
         * @return
         */
        operator fun get(context: Context, key: String, defaultObject: Any): Any? {
            val sp = context.getSharedPreferences(SHARE_DATA, Context.MODE_PRIVATE)
            if (defaultObject is String) {
                return sp.getString(key, defaultObject )
            } else if (defaultObject is Int) {
                return sp.getInt(key, defaultObject)
            } else if (defaultObject is Boolean) {
                return sp.getBoolean(key, defaultObject)
            } else if (defaultObject is Float) {
                return sp.getFloat(key, defaultObject)
            } else if (defaultObject is Long) {
                return sp.getLong(key, defaultObject)
            }
            return null
        }
    }
}