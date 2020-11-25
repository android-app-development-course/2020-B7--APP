package com.example.dacnce.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacnce.R
import com.example.dacnce.adapter.FansAdapter
import com.example.dacnce.bean.FansItem
import kotlinx.android.synthetic.main.activity_fans.*


class FansActivity : AppCompatActivity() {

    private lateinit var fansAdapter: FansAdapter
    private val fansItemList = ArrayList<FansItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fans)

        //初始化Toolbar
        setSupportActionBar(toolbarFans)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "我的粉丝"

        fansItemList.add(FansItem(R.drawable.example,"神奇小宝贝","傻人有傻福"))
        fansItemList.add(FansItem(R.drawable.example2,"神奇小宝贝2","傻人有傻福"))
        fansItemList.add(FansItem(R.drawable.example3,"神奇小宝贝3","傻人有傻福"))
        fansItemList.add(FansItem(R.drawable.example4,"神奇小宝贝4","傻人有傻福"))


        val layoutManager = LinearLayoutManager(this)
        rv_recyclerView.layoutManager = layoutManager
        fansAdapter = FansAdapter(this,fansItemList)
        rv_recyclerView.adapter = fansAdapter


        //创建分割线对象，第一个参数为上下文，第二个参数为RecyclerView排列方向
        //val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        //为RecyclerView添加分割线
        //rv_recyclerView.addItemDecoration(decoration)


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