package com.example.dacnce.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacnce.R
import com.example.dacnce.adapter.FansAdapter
import com.example.dacnce.adapter.MyFollowAdapter
import com.example.dacnce.bean.MyFollowItem
import kotlinx.android.synthetic.main.activity_follow.*

class FollowActivity : AppCompatActivity() {

    private lateinit var myFollowAdapter: MyFollowAdapter
    private val myFollowItemList = ArrayList<MyFollowItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow)

        //初始化Toolbar
        setSupportActionBar(toolbarFollow)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "我的关注"

        myFollowItemList.add(MyFollowItem(R.drawable.example,"年轻人2","年轻人不讲武德"))
        myFollowItemList.add(MyFollowItem(R.drawable.example2,"年轻人3","我大意了，没有闪"))
        myFollowItemList.add(MyFollowItem(R.drawable.example3,"年轻人4","年轻人不讲武德"))
        myFollowItemList.add(MyFollowItem(R.drawable.example,"年轻人5","年轻人不讲武德"))
        myFollowItemList.add(MyFollowItem(R.drawable.example5,"年轻人1","年轻人不讲武德"))

        val layoutManager = LinearLayoutManager(this)
        rv_recyclerView.layoutManager = layoutManager
        myFollowAdapter = MyFollowAdapter(this,myFollowItemList)
        rv_recyclerView.adapter = myFollowAdapter

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                //toolBar点击返回按钮，销毁单前activity
                finish()
                //Toast.makeText(this,"Finish FansActivity", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}