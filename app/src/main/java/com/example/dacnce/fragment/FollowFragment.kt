package com.example.dacnce.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.example.dacnce.DanceApplication
import com.example.dacnce.R
import com.example.dacnce.adapter.FollowAdapter
import com.example.dacnce.bean.*
import com.example.dacnce.utils.NetworkUtils
import com.example.dacnce.utils.SPUtils
import es.dmoral.toasty.Toasty

class FollowFragment: Fragment() {

    private val followItemList = ArrayList<FollowItem>()
    private lateinit var followRecyclerView: RecyclerView
    private lateinit var adapter: FollowAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_follow, container, false)

        val layoutManager = LinearLayoutManager(activity)
        followRecyclerView = view.findViewById(R.id.follow_recyclerView)
        followRecyclerView.layoutManager = layoutManager
        adapter = context?.let { FollowAdapter(it,followItemList) }!!
        followRecyclerView.adapter = adapter

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        if(NetworkUtils.isConnected()){
            if(BmobUser.getCurrentUser(User::class.java) != null &&
                SPUtils.get(DanceApplication.context,"isLogin",false) == true){
                refreshData()
            }
        }else{
            Toasty.error(DanceApplication.context,"无网络连接", Toast.LENGTH_SHORT).show()
        }
        return view
    }

    private fun refreshData(){
        followItemList.clear()
        val userQuery: BmobQuery<User> = BmobQuery<User>()
        userQuery.addWhereRelatedTo("follows", BmobPointer(BmobUser.getCurrentUser(User::class.java)))
        userQuery.findObjects(object : FindListener<User>() {
            override fun done(p0: MutableList<User>?, p1: BmobException?) {
                if(p1 == null) {
                    if (p0 != null) {
                        for(p in p0){
                            val bmobQuery: BmobQuery<PostModel> = BmobQuery<PostModel>()
                            bmobQuery.addWhereEqualTo("user",p)
                                .order("-updatedAt")
                                .include("user")
                                .setLimit(5)
                            bmobQuery.findObjects(object :FindListener<PostModel>(){
                                override fun done(posts: MutableList<PostModel>?, ex: BmobException?) {
                                    if(ex == null){
                                        if(posts!=null){
                                            activity?.runOnUiThread {
                                                val pictureList = ArrayList<FollowChildItem>()
                                                for(p in posts){
                                                    var isVideo=false
                                                    var videoPath = "NoPath"
                                                    if(p.pic_or_mov=="mov") {
                                                        isVideo = true
                                                        videoPath = p.post_mov_path
                                                    }
                                                    val picPaths= p.post_pic_path.split(",")
                                                    if(picPaths.isNotEmpty()){
                                                        if(picPaths.size > 3){
                                                            for(i in 0 until 3){
                                                                pictureList.add(FollowChildItem(p.objectId,picPaths[i],isVideo,videoPath))
                                                            }
                                                        }else{
                                                            for(i in 0 until picPaths.size - 1 ){
                                                                pictureList.add(FollowChildItem(p.objectId,picPaths[i],isVideo,videoPath))
                                                            }
                                                        }
                                                    }
                                                }

                                                val model = FollowItem(
                                                    p.image_url!!,
                                                    p.user_nickname!!,
                                                    p.user_signature!!,
                                                    pictureList,
                                                    p.objectId,
                                                    p
                                                )

                                                followItemList.add(model)
                                                adapter.notifyDataSetChanged()

                                                swipeRefreshLayout.isRefreshing = false
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}