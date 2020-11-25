package com.example.dacnce.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class DiscoverAdapter (fragmentManager: FragmentManager, private val fragmentList: MutableList<Fragment>,private val myFragmentTitleList: MutableList<String>):
    FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return myFragmentTitleList[position]
    }

}