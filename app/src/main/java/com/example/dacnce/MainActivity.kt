package com.example.dacnce

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.dacnce.activity.PostPictureActivity
import com.example.dacnce.activity.PostVideoActivity
import com.example.dacnce.adapter.MainViewpagerAdapter
import com.example.dacnce.fragment.AccountFragment
import com.example.dacnce.fragment.ContactsFragment
import com.example.dacnce.fragment.DiscoverFragment
import com.example.dacnce.fragment.MessageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mingle.entity.MenuEntity
import com.mingle.sweetpick.DimEffect
import com.mingle.sweetpick.SweetSheet
import com.mingle.sweetpick.ViewPagerDelegate
import kotlinx.android.synthetic.main.activity_main.*


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

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.menu_discover -> {
                closeSheet()
                viewPager.currentItem = 0
                bv_bottomNavigation.menu.findItem(R.id.menu_discover).isChecked = true

            }
            R.id.menu_message -> {
                closeSheet()
                viewPager.currentItem = 1
                bv_bottomNavigation.menu.findItem(R.id.menu_message).isChecked = true

            }
            R.id.menu_new -> {
                closeSheet()
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
                closeSheet()
                viewPager.currentItem = 2
                bv_bottomNavigation.menu.findItem(R.id.menu_contacts).isChecked = true
            }
            R.id.menu_me -> {
                closeSheet()
                viewPager.currentItem = 3
                bv_bottomNavigation.menu.findItem(R.id.menu_me).isChecked = true
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
        menuEntity.iconId = R.drawable.ic_post_qa
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
            //Toast.makeText(this, menuEntity1.title.toString() + "  " + position, Toast.LENGTH_SHORT).show()
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

}

