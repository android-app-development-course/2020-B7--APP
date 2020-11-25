package com.example.dacnce.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.dacnce.R
import com.example.dacnce.adapter.DynamicAdapter
import com.example.dacnce.bean.DynamicListItem
import com.example.dacnce.bean.PictureItem
import com.example.dacnce.utils.RecyclerItemClickListener
import com.mingle.entity.MenuEntity
import com.mingle.sweetpick.DimEffect
import com.mingle.sweetpick.SweetSheet
import com.mingle.sweetpick.ViewPagerDelegate
import kotlinx.android.synthetic.main.activity_dynamic.*

class DynamicActivity : AppCompatActivity() {

    private lateinit var mySweetSheet: SweetSheet
    private lateinit var dynamicAdapter: DynamicAdapter
    private val dynamicListItemList = ArrayList<DynamicListItem>()
    private val pictureItemList = ArrayList<PictureItem>()

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

        //dynamicItemList.add(DynamicItem(R.drawable.example2,"张学友"))
        //dynamicItemList.add(DynamicItem(R.drawable.example2,"张家辉"))
        //dynamicItemList.add(DynamicItem(R.drawable.example2,"张无忌"))

        pictureItemList.add(PictureItem(R.drawable.example,false))
        pictureItemList.add(PictureItem(R.drawable.example3,false))
        pictureItemList.add(PictureItem(R.drawable.example4,false))
        pictureItemList.add(PictureItem(R.drawable.example2,false))
        pictureItemList.add(PictureItem(R.drawable.example7,false))
        pictureItemList.add(PictureItem(R.drawable.example,false))
        pictureItemList.add(PictureItem(R.drawable.example6,false))
        pictureItemList.add(PictureItem(R.drawable.example5,false))
        pictureItemList.add(PictureItem(R.drawable.example8,false))


        dynamicListItemList.add(DynamicListItem(pictureItemList,"用户的名字",R.drawable.nav_icon,false,R.drawable.example3,"琪琪怪怪的世界琪琪快怪的人"))
        dynamicListItemList.add(DynamicListItem(null,"用户的名字",R.drawable.nav_icon,true,R.drawable.example3,"琪琪怪怪的世界琪琪快怪的人"))
        dynamicListItemList.add(DynamicListItem(null,"用户的名字",R.drawable.nav_icon,true,R.drawable.example3,"琪琪怪怪的世界琪琪快怪的人"))
        dynamicListItemList.add(DynamicListItem(pictureItemList,"用户的名字",R.drawable.nav_icon,false,R.drawable.example3,"琪琪怪怪的世界琪琪快怪的人"))
        dynamicListItemList.add(DynamicListItem(pictureItemList,"用户的名字",R.drawable.nav_icon,false,R.drawable.example3,"琪琪怪怪的世界琪琪快怪的人"))

        val layoutManager = LinearLayoutManager(this)
        main_rv.layoutManager = layoutManager
        main_rv.itemAnimator = DefaultItemAnimator()
        dynamicAdapter = DynamicAdapter(this,dynamicListItemList)
        main_rv.adapter = dynamicAdapter

        main_rv.addOnItemTouchListener(RecyclerItemClickListener(this, main_rv, object : RecyclerItemClickListener.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {
                Toast.makeText(applicationContext, "click Recyclerview$position",Toast.LENGTH_SHORT).show()
            }
            override fun onItemLongClick(view: View?, position: Int) {

            }
        }))

        setupRecyclerView()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bar_dynamic_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                //toolBar点击返回按钮，销毁单前activity
                finish()
                Toast.makeText(this,"Finish DynamicActivity", Toast.LENGTH_SHORT).show()
            }
            R.id.share -> {
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
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView(){
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
}