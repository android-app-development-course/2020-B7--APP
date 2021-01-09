package com.example.dacnce.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
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
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
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
import kotlinx.android.synthetic.main.activity_dynamic.*

class DynamicActivity : AppCompatActivity() {

    private lateinit var mySweetSheet: SweetSheet
    private lateinit var dynamicAdapter: DynamicAdapter
    private val dynamicListItemList = ArrayList<DynamicListItem>()
    private lateinit var user:User
    private var count: Int = 0

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic)

        //初始化Toolbar
        setSupportActionBar(toolbarDynamic)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        collapsingToolbar.title = "我的动态"
        Glide.with(this).load(R.drawable.example3).into(dynamic_title_image)
        //设置颜色
        collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.black0))
        collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))

        if (isNetwork(this)) {

            user = BmobUser.getCurrentUser(User::class.java)
            Glide.with(this).load(NetworkUtils.PIC_PRE_PATH + "/images" + user.image_url).placeholder(R.drawable.nav_icon).into(user_image)
            user_name.text = user.user_nickname

            /*读取数据库的信息*/
            queryObjects()

            //calculateFollows()

            //calculateFans()

            if(SPUtils.get(DanceApplication.context,"fans_cal",false) == true){
                user_fans.text = SPUtils.get(DanceApplication.context,"fans_count",0).toString() + "关注者"
            }
            if(SPUtils.get(DanceApplication.context,"follow_cal",false) == true){
                user_follow.text = SPUtils.get(DanceApplication.context,"follow_count",0).toString() + "正在关注"
            }

        } else {
            /*没有网络读不了*/
            AlertDialog.Builder(this)
                .setTitle("无法连接网络！")
                .setPositiveButton(
                    "确定",
                    DialogInterface.OnClickListener { _, _ ->
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


    private fun queryObjects() {
        user = BmobUser.getCurrentUser(User::class.java)
        val bmobQuery: BmobQuery<PostModel> = BmobQuery()
        bmobQuery.addWhereEqualTo("user",user)
            .order("-updatedAt")
            .include("user")
            .setLimit(10)
        bmobQuery.findObjects(object : FindListener<PostModel>() {
            override fun done(posts: MutableList<PostModel>?, ex: BmobException?) {
                if (ex == null) {
                    if (posts != null) {

                        runOnUiThread {

                            dealQueryData(posts)

                            val layoutManager = LinearLayoutManager(this@DynamicActivity)
                            main_rv.layoutManager = layoutManager
                            main_rv.itemAnimator = DefaultItemAnimator()

                            dynamicAdapter = DynamicAdapter(this@DynamicActivity, dynamicListItemList)
                            main_rv.adapter = dynamicAdapter

                            /*main_rv.addOnItemTouchListener(
                                RecyclerItemClickListener(this@DynamicActivity, main_rv,
                                    object : RecyclerItemClickListener.OnItemClickListener {
                                        override fun onItemClick(view: View, position: Int) {
                                            Toast.makeText(
                                                applicationContext,
                                                "click Recyclerview$position",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            val list:ArrayList<String> = ArrayList()
                                            for (i : PictureItem in dynamicListItemList[position].imageList!!){
                                                list.add(i.image)
                                            }


                                            statPhotoViewActivity(position,list)
                                        }
                                        override fun onItemLongClick(view: View?, position: Int) {

                                        }
                                    })
                            )*/
                        }

                    }
                } else {
                    Toast.makeText(this@DynamicActivity, ex.message, Toast.LENGTH_LONG).show()
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

    private fun calculateFollows(){
        if(NetworkUtils.isConnected()){
            if(BmobUser.getCurrentUser(User::class.java) != null &&
                SPUtils.get(DanceApplication.context,"isLogin",false) == true){
                var count = 0
                val userQuery: BmobQuery<User> = BmobQuery<User>()
                userQuery.addWhereRelatedTo("follows", BmobPointer(BmobUser.getCurrentUser(User::class.java)))
                userQuery.findObjects(object : FindListener<User>() {
                    override fun done(p0: MutableList<User>?, p1: BmobException?) {
                        if(p1 == null){
                            if(p0!=null){
//                                    count = p0.size
//                                    SPUtils.put(DanceApplication.context,"follow_cal",true)
//                                    SPUtils.put(DanceApplication.context,"follow_count",count)
                                user_follow.text = p0.size.toString() + "正在关注"
                                runOnUiThread{
                                    //Log.d("DynamicActivity",p0.size.toString() + "正在关注")
                                    user_follow.text = p0.size.toString() + "正在关注"
                                }
                            }
                        }else{
                            Toast.makeText(this@DynamicActivity,p1.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }

    private fun calculateFans(){
        if(NetworkUtils.isConnected()){
            if(BmobUser.getCurrentUser(User::class.java) != null &&
                SPUtils.get(DanceApplication.context,"isLogin",false) == true){
                var count = 0
                val currentUser = BmobUser.getCurrentUser(User::class.java)
                //先获取所有user的objectId
                val bmobQuery: BmobQuery<User> = BmobQuery<User>()
                bmobQuery.findObjects(object : FindListener<User>(){
                    override fun done(p0: MutableList<User>?, p1: BmobException?) {
                        if(p1 == null){
                            if(p0!=null){
                                //遍历每个用户的 follow relation内容
                                for(p in p0){
                                    val userQuery: BmobQuery<User> = BmobQuery<User>()
                                    userQuery.addWhereRelatedTo("follows", BmobPointer(p))
                                    userQuery.findObjects(object : FindListener<User>(){
                                        override fun done(userList: MutableList<User>?, exception: BmobException?) {
                                            if(exception == null){
                                                if(userList!=null){
                                                    for(u in userList){
                                                        if(u.objectId == currentUser.objectId){
                                                            count++
                                                            user_follow.text = count.toString() + "关注者"
                                                            runOnUiThread{
                                                                //Log.d("DynamicActivity",count.toString() + "关注者")
                                                                user_follow.text = count.toString() + "关注者"
                                                            }
                                                        }
                                                    }
                                                }
                                            }else{
                                                Toast.makeText(this@DynamicActivity,exception.message,Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    })
                                }
                            }
                        }else{
                            Toast.makeText(this@DynamicActivity,p1.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }

//    override fun onStart() {
//        super.onStart()
//
//        if (isNetwork(this)) {
//
//            user = BmobUser.getCurrentUser(User::class.java)
//            Glide.with(this).load(NetworkUtils.PIC_PRE_PATH + "/images" + user.image_url).placeholder(R.drawable.nav_icon).into(user_image)
//            user_name.text = user.user_nickname
//
//            /*读取数据库的信息*/
//            queryObjects()
//
//            if(SPUtils.get(DanceApplication.context,"fans_cal",false) == true){
//                user_fans.text = SPUtils.get(DanceApplication.context,"fans_count",0).toString() + "关注者"
//            }
//            if(SPUtils.get(DanceApplication.context,"follow_cal",false) == true){
//                user_follow.text = SPUtils.get(DanceApplication.context,"follow_count",0).toString() + "正在关注"
//            }
//
//        } else {
//            /*没有网络读不了*/
//            AlertDialog.Builder(this)
//                .setTitle("无法连接网络！")
//                .setPositiveButton(
//                    "确定",
//                    DialogInterface.OnClickListener { _, _ ->
//                    }
//                ).setCancelable(true)
//                .create()
//                .show()
//        }
//    }

    /*private fun statPhotoViewActivity(position: Int,imagesList:ArrayList<String>) {
        val intent = Intent(this, ShowPicActivity::class.java)
        intent.putStringArrayListExtra("images", imagesList)
        intent.putExtra("position", position)
        startActivity(intent)
    }*/
}