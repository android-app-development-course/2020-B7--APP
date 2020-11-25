package com.example.dacnce.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacnce.R
import com.example.dacnce.adapter.DynamicAdapter
import com.example.dacnce.adapter.HistoryAdapter
import com.example.dacnce.bean.DynamicItem
import com.example.dacnce.bean.HistoryItem
import com.example.dacnce.utils.RecyclerItemClickListener
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    private lateinit var historyAdapter: HistoryAdapter
    private val historyItemList = ArrayList<HistoryItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        //初始化Toolbar
        setSupportActionBar(toolbarHistory)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "观看历史"

        historyItemList.add(HistoryItem(R.drawable.example3,"【舞蹈风暴】民间反串艺人的一生与现代人具象化的万搬思绪像碰撞","11分钟前","up主：陈海宗","11:30"))
        historyItemList.add(HistoryItem(R.drawable.example2,"Swag慢慢！根本停不下来的超中毒屋顶起舞GO Crazy！AIKI编舞（All Ready）","18分钟前","up主：陈海宗","06:12"))
        historyItemList.add(HistoryItem(R.drawable.example6,"赛博朋克 x Hiphop舞蹈，荷兰大神Kevin Paradox超帅FreeStyle小集合","5分钟前","up主：陈海宗","19:12"))
        historyItemList.add(HistoryItem(R.drawable.example4,"【舞蹈风暴】民间反串艺人的一生与现代人具象化的万搬思绪像碰撞","55秒前","up主：陈海宗","03:36"))
        historyItemList.add(HistoryItem(R.drawable.example7,"【舞蹈风暴】民间反串艺人的一生与现代人具象化的万搬思绪像碰撞","刚刚","up主：陈海宗","01:45"))
        historyItemList.add(HistoryItem(R.drawable.example8,"【舞蹈风暴】民间反串艺人的一生与现代人具象化的万搬思绪像碰撞","2天前","up主：陈海宗","00:15"))
        historyItemList.add(HistoryItem(R.drawable.example4,"【舞蹈风暴】民间反串艺人的一生与现代人具象化的万搬思绪像碰撞","11分钟前","up主：陈海宗","13:14"))

        val layoutManager = LinearLayoutManager(this)
        main_rv.layoutManager = layoutManager
        main_rv.itemAnimator = DefaultItemAnimator()
        historyAdapter = HistoryAdapter(this,historyItemList)
        main_rv.adapter = historyAdapter

        main_rv.addOnItemTouchListener(RecyclerItemClickListener(this, main_rv, object : RecyclerItemClickListener.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {
                Toast.makeText(applicationContext, "click Recyclerview$position", Toast.LENGTH_SHORT).show()
            }
            override fun onItemLongClick(view: View?, position: Int) {

            }
        }))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                //toolBar点击返回按钮，销毁单前activity
                finish()
                Toast.makeText(this,"Finish HistoryActivity", Toast.LENGTH_SHORT).show()
            }
            R.id.history_video_clear -> {
                //数据库操作
                Toast.makeText(this,"清除历史数据",Toast.LENGTH_SHORT).show()
                historyAdapter.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bar_history_video_clear,menu)
        return true
    }
}