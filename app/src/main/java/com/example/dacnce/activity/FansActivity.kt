package com.example.dacnce.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.example.dacnce.DanceApplication
import com.example.dacnce.R
import com.example.dacnce.adapter.FansAdapter
import com.example.dacnce.bean.FansItem
import com.example.dacnce.bean.User
import com.example.dacnce.bean.UserData
import com.example.dacnce.utils.BmobUtils
import com.example.dacnce.utils.NetworkUtils
import com.example.dacnce.utils.SPUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_fans.*


class FansActivity : AppCompatActivity() {

    private lateinit var fansAdapter: FansAdapter
    private val fansItemList = ArrayList<FansItem>()

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fans)

        //初始化Toolbar
        setSupportActionBar(toolbarFans)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "我的粉丝"

        val layoutManager = LinearLayoutManager(this)
        rv_recyclerView.layoutManager = layoutManager
        fansAdapter = FansAdapter(this,fansItemList)
        rv_recyclerView.adapter = fansAdapter


        if(NetworkUtils.isConnected()){
            refreshFansItem()
        }else{
            Toasty.error(DanceApplication.context,"无网络连接",Toast.LENGTH_SHORT).show()
        }

        //创建分割线对象，第一个参数为上下文，第二个参数为RecyclerView排列方向
        //val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        //为RecyclerView添加分割线
        //rv_recyclerView.addItemDecoration(decoration)


    }

    private fun refreshFansItem(){
        count = 0
        var currentUser = BmobUser.getCurrentUser(User::class.java)
        //先获取所有user的objectId
        val bmobQuery: BmobQuery<User> = BmobQuery<User>()
        bmobQuery.findObjects(object :FindListener<User>(){
            override fun done(p0: MutableList<User>?, p1: BmobException?) {
                if(p1 == null){
                    if(p0!=null){
                        for(p in p0){
                            val userQuery: BmobQuery<User> = BmobQuery<User>()
                            userQuery.addWhereRelatedTo("follows", BmobPointer(p))
                            userQuery.findObjects(object :FindListener<User>(){
                                override fun done(userList: MutableList<User>?, exception: BmobException?) {
                                    if(exception == null){
                                        if(userList!=null){
                                            var flag:Boolean = false
                                            for(u in userList){
                                                if(u.objectId == currentUser.objectId){
                                                    flag = true
                                                    count++
                                                    SPUtils.put(DanceApplication.context,"fans_cal",true)
                                                    SPUtils.put(DanceApplication.context,"fans_count",count)
                                                    break
                                                }
                                            }
                                            if(flag){
                                                runOnUiThread{
                                                    val model = FansItem(
                                                        p.image_url!!,
                                                        p.user_nickname!!,
                                                        p.user_signature!!,
                                                        p.objectId,
                                                        p
                                                    )
                                                    fansItemList.add(model)
                                                    fansAdapter.notifyDataSetChanged()
                                                }
                                            }
                                        }
                                    }
                                }
                            })
                        }
                    }
                }else{
                    Toast.makeText(this@FansActivity,"${p1.message}",Toast.LENGTH_SHORT).show()
                }
            }

        })
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