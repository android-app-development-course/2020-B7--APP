package com.example.dacnce.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MainViewpagerAdapter(fragmentManager: FragmentManager, private val fragmentList: MutableList<Fragment>):
    FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}