package com.example.dacnce.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
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
import com.example.dacnce.adapter.HotAdapter
import com.example.dacnce.bean.HotItem
import com.example.dacnce.bean.PictureItem
import com.example.dacnce.bean.PostModel
import com.example.dacnce.bean.User
import com.example.dacnce.utils.NetworkUtils
import com.mingle.entity.MenuEntity
import com.mingle.sweetpick.DimEffect
import com.mingle.sweetpick.SweetSheet
import com.mingle.sweetpick.ViewPagerDelegate
import es.dmoral.toasty.Toasty

class HotFragment: Fragment(){
    private val hotItemList = ArrayList<HotItem>()
    private lateinit var hotRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: HotAdapter
    private lateinit var mySweetSheet: SweetSheet
    private lateinit var hot_rl:RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_hot, container, false)

        hot_rl = view.findViewById(R.id.hot_rl)

        val layoutManager = LinearLayoutManager(activity)
        hotRecyclerView = view.findViewById(R.id.hot_recyclerView)

        hotRecyclerView.layoutManager = layoutManager

        adapter = HotAdapter(requireContext(),hotItemList,requireActivity())

        hotRecyclerView.itemAnimator = DefaultItemAnimator()

        hotRecyclerView.adapter = adapter

        setupRecyclerView()

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
        swipeRefreshLayout.setOnRefreshListener {
            refreshHotItem()
        }

        if(NetworkUtils.isConnected()){
            refreshHotItem()
        }else{
            Toasty.error(DanceApplication.context,"无网络连接",Toast.LENGTH_SHORT).show()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

//    private fun refreshHotItem(){
//        this.hotItemList.clear()
//        if(NetworkUtils.isConnected()){
//            val bmobQuery:BmobQuery<PostModel> = BmobQuery()
//            bmobQuery.order("-updatedAt")
//                .setLimit(10)
//                .include("user")
//            bmobQuery.findObjects(object :FindListener<PostModel>(){
//                override fun done(posts: MutableList<PostModel>?, ex: BmobException?) {
//                    if(ex == null){
//                        if(posts != null){
//
//                            for(p in posts){
//                                val userQuery:BmobQuery<User> = BmobQuery<User>()
//                                userQuery.addWhereRelatedTo("fans", BmobPointer(p.user))
//                                userQuery.findObjects(object :FindListener<User>(){
//                                    override fun done(userList: MutableList<User>?, exception: BmobException?) {
//
//                                        if(exception == null){
//                                            if(userList != null){
//                                                var flag:Boolean = false
//                                                for(u in userList){
//                                                    val userNow = BmobUser.getCurrentUser(User::class.java)
//                                                    if(u.objectId == userNow.objectId){
//                                                        flag = true
//                                                    }
//                                                }
//                                                activity?.runOnUiThread{
//                                                    judgeQueryData(p,flag)
//
//                                                    adapter.setOnItemViewClickListener(object:HotAdapter.OnItemViewClickLisenter{
//                                                        override fun OnItemClick(view: View, position: Int) {
//                                                            if(mySweetSheet == null){
//                                                                setupRecyclerView()
//                                                                mySweetSheet.show()
//                                                            }
//                                                            else if(mySweetSheet.isShow){
//                                                                mySweetSheet.dismiss()
//                                                            }else{
//                                                                setupRecyclerView()
//                                                                mySweetSheet.show()
//                                                            }
//                                                        }
//                                                    })
//                                                    swipeRefreshLayout.isRefreshing = false
//
//                                                    for(i in hotItemList){
//                                                        Log.d("hotItemList33",i.user.objectId)
//                                                    }
//                                                }
//                                            }else{
//
//                                            }
//                                        }
//                                    }
//                                })
//                            }
//                        }else{
//                            activity?.runOnUiThread {
//                                swipeRefreshLayout.isRefreshing = false
//                            }
//                        }
//                    } else {
//                        if(activity!=null){
//                            Toasty.error(activity!!, ex.message.toString(), Toast.LENGTH_LONG).show()
//                        }
//                    }
//                }
//            })
//        }
//    }

    private fun refreshHotItem(){
        if(NetworkUtils.isConnected()){
            val bmobQuery:BmobQuery<PostModel> = BmobQuery()
            bmobQuery.order("-updatedAt")
                .setLimit(10)
                .include("user")
            bmobQuery.findObjects(object :FindListener<PostModel>(){
                override fun done(posts: MutableList<PostModel>?, ex: BmobException?) {
                    if(ex == null){
                        if(posts != null){
                            activity?.runOnUiThread {
                                dealQueryData(posts)

//                                adapter = HotAdapter(activity!!,hotItemList)
//
//                                hotRecyclerView.itemAnimator = DefaultItemAnimator()
//
//                                hotRecyclerView.adapter = adapter

                                adapter.setOnItemViewClickListener(object:HotAdapter.OnItemViewClickLisenter{
                                    override fun OnItemClick(view: View, position: Int) {
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
                                })

                                swipeRefreshLayout.isRefreshing = false

                            }
                        }
                    } else {
                        if(activity!=null){
                            Toasty.error(activity!!, ex.message.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })
        }
    }


    private fun judgeQueryData(item:PostModel,flag:Boolean){
        var isVideo = false
        var videoPath = "NoPath"

        if (item.pic_or_mov == "mov") {
            isVideo = true
            videoPath = item.post_mov_path
        }

        val picItemList = ArrayList<PictureItem>()
        val picPaths = item.post_pic_path.split(",")
        if (picPaths.isNotEmpty()) {
            for (i in 0 until picPaths.size - 1) {

                picItemList.add(PictureItem(picPaths[i], isVideo,videoPath))
            }
        }

        val model = HotItem(
            item.objectId,
            picItemList,
            videoPath,
            item.user.user_nickname!!,
            item.user.image_url!!,
            isVideo,
            null,
            item.post_content,
            item.user,
            flag
        )
        this.hotItemList.add(model)
        this.adapter.notifyDataSetChanged()
    }

    private fun dealQueryData(list: MutableList<PostModel>) {
        this.hotItemList.clear()
        for (item: PostModel in list) {

            var isVideo = false
            var videoPath = "NoPath"

            if (item.pic_or_mov == "mov") {
                isVideo = true
                videoPath = item.post_mov_path
            }

            val picItemList = ArrayList<PictureItem>()
            val picPaths = item.post_pic_path.split(",")
            if (picPaths.isNotEmpty()) {
                for (i in 0 until picPaths.size - 1) {

                    picItemList.add(PictureItem(picPaths[i], isVideo,videoPath))
                }
            }

            val model = HotItem(
                item.objectId,
                picItemList,
                videoPath,
                item.user.user_nickname!!,
                item.user.image_url!!,
                isVideo,
                null,
                item.post_content,
                item.user,
                false
            )
            this.hotItemList.add(model)

        }
        this.adapter.notifyDataSetChanged()
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

        mySweetSheet = SweetSheet(hot_rl)
        mySweetSheet.setMenuList(list)
        mySweetSheet.setDelegate(ViewPagerDelegate())
        mySweetSheet.setBackgroundEffect(DimEffect(1f))
    }

}