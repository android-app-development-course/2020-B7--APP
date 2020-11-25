package com.example.dacnce.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.dacnce.R
import com.example.dacnce.adapter.DiscoverAdapter
import com.google.android.material.tabs.TabLayout


class DiscoverFragment : Fragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var discoverAdapter: DiscoverAdapter
    private lateinit var tabsLayout:TabLayout

    private val myFragmentList: MutableList<Fragment> = mutableListOf(
        HotFragment(),
        FollowFragment()
    )

    private val myFragmentTitleList: MutableList<String> = mutableListOf(
        "热门","关注"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_discover, container, false)
        viewPager = view.findViewById(R.id.discover_viewpager)
        discoverAdapter = DiscoverAdapter(childFragmentManager,myFragmentList,myFragmentTitleList)
        viewPager.adapter = discoverAdapter
        tabsLayout = view.findViewById(R.id.tabs)
        tabsLayout.setupWithViewPager(viewPager)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }




}