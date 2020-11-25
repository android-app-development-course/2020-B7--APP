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
import com.example.dacnce.bean.FollowItem

class FollowFragment: Fragment() {

    private val followItemList = ArrayList<FollowItem>()
    private lateinit var followRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_follow, container, false)
        followItemList.add(FollowItem(R.drawable.nav_icon,"一只老沐白","奇奇怪怪的世界奇奇怪怪的人"))
        followItemList.add(FollowItem(R.drawable.nav_icon,"一只老沐白","奇奇怪怪的世界奇奇怪怪的人"))
        followItemList.add(FollowItem(R.drawable.nav_icon,"一只老沐白","奇奇怪怪的世界奇奇怪怪的人"))
        followItemList.add(FollowItem(R.drawable.nav_icon,"一只老沐白","奇奇怪怪的世界奇奇怪怪的人"))
        followItemList.add(FollowItem(R.drawable.nav_icon,"一只老沐白","奇奇怪怪的世界奇奇怪怪的人"))
        followItemList.add(FollowItem(R.drawable.nav_icon,"一只老沐白","奇奇怪怪的世界奇奇怪怪的人"))


        val layoutManager = LinearLayoutManager(activity)
        followRecyclerView = view.findViewById(R.id.follow_recyclerView)
        followRecyclerView.layoutManager = layoutManager
        val adapter = context?.let { FollowAdapter(it,followItemList) }
        followRecyclerView.adapter = adapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}