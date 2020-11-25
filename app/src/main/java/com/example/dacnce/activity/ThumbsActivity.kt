package com.example.dacnce.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacnce.R
import com.example.dacnce.adapter.DynamicAdapter
import com.example.dacnce.bean.DynamicItem
import com.example.dacnce.utils.RecyclerItemClickListener
import kotlinx.android.synthetic.main.activity_thumbs.*

class ThumbsActivity : AppCompatActivity() {

    private lateinit var dynamicAdapter: DynamicAdapter
    private val dynamicItemList = ArrayList<DynamicItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thumbs)

        //初始化Toolbar
        setSupportActionBar(toolbarThumbs)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "我赞过的"

        dynamicItemList.add(DynamicItem(R.drawable.example3,"张学友"))
        dynamicItemList.add(DynamicItem(R.drawable.example2,"张家辉"))
        dynamicItemList.add(DynamicItem(R.drawable.example2,"张无忌"))
        dynamicItemList.add(DynamicItem(R.drawable.example4,"张无忌"))
        dynamicItemList.add(DynamicItem(R.drawable.example5,"张无忌"))
        dynamicItemList.add(DynamicItem(R.drawable.example6,"张无忌"))
        dynamicItemList.add(DynamicItem(R.drawable.example7,"张无忌"))
        dynamicItemList.add(DynamicItem(R.drawable.example8,"张无忌"))
        dynamicItemList.add(DynamicItem(R.drawable.example9,"张无忌"))

//        val layoutManager = LinearLayoutManager(this)
//        main_rv.layoutManager = layoutManager
//        main_rv.itemAnimator = DefaultItemAnimator()
//        dynamicAdapter = DynamicAdapter(this,dynamicItemList)
//        main_rv.adapter = dynamicAdapter

//        main_rv.addOnItemTouchListener(RecyclerItemClickListener(this, main_rv, object : RecyclerItemClickListener.OnItemClickListener {
//
//            override fun onItemClick(view: View, position: Int) {
//                Toast.makeText(applicationContext, "click Recyclerview$position", Toast.LENGTH_SHORT).show()
//            }
//            override fun onItemLongClick(view: View?, position: Int) {
//
//            }
//        }))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                //toolBar点击返回按钮，销毁单前activity
                finish()
                Toast.makeText(this,"Finish ThumbsActivity", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}