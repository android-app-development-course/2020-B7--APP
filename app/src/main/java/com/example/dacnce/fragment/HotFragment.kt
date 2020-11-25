package com.example.dacnce.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dacnce.R
import com.example.dacnce.adapter.HotAdapter
import com.example.dacnce.bean.HotItem
import com.mingle.entity.MenuEntity
import com.mingle.sweetpick.DimEffect
import com.mingle.sweetpick.SweetSheet
import com.mingle.sweetpick.ViewPagerDelegate
import kotlinx.android.synthetic.main.activity_main.*

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
        hotItemList.add(HotItem(R.drawable.nav_icon,"用户1"))
        hotItemList.add(HotItem(R.drawable.nav_icon,"用户2"))
        hotItemList.add(HotItem(R.drawable.nav_icon,"用户3"))
        hotItemList.add(HotItem(R.drawable.nav_icon,"用户4"))
        hotItemList.add(HotItem(R.drawable.nav_icon,"用户5"))
        hotItemList.add(HotItem(R.drawable.nav_icon,"用户6"))

        hot_rl = view.findViewById(R.id.hot_rl)

        val layoutManager = LinearLayoutManager(activity)
        hotRecyclerView = view.findViewById(R.id.hot_recyclerView)
        hotRecyclerView.layoutManager = layoutManager
        adapter = activity?.let { HotAdapter(it,hotItemList) }!!
        hotRecyclerView.adapter = adapter

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

        setupRecyclerView()

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
        swipeRefreshLayout.setOnRefreshListener {
            refreshHotItem()
        }


        return view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun refreshHotItem(){
        //改变HotItemList数据
        hotItemList.add(HotItem(R.drawable.nav_icon,"我爱你xx"))
        hotItemList.add(HotItem(R.drawable.nav_icon,"我爱你xx"))
        hotItemList.add(HotItem(R.drawable.nav_icon,"我爱你xx"))
        adapter.notifyDataSetChanged()
        swipeRefreshLayout.isRefreshing = false
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