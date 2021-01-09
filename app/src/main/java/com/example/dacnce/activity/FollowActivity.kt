package com.example.dacnce.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.example.dacnce.DanceApplication
import com.example.dacnce.R
import com.example.dacnce.adapter.FollowAdapterWithUpdate
import com.example.dacnce.bean.FollowItemNoPic
import com.example.dacnce.bean.User
import com.example.dacnce.utils.NetworkUtils
import com.example.dacnce.utils.SPUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_follow.*

class FollowActivity : AppCompatActivity() {

    private lateinit var followAdapterWithUpdate: FollowAdapterWithUpdate
    private val myFollowItemList = ArrayList<FollowItemNoPic>()
    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow)

        //初始化Toolbar
        setSupportActionBar(toolbarFollow)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "我的关注"

        val layoutManager = LinearLayoutManager(this)
        rv_recyclerView.layoutManager = layoutManager
        followAdapterWithUpdate = FollowAdapterWithUpdate(this,myFollowItemList,this)
        rv_recyclerView.adapter = followAdapterWithUpdate

        if(NetworkUtils.isConnected()){
            refreshFollowItem()
        }else{
            Toasty.error(DanceApplication.context,"无网络连接",Toast.LENGTH_SHORT).show()
        }
    }

    private fun refreshFollowItem(){
        count = 0
        val userQuery: BmobQuery<User> = BmobQuery<User>()
        userQuery.addWhereRelatedTo("follows", BmobPointer(BmobUser.getCurrentUser(User::class.java)))
        userQuery.findObjects(object : FindListener<User>() {
            override fun done(p0: MutableList<User>?, p1: BmobException?) {
                if(p1 == null){
                    if(p0!=null){
                        runOnUiThread{
                            for(u in p0){
                                val model = FollowItemNoPic(
                                    u.image_url!!,
                                    u.user_nickname!!,
                                    u.user_signature!!,
                                    u.objectId,
                                    u
                                )
                                count++
                                SPUtils.put(DanceApplication.context,"follow_cal",true)
                                SPUtils.put(DanceApplication.context,"follow_count",count)
                                myFollowItemList.add(model)
                            }
                            followAdapterWithUpdate.notifyDataSetChanged()
                        }
                    }
                }else{
                    Log.i("bmobError",p1.message.toString())
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                //toolBar点击返回按钮，销毁单前activity
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}