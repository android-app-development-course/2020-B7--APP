package com.example.dacnce

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import com.example.dacnce.activity.PostPictureActivity
import com.example.dacnce.activity.PostVideoActivity
import com.example.dacnce.adapter.MainViewpagerAdapter
import com.example.dacnce.bean.PostModel
import com.example.dacnce.bean.User
import com.example.dacnce.fragment.AccountFragment
import com.example.dacnce.fragment.ContactsFragment
import com.example.dacnce.fragment.DiscoverFragment
import com.example.dacnce.fragment.MessageFragment
import com.example.dacnce.utils.NetworkUtils
import com.example.dacnce.utils.SPUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mingle.entity.MenuEntity
import com.mingle.sweetpick.DimEffect
import com.mingle.sweetpick.SweetSheet
import com.mingle.sweetpick.ViewPagerDelegate
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(){
    private lateinit var mySweetSheet: SweetSheet
    private lateinit var viewPager: ViewPager
    private lateinit var mainViewpagerAdapter: MainViewpagerAdapter
    private var fragmentList: MutableList<Fragment> = mutableListOf(
        DiscoverFragment(),MessageFragment(),ContactsFragment(),AccountFragment()
    )
    //NewFragment()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.view_pager)

        mainViewpagerAdapter = MainViewpagerAdapter(supportFragmentManager, fragmentList)
        viewPager.adapter = mainViewpagerAdapter

        bv_bottomNavigation.labelVisibilityMode = 1
        bv_bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        viewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when(position){
//                    0 -> bv_bottomNavigation.menu.findItem(R.id.menu_discover).isChecked = true
//                    1 -> bv_bottomNavigation.menu.findItem(R.id.menu_message).isChecked = true
//                    2 -> bv_bottomNavigation.menu.findItem(R.id.menu_new).isChecked = true
//                    3 -> bv_bottomNavigation.menu.findItem(R.id.menu_contacts).isChecked = true
//                    4 -> bv_bottomNavigation.menu.findItem(R.id.menu_me).isChecked = true
                    0 -> bv_bottomNavigation.menu.findItem(R.id.menu_discover).isChecked = true
                    1 -> bv_bottomNavigation.menu.findItem(R.id.menu_message).isChecked = true
                    2 -> bv_bottomNavigation.menu.findItem(R.id.menu_contacts).isChecked = true
                    3 -> bv_bottomNavigation.menu.findItem(R.id.menu_me).isChecked = true
                }
            }
        })

        setupRecyclerView()

        //第一：默认初始化
        if(NetworkUtils.isConnected()){
            Bmob.initialize(this, "ca1186997e987d6205110e4c23547afb")
        }

        if(SPUtils.get(DanceApplication.context,"isLogin",false) == true){
            val user: BmobUser = BmobUser()
            user.username = SPUtils.get(DanceApplication.context,"account","") as String
            user.setPassword(SPUtils.get(DanceApplication.context,"password","")as String)
            user.login(object: SaveListener<BmobUser>(){
                override fun done(p0: BmobUser?, p1: BmobException?) {
                    SPUtils.put(DanceApplication.context,"isLogin",true)
                    SPUtils.put(DanceApplication.context,"account",SPUtils.get(DanceApplication.context,"account","") as String)
                    SPUtils.put(DanceApplication.context,"password",SPUtils.get(DanceApplication.context,"password","")as String)
                }
            })
        }
        //计算用户动态数
        calculateDynamic()

        //计算关注用户个数线程
        calculateFollows()

        //计算粉丝数线程
        calculateFans()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.menu_discover -> {
                viewPager.currentItem = 0
                bv_bottomNavigation.menu.findItem(R.id.menu_discover).isChecked = true
                closeSheet()
            }
            R.id.menu_message -> {
                viewPager.currentItem = 1
                bv_bottomNavigation.menu.findItem(R.id.menu_message).isChecked = true
                closeSheet()
            }
            R.id.menu_new -> {
                bv_bottomNavigation.menu.findItem(R.id.menu_new).isChecked = true
//                viewPager.currentItem = 2
                if(mySweetSheet == null){
                    setupRecyclerView()
                    mySweetSheet.show()
                }
                else if(mySweetSheet.isShow){
                    mySweetSheet.dismiss()
                }else{
                    setupRecyclerView()
                    mySweetSheet.show()
                }

            }
            R.id.menu_contacts -> {
                viewPager.currentItem = 2
                bv_bottomNavigation.menu.findItem(R.id.menu_contacts).isChecked = true
                closeSheet()
            }
            R.id.menu_me -> {
                viewPager.currentItem = 3
                bv_bottomNavigation.menu.findItem(R.id.menu_me).isChecked = true

                closeSheet()
            }
        }
        false
    }

    private fun setupRecyclerView(){
        val list = ArrayList<MenuEntity>()

        var menuEntity = MenuEntity()
        menuEntity.iconId = R.drawable.ic_post_picture
        menuEntity.title = "图片"
        list.add(menuEntity)

        menuEntity = MenuEntity()
        menuEntity.iconId = R.drawable.ic_post_video
        menuEntity.title = "视频"
        list.add(menuEntity)

        menuEntity = MenuEntity()
        menuEntity.iconId = R.drawable.ic_post_video
        menuEntity.title = "问答"
        list.add(menuEntity)

        menuEntity = MenuEntity()
        menuEntity.iconId = R.color.white
        menuEntity.title = ""
        list.add(menuEntity)

        menuEntity = MenuEntity()
        menuEntity.iconId = R.drawable.ic_post_cancel
        menuEntity.title = ""
        list.add(menuEntity)

        menuEntity = MenuEntity()
        menuEntity.iconId = R.color.white
        menuEntity.title = ""
        list.add(menuEntity)

        mySweetSheet = SweetSheet(main_rl)
        mySweetSheet.setMenuList(list)
        mySweetSheet.setDelegate(ViewPagerDelegate())
        mySweetSheet.setBackgroundEffect(DimEffect(1f))

        mySweetSheet.setOnMenuItemClickListener(SweetSheet.OnMenuItemClickListener { position, menuEntity1 ->
            //根据返回值, true 会关闭 SweetSheet ,false 则不会.
            Toast.makeText(this, menuEntity1.title.toString() + "  " + position, Toast.LENGTH_SHORT).show()
            if(position == 0){
                val intent = Intent(this,PostPictureActivity::class.java)
                startActivity(intent)
            }
            else if(position == 1){
                val intent = Intent(this,PostVideoActivity::class.java)
                startActivity(intent)
            }
            true
        })
    }

    private fun closeSheet(){
        if(mySweetSheet.isShow){
            //mySweetSheet.
            mySweetSheet.dismiss()
        }
    }

    override fun onBackPressed() {
        if (mySweetSheet.isShow()) {
            mySweetSheet.dismiss()
        } else {
            super.onBackPressed()
        }
    }

    public fun calculateDynamic(){
        if(NetworkUtils.isConnected()){
            if(BmobUser.getCurrentUser(User::class.java) != null &&
                SPUtils.get(DanceApplication.context,"isLogin",false) == true){
                thread {
                    var count = 0
                    val user = BmobUser.getCurrentUser(User::class.java)
                    val bmobQuery: BmobQuery<PostModel> = BmobQuery()
                    bmobQuery.addWhereEqualTo("user",user)
                        .order("-updatedAt")
                        .include("user")
                        .setLimit(10)
                    bmobQuery.findObjects(object : FindListener<PostModel>() {
                        override fun done(posts: MutableList<PostModel>?, ex: BmobException?) {
                            if (ex == null) {
                                if (posts != null) {
                                    count = posts.size
                                    SPUtils.put(DanceApplication.context,"dynamic_cal",true)
                                    SPUtils.put(DanceApplication.context,"dynamic_count",count)
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    public fun calculateFollows(){
        if(NetworkUtils.isConnected()){
            if(BmobUser.getCurrentUser(User::class.java) != null &&
                SPUtils.get(DanceApplication.context,"isLogin",false) == true){
                thread {
                    var count = 0
                    val userQuery: BmobQuery<User> = BmobQuery<User>()
                    userQuery.addWhereRelatedTo("follows", BmobPointer(BmobUser.getCurrentUser(User::class.java)))
                    userQuery.findObjects(object : FindListener<User>() {
                        override fun done(p0: MutableList<User>?, p1: BmobException?) {
                            if(p1 == null){
                                if(p0!=null){
                                    count = p0.size
                                    SPUtils.put(DanceApplication.context,"follow_cal",true)
                                    SPUtils.put(DanceApplication.context,"follow_count",count)
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    public fun calculateFans(){
        if(NetworkUtils.isConnected()){
            if(BmobUser.getCurrentUser(User::class.java) != null &&
                SPUtils.get(DanceApplication.context,"isLogin",false) == true){
                thread{
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
                                                                SPUtils.put(DanceApplication.context,"fans_cal",true)
                                                                SPUtils.put(DanceApplication.context,"fans_count",count)
                                                                break
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        })
                                    }
                                }
                            }
                        }
                    })
                }
            }
        }
    }
}

