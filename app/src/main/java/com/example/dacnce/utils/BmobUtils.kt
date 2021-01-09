package com.example.dacnce.utils

import android.util.Log
import android.widget.Toast
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import com.example.dacnce.DanceApplication
import com.example.dacnce.bean.User
import es.dmoral.toasty.Toasty

class BmobUtils {

    companion object{

        fun updateUserNickName(str: String){
            if(NetworkUtils.isConnected()){
                val user = BmobUser.getCurrentUser(User::class.java)
                //Log.d("onActivityResult",user.toString())
                user.user_nickname = str
                user.update(object : UpdateListener(){
                    override fun done(p0: BmobException?) {
                        if(p0 == null){
                            Log.i("onActivityResult","修改昵称成功")
                            Toasty.success(DanceApplication.context,"修改昵称成功", Toast.LENGTH_SHORT).show()
                            //SPUtils.put(DanceApplication.context,"isLogin",false)
                        }
                        else{
                            Toasty.error(DanceApplication.context,"onActivityResult", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }


        fun updateUserSex(str:String){
            if(NetworkUtils.isConnected()){
                val user = BmobUser.getCurrentUser(User::class.java)
                //Log.d("onActivityResult",user.toString())
                user.user_sex = str
                user.update(object : UpdateListener(){
                    override fun done(p0: BmobException?) {
                        if(p0 == null){
                            Log.i("onActivityResult","修改性别成功")
                            Toasty.success(DanceApplication.context,"修改性别成功", Toast.LENGTH_SHORT).show()
                            //SPUtils.put(DanceApplication.context,"isLogin",false)
                        }
                        else{
                            Toasty.error(DanceApplication.context,"onActivityResult", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }

        fun updateUserBirthday(str:String){
            if(NetworkUtils.isConnected()){
                val user = BmobUser.getCurrentUser(User::class.java)
                //Log.d("onActivityResult",user.toString())
                user.user_bd = str
                user.update(object : UpdateListener(){
                    override fun done(p0: BmobException?) {
                        if(p0 == null){
                            Log.i("onActivityResult","修改生日成功")
                            Toasty.success(DanceApplication.context,"修改生日成功", Toast.LENGTH_SHORT).show()
                            //SPUtils.put(DanceApplication.context,"isLogin",false)
                        }
                        else{
                            Toasty.error(DanceApplication.context,"onActivityResult", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }

        fun updateUserSignature(str:String){
            if(NetworkUtils.isConnected()){
                val user = BmobUser.getCurrentUser(User::class.java)
                //Log.d("onActivityResult",user.toString())
                user.user_signature = str
                user.update(object : UpdateListener(){
                    override fun done(p0: BmobException?) {
                        if(p0 == null){
                            Log.i("onActivityResult","修改个性签名成功")
                            Toasty.success(DanceApplication.context,"修改个性签名成功", Toast.LENGTH_SHORT).show()
                            //SPUtils.put(DanceApplication.context,"isLogin",false)
                        }
                        else{
                            Toasty.error(DanceApplication.context,"onActivityResult", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }

        fun updateUserImageURL(str:String){
            if(NetworkUtils.isConnected()){
                val user = BmobUser.getCurrentUser(User::class.java)
                //Log.d("onActivityResult",user.toString())
                user.image_url = str
                user.update(object : UpdateListener(){
                    override fun done(p0: BmobException?) {
                        if(p0 == null){
                            Log.i("onActivityResult","修改图片路径成功")
                            Toasty.success(DanceApplication.context,"修改图片路径成功", Toast.LENGTH_SHORT).show()
                            //SPUtils.put(DanceApplication.context,"isLogin",false)
                        }
                        else{
                            Toasty.error(DanceApplication.context,"onActivityResult", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }

}