package com.example.dacnce.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import com.bumptech.glide.Glide
import com.example.dacnce.DanceApplication
import com.example.dacnce.R
import com.example.dacnce.adapter.DynamicAdapter
import com.example.dacnce.bean.DynamicListItem
import com.example.dacnce.bean.PictureItem
import com.example.dacnce.bean.PostModel
import com.example.dacnce.bean.User
import com.example.dacnce.utils.NetworkUtils
import com.example.dacnce.utils.RecyclerItemClickListener
import com.example.dacnce.utils.SPUtils
import com.example.dacnce.utils.isNetwork
import com.mingle.entity.MenuEntity
import com.mingle.sweetpick.DimEffect
import com.mingle.sweetpick.SweetSheet
import com.mingle.sweetpick.ViewPagerDelegate
import kotlinx.android.synthetic.main.activity_person_dynamic.*
import kotlin.collections.ArrayList

class PersonDynamicActivity : AppCompatActivity() {

    private lateinit var mySweetSheet: SweetSheet
    private lateinit var dynamicAdapter: DynamicAdapter
    private val dynamicListItemList = ArrayList<DynamicListItem>()
    private var count: Int = 0
    private var userObjectId = ""

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_dynamic)

        //初始化Toolbar
        setSupportActionBar(toolbarDynamic)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        collapsingToolbar.title = "他的动态"
        Glide.with(this).load(R.drawable.example3).into(dynamic_title_image)
        //设置颜色
        collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.black0))
        collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))

        userObjectId = intent.getStringExtra("userObjectId").toString()
        //Log.d("userObjectId",userObjectId)

        if (isNetwork(this)) {

            if(userObjectId != "" && userObjectId!= "null"){

                //Log.d("userObjectId",userObjectId)
                //用户信息
                queryUser()

                /*读取数据库的信息*/
                queryObjects()

                //关注,粉丝信息
                queryFollow()

                queryFans()
            }
        } else {
            /*没有网络读不了*/
            AlertDialog.Builder(this)
                .setTitle("无法连接网络！")
                .setPositiveButton(
                    "确定",
                    DialogInterface.OnClickListener { _, _ ->
                        finish()
                    }
                ).setCancelable(true)
                .create()
                .show()
        }
        setupRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bar_dynamic_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //toolBar点击返回按钮，销毁单前activity
                finish()
            }
            R.id.share -> {
                if (mySweetSheet.isShow) {
                    mySweetSheet.dismiss()
                } else {
                    setupRecyclerView()
                    mySweetSheet.show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        val list = ArrayList<MenuEntity>()

        var menuEntity = MenuEntity()
        menuEntity.iconId = R.drawable.ic_share_wechat
        menuEntity.title = "微信好友"
        list.add(menuEntity)

        menuEntity = MenuEntity()
        menuEntity.iconId = R.drawable.ic_share_pyq
        menuEntity.title = "朋友圈"
        list.add(menuEntity)

        menuEntity = MenuEntity()
        menuEntity.iconId = R.drawable.ic_share_qqzone
        menuEntity.title = "QQ空间"
        list.add(menuEntity)

        menuEntity = MenuEntity()
        menuEntity.iconId = R.drawable.ic_share_weibo
        menuEntity.title = "微博"
        list.add(menuEntity)

        menuEntity = MenuEntity()
        menuEntity.iconId = R.drawable.ic_qq_friend
        menuEntity.title = "QQ好友"
        list.add(menuEntity)

        menuEntity = MenuEntity()
        menuEntity.iconId = R.drawable.ic_computer
        menuEntity.title = "我的电脑"
        list.add(menuEntity)

        mySweetSheet = SweetSheet(dy_rl)
        mySweetSheet.setMenuList(list)
        mySweetSheet.setDelegate(ViewPagerDelegate())
        mySweetSheet.setBackgroundEffect(DimEffect(1f))
    }

    private fun queryUser(){
        val bmobQuery: BmobQuery<User> = BmobQuery<User>()
        bmobQuery.getObject(userObjectId,object :QueryListener<User>(){
            override fun done(p0: User?, p1: BmobException?) {
                if(p1 == null){
                    if(p0 != null){
                        runOnUiThread {
                            Glide.with(this@PersonDynamicActivity).load(NetworkUtils.PIC_PRE_PATH + "/images" + p0.image_url).placeholder(R.drawable.nav_icon).into(user_image)
                            user_name.text = p0.user_nickname
                            collapsingToolbar.title = "${p0.user_nickname}的动态"
                        }
                    }
                }else{
                    Toast.makeText(this@PersonDynamicActivity,p1.message,Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun queryObjects() {
        val userNow = User(null,null,null,null,null,null,null,null,null)
        userNow.objectId = userObjectId
        val bmobQuery: BmobQuery<PostModel> = BmobQuery()
        bmobQuery.addWhereEqualTo("user",userNow)
            .order("-updatedAt")
            .include("user")
            .setLimit(10)
        bmobQuery.findObjects(object : FindListener<PostModel>() {
            override fun done(posts: MutableList<PostModel>?, ex: BmobException?) {
                if (ex == null) {
                    if (posts != null) {

                        runOnUiThread {

                            dealQueryData(posts)

                            val layoutManager = LinearLayoutManager(this@PersonDynamicActivity)
                            main_rv.layoutManager = layoutManager
                            main_rv.itemAnimator = DefaultItemAnimator()

                            dynamicAdapter = DynamicAdapter(this@PersonDynamicActivity, dynamicListItemList)
                            main_rv.adapter = dynamicAdapter

//                            main_rv.addOnItemTouchListener(
//                                RecyclerItemClickListener(this@PersonDynamicActivity, main_rv,
//                                    object : RecyclerItemClickListener.OnItemClickListener {
//                                        override fun onItemClick(view: View, position: Int) {
//                                            Toast.makeText(
//                                                applicationContext,
//                                                "click Recyclerview$position",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                        }
//                                        override fun onItemLongClick(view: View?, position: Int) {
//
//                                        }
//                                    })
//                            )
                        }

                    }
                } else {
                    Toast.makeText(this@PersonDynamicActivity, ex.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun dealQueryData(list: MutableList<PostModel>) {
        count = list.size
        SPUtils.put(DanceApplication.context,"dynamic_cal",true)
        SPUtils.put(DanceApplication.context,"dynamic_count",count)
        for (item: PostModel in list) {
            var isVideo=false
            var videoPath="NoPath"
            if(item.pic_or_mov=="mov") {
                isVideo=true
                videoPath=item.post_mov_path
            }
            val picItemList=ArrayList<PictureItem>()
            val picPaths=item.post_pic_path.split(",")
            if(picPaths.isNotEmpty()){
                for(i in 0 until picPaths.size - 1 ){

                    picItemList.add(PictureItem(picPaths[i],isVideo,videoPath))
                }
            }

            val model = DynamicListItem(
                item.objectId,
                picItemList,
                videoPath,
                //用户昵称
                //item.post_user_id,
                item.user.user_nickname!!,
                //图片
                //R.drawable.example9,
                item.user.image_url!!,
                isVideo,
                null,
                item.post_content,
                item.user
            )
            this.dynamicListItemList.add(model)
        }
    }

    private fun queryFollow(){

    }

    private fun queryFans(){

    }

}