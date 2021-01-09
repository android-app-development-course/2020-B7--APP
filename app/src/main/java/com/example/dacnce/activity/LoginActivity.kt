package com.example.dacnce.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.example.dacnce.DanceApplication
import com.example.dacnce.MainActivity
import com.example.dacnce.R
import com.example.dacnce.utils.NetworkUtils
import com.example.dacnce.utils.SPUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_loginbypassword.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private var choice: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        choice = intent.getIntExtra("choice",0)
        setContentView(R.layout.activity_loginbypassword)


        login_commit_btn.setOnClickListener(this)
        register_btn.setOnClickListener(this)
        login_pass_btn.setOnClickListener(this)

        if(SPUtils.get(DanceApplication.context,"remember_login",false) == true){
            login_input_phone.setText(SPUtils.get(DanceApplication.context,"account","").toString())
            login_input_code.setText(SPUtils.get(DanceApplication.context,"password","").toString())
            login_remember.isChecked = true
        }else{
            login_input_phone.setText(SPUtils.get(DanceApplication.context,"account","").toString())
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.login_commit_btn -> {
                if(NetworkUtils.isConnected()){
                    if(TextUtils.isEmpty(login_input_phone.text.toString())||
                        TextUtils.isEmpty(login_input_code.text.toString())){
                        Toasty.info(this@LoginActivity,"请输入账号和密码", Toast.LENGTH_SHORT).show()
                        return
                    }
                    //Log.d("LoginActivity",login_input_phone.text.toString())
                    //Log.d("LoginActivity",login_input_code.text.toString())
                    val user: BmobUser = BmobUser()
                    user.username = login_input_phone.text.toString()
                    user.setPassword(login_input_code.text.toString())
                    user.login(object: SaveListener<BmobUser>(){
                        override fun done(p0: BmobUser?, p1: BmobException?) {
                            if(p1 == null){
                                //Log.d("LoginActivity","click login4")
                                Toasty.success(this@LoginActivity,"登录成功",Toast.LENGTH_SHORT).show()
                                val intent = Intent(DanceApplication.context, MainActivity::class.java)
                                startActivity(intent)
                                //记住密码
                                if(login_remember.isChecked){
                                    SPUtils.put(DanceApplication.context,"remember_login",true)
                                }else{
                                    SPUtils.put(DanceApplication.context,"remember_login",false)
                                }
                                SPUtils.put(DanceApplication.context,"isLogin",true)
                                SPUtils.put(DanceApplication.context,"account",login_input_phone.text.toString())
                                SPUtils.put(DanceApplication.context,"password",login_input_code.text.toString())
                                finish();

                            }else{
                                //Log.d("LoginActivity","click login5")
                                login_input_code.setText("")
                                runOnUiThread(Runnable {
                                    Toasty.error(this@LoginActivity, "账号或密码不正确", Toast.LENGTH_SHORT, true).show();
                                })
                                p1.printStackTrace()
                            }
                        }
                    })
                }else{
                    Toasty.error(DanceApplication.context, "无网络连接", Toast.LENGTH_SHORT, true).show();
                }
            }
            R.id.register_btn -> {
                val intent = Intent(DanceApplication.context, RegisterActivity::class.java)
                startActivity(intent)
            }

            R.id.login_pass_btn -> {
                val intent = Intent(DanceApplication.context, MainActivity::class.java)
                startActivity(intent)
                //记住密码
                SPUtils.put(DanceApplication.context,"isLogin",false)
                SPUtils.put(DanceApplication.context,"account","")
                SPUtils.put(DanceApplication.context,"password","")
                finish()
            }

        }
    }
}