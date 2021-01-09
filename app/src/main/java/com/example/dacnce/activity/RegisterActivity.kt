package com.example.dacnce.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import cn.bmob.v3.datatype.BmobRelation
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.example.dacnce.R
import com.example.dacnce.bean.User
import com.example.dacnce.bean.UserData
import com.example.dacnce.utils.NetworkUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity(),View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        login_by_message_return.setOnClickListener(this)
        register_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.login_by_message_return -> {
                finish()
            }
            R.id.register_btn -> {
                val userName = login_input_phone.text.toString()
                val password = login_input_code.text.toString()

                //判断网络情况
                if(NetworkUtils.isConnected()){
                    if(TextUtils.isEmpty(userName)||
                        TextUtils.isEmpty(password)){
                        Toasty.info(this,"请输入用户名或密码", Toast.LENGTH_SHORT).show()
                        return
                    }

                    if(login_input_phone.length() < 4){
                        Toasty.info(this,"用户名不能低于4位",Toast.LENGTH_SHORT).show()
                        return
                    }
                    if(login_input_code.length() < 6){
                        Toasty.info(this,"密码不能低于4位",Toast.LENGTH_SHORT).show()
                        return
                    }

                    val user = User(
                        0,
                        "街舞爱好者",
                        "保密",
                        "2000年1月1日",
                        "这人什么都没有留下",
                        "",
                        "nav_icon",
                        BmobRelation(),
                        BmobRelation()
                    )
                    user.username = userName
                    user.setPassword(password)
                    //注册用户
                    user.signUp(object : SaveListener<User>(){
                        override fun done(userInsert: User?, ex: BmobException?) {
                            if(ex == null){
                                if(userInsert!=null){
                                    //新建用户数据表
                                    val userData = UserData(userInsert, userInsert.objectId,"","","","")
                                    userData.save(object :SaveListener<String>(){
                                        override fun done(p0: String?, exception: BmobException?) {
                                            if(exception == null){
                                                Toasty.success(this@RegisterActivity,"注册成功",Toast.LENGTH_SHORT).show()
                                                val intent = Intent(this@RegisterActivity,LoginActivity::class.java)
                                                startActivity(intent)
                                                finish()
                                            }else{
                                                Toasty.error(this@RegisterActivity, "注册失败", Toast.LENGTH_SHORT, true).show();
                                                exception.message?.let { Log.i("register", it) }
                                            }
                                        }
                                    })
                                }
                            }else{
                                Toasty.error(this@RegisterActivity, "注册失败", Toast.LENGTH_SHORT, true).show();
                                ex.message?.let { Log.i("register", it) }
                            }
                        }
                    })
                } else {
                    Toasty.error(this@RegisterActivity, "无网络连接", Toast.LENGTH_SHORT, true).show();
                }
            }
        }
    }
}