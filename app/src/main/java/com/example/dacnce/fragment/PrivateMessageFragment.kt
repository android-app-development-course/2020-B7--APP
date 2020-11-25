package com.example.dacnce.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacnce.R
import com.example.dacnce.adapter.FollowAdapter
import com.example.dacnce.adapter.PrivateMessageAdapter
import com.example.dacnce.bean.FollowItem
import com.example.dacnce.bean.PrivateMessageItem

class PrivateMessageFragment : Fragment() {

    private var privateMessageItemList = ArrayList<PrivateMessageItem>()
    private lateinit var privateMessageRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_private_message, container, false)
        privateMessageItemList = ArrayList<PrivateMessageItem>()
        privateMessageItemList.add(PrivateMessageItem(R.drawable.nav_icon,"一只小沐白","11月21日","舞蹈风暴: 民间反串艺人的一生与现代人具象化的万搬思绪像碰撞"))
        privateMessageItemList.add(PrivateMessageItem(R.drawable.example2,"耗子尾汁","11月23日","Swag慢慢！根本停不下来的超中毒屋顶起舞GO Crazy！AIKI编舞（All Ready）"))
        privateMessageItemList.add(PrivateMessageItem(R.drawable.example,"年轻人不讲武德","11月24日","赛博朋克 x Hiphop舞蹈，荷兰大神Kevin Paradox超帅FreeStyle小集合"))

        val layoutManager = LinearLayoutManager(activity)
        privateMessageRecyclerView = view.findViewById(R.id.private_message_rv)
        privateMessageRecyclerView.layoutManager = layoutManager

        val adapter = context?.let { PrivateMessageAdapter(it,privateMessageItemList) }
        privateMessageRecyclerView.adapter = adapter

        return view
    }


}