package com.example.dacnce.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.dacnce.R
import com.example.dacnce.activity.*
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

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
    }

    /**
     * 点击事件响应
     */
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ll_myself -> {
                val intent = Intent(context, PersonInfoActivity::class.java)
                startActivity(intent)
            }
            R.id.tv_dynamic -> {
                val intent = Intent(context, DynamicActivity::class.java)
                startActivity(intent)
            }
            R.id.tv_follow -> {
                val intent = Intent(context, FollowActivity::class.java)
                startActivity(intent)
            }
            R.id.tv_fans -> {
                val intent = Intent(context, FansActivity::class.java)
                startActivity(intent)
            }
            R.id.iv_setting -> {
                val intent = Intent(context, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.iv_thumbs_up -> {
                val intent = Intent(context, ThumbsActivity::class.java)
                startActivity(intent)
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
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //从以前编辑过的图片中设置头像，若不存在则加载默认头像
        val ImageFile = File(activity?.getExternalFilesDir(null), "crop_image.jpg")
        if (ImageFile.exists()) {
            try {
                val bm = BitmapFactory.decodeFile(ImageFile.getPath())
                riPortrait.setImageBitmap(bm)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            riPortrait.setImageDrawable(resources.getDrawable(R.drawable.nav_icon))
            //Toast.makeText(activity, "crop_image.jpg 为空！", Toast.LENGTH_SHORT).show()
        }
    }
}