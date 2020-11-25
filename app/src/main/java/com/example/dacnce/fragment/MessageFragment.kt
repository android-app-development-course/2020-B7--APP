package com.example.dacnce.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.dacnce.R
import com.example.dacnce.adapter.DiscoverAdapter
import com.google.android.material.tabs.TabLayout

class MessageFragment: Fragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var messageAdapter: DiscoverAdapter
    private lateinit var tabsLayout: TabLayout

    private val myFragmentList: MutableList<Fragment> = mutableListOf(
        PrivateMessageFragment(),
        CommentFragment(),
        MentionMeFragment(),
        NoticeFragment()
    )

    private val myFragmentTitleList: MutableList<String> = mutableListOf(
        "私信","评论","@我","通知"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_message,container,false)
        viewPager = view.findViewById(R.id.message_viewpager)
        tabsLayout = view.findViewById(R.id.tabs)

        messageAdapter = DiscoverAdapter(childFragmentManager,myFragmentList,myFragmentTitleList)
        viewPager.adapter = messageAdapter
        tabsLayout.setupWithViewPager(viewPager)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}