package com.example.dacnce.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cn.bmob.v3.BmobUser
import com.bumptech.glide.Glide
import com.example.dacnce.DanceApplication
import com.example.dacnce.R
import com.example.dacnce.activity.*
import com.example.dacnce.bean.User
import com.example.dacnce.utils.NetworkUtils
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import com.example.dacnce.utils.DonwloadSaveImgVersion3
import com.example.dacnce.utils.SPUtils
import es.dmoral.toasty.Toasty

class AccountFragment : Fragment(),View.OnClickListener{

    private lateinit var llMyself:LinearLayout
    private lateinit var tvDynamic:TextView
    private lateinit var tvFollow:TextView
    private lateinit var tvFans:TextView
    private lateinit var ivSetting:ImageView
    private lateinit var ivThumbsUp:ImageView
    private lateinit var ivHistory:ImageView
    private lateinit var loginMessage:Button
    private lateinit var loginPassword:Button
    private lateinit var tvName:TextView
    private lateinit var loginOut: Button

    private lateinit var riPortrait:CircleImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        //获取线性布局实例
        initInstance(view)

        //初始化监听器
        initView()

        //初始化内容
        initContent()


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    /**
     * 获取实例
     */
    private fun initInstance(view:View){
        llMyself = view.findViewById(R.id.ll_myself)
        tvDynamic = view.findViewById(R.id.tv_dynamic)
        tvFollow = view.findViewById(R.id.tv_follow)
        tvFans = view.findViewById(R.id.tv_fans)
        ivSetting = view.findViewById(R.id.iv_setting)
        ivThumbsUp = view.findViewById(R.id.iv_thumbs_up)
        ivHistory = view.findViewById(R.id.iv_history)
        loginMessage = view.findViewById(R.id.login_message)
        loginPassword = view.findViewById(R.id.login_password)
        riPortrait=view.findViewById(R.id.ri_portrait)
        tvName = view.findViewById(R.id.tv_name)
        loginOut = view.findViewById(R.id.login_out)
    }

    /**
     * 初始化监听器
     */
    private fun initView(){
        llMyself.setOnClickListener(this)
        tvDynamic.setOnClickListener(this)
        tvFollow.setOnClickListener(this)
        tvFans.setOnClickListener(this)
        ivSetting.setOnClickListener(this)
        ivThumbsUp.setOnClickListener(this)
        ivHistory.setOnClickListener(this)
        loginMessage.setOnClickListener(this)
        loginPassword.setOnClickListener(this)
        loginOut.setOnClickListener(this)
        tvName.setOnClickListener(this)
    }

    /**
     * 初始化内容
     */
    private fun initContent(){

        if(BmobUser.getCurrentUser(User::class.java) == null ||
            SPUtils.get(DanceApplication.context,"isLogin",false) == false){
            tvName.text = "请登录".toString()
            Glide.with(this)
                .load(R.drawable.nav_icon)
                .centerCrop()
                .placeholder(R.drawable.nav_icon)
                .into(riPortrait)
            loginPassword.visibility = View.VISIBLE
            loginMessage.visibility = View.GONE
            loginOut.visibility = View.GONE

        }else{
            loginPassword.visibility = View.GONE
            loginMessage.visibility = View.GONE
            loginOut.visibility = View.VISIBLE
            if(NetworkUtils.isConnected()) {
                if(SPUtils.get(DanceApplication.context,"fans_cal",false) == true){
                    tvFans.text = SPUtils.get(DanceApplication.context,"fans_count",0).toString()
                }
                if(SPUtils.get(DanceApplication.context,"follow_cal",false) == true){
                    tvFollow.text = SPUtils.get(DanceApplication.context,"follow_count",0).toString()
                }
                if(SPUtils.get(DanceApplication.context,"dynamic_cal",false) == true){
                    tvDynamic.text = SPUtils.get(DanceApplication.context,"dynamic_count",0).toString()
                }
                val user: User = BmobUser.getCurrentUser(User::class.java)
                tvName.text = user.user_nickname.toString()
                if(user.image_url != "nav_icon"){
                    val suffix = user.image_url!!.substring(user.image_url!!.lastIndexOf("/")+1)
                    //Log.d("suffix",suffix)
//                val file = File(Environment.getExternalStorageDirectory()
//                    .path + "/DCIM/Camera/" + suffix)
                    val file = File(requireContext().getExternalFilesDir(null)!!.path +"/"+ suffix)
                    //Log.d("suffix3",file.toString())
                    if(!file.exists()){
                        //DonwloadSaveImgVersion3.donwloadImg(context,NetworkUtils.PIC_PRE_PATH + "/images" + user.image_url)
                        Glide.with(this@AccountFragment)
                            .load(file)
                            .centerCrop()
                            .placeholder(R.drawable.nav_icon)
                            .into(riPortrait)
                    }else{
                        Glide.with(this@AccountFragment)
                            .load(file)
                            .centerCrop()
                            .placeholder(R.drawable.nav_icon)
                            .into(riPortrait)
                    }

                }
            }else{
                Glide.with(this@AccountFragment)
                    .load(R.drawable.nav_icon)
                    .centerCrop()
                    .placeholder(R.drawable.nav_icon)
                    .into(riPortrait)
            }
        }
    }

    /**
     * 点击事件响应
     */
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ll_myself -> {
                if(BmobUser.getCurrentUser(User::class.java) == null||
                    SPUtils.get(DanceApplication.context,"isLogin",false) == false ){
                    Toasty.info(DanceApplication.context,"请登录",Toast.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(context, PersonInfoActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.tv_dynamic -> {
                if(BmobUser.getCurrentUser(User::class.java) == null||
                    SPUtils.get(DanceApplication.context,"isLogin",false) == false ){
                    Toasty.info(DanceApplication.context,"请登录",Toast.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(context, DynamicActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.tv_follow -> {
                if(BmobUser.getCurrentUser(User::class.java) == null||
                    SPUtils.get(DanceApplication.context,"isLogin",false) == false ){
                    Toasty.info(DanceApplication.context,"请登录",Toast.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(context, FollowActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.tv_fans -> {
                if(BmobUser.getCurrentUser(User::class.java) == null||
                    SPUtils.get(DanceApplication.context,"isLogin",false) == false ){
                    Toasty.info(DanceApplication.context,"请登录",Toast.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(context, FansActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.iv_setting -> {
                val intent = Intent(context, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.iv_thumbs_up -> {
                if(BmobUser.getCurrentUser(User::class.java) == null||
                    SPUtils.get(DanceApplication.context,"isLogin",false) == false ){
                    Toasty.info(DanceApplication.context,"请登录",Toast.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(context, ThumbsActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.iv_history -> {
                val intent = Intent(context, HistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.login_message -> {
                val intent = Intent(context, LoginActivity::class.java)
                intent.putExtra("choice",0)
                startActivity(intent)

            }
            R.id.login_password -> {
                val intent = Intent(context, LoginActivity::class.java)
                intent.putExtra("choice",1)
                startActivity(intent)
                activity?.finish()
            }
            R.id.login_out -> {
                AlertDialog
                    .Builder(context)
                    .setTitle("退出")
                    .setMessage("确认退出吗")
                    .setPositiveButton("确定",DialogInterface.OnClickListener{ dialog, which ->
                        BmobUser.logOut()
                        SPUtils.put(DanceApplication.context,"isLogin",false)
                        Toasty.info(DanceApplication.context,"退出成功",Toast.LENGTH_SHORT).show()
                        initContent()
                    })
                    .setNegativeButton("取消",null)
                    .setCancelable(true)
                    .create()
                    .show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //Log.d("onStart","AccoutFragment onStart")
        //从以前编辑过的图片中设置头像，若不存在则加载默认头像
        initContent()

        if(NetworkUtils.isConnected()) {
            if(BmobUser.getCurrentUser(User::class.java) != null){
                val user: User = BmobUser.getCurrentUser(User::class.java)
                if(user.image_url != "nav_icon"){
                    val suffix = user.image_url!!.substring(user.image_url!!.lastIndexOf("/")+1)
                    //Log.d("suffix",suffix)
                    val file = File(requireContext().getExternalFilesDir(null)!!.path +"/"+ suffix)
                    if(file.exists()){
                        Glide.with(this@AccountFragment)
                            .load(file)
                            .centerCrop()
                            .into(riPortrait)
                    }else{
                        Glide.with(this)
                            .load(NetworkUtils.PIC_PRE_PATH + "/images" + user.image_url)
                            .centerCrop()
                            .placeholder(R.drawable.nav_icon)
                            .into(riPortrait)
                    }
                }
            }
        }else{
            Glide.with(this@AccountFragment)
                .load(R.drawable.nav_icon)
                .centerCrop()
                .into(riPortrait)
        }
    }

    override fun onPause() {
        super.onPause()
        //Log.d("AccountFragment","onPause")
    }

    override fun onResume() {
        super.onResume()
        //Log.d("AccountFragment","onResume")
        onStart()
    }

}