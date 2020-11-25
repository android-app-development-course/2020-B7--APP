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

class ContactsFragment : Fragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var contactsAdapter: DiscoverAdapter
    private lateinit var tabsLayout: TabLayout

    private val myFragmentList: MutableList<Fragment> = mutableListOf(
        ContactsFollowFragment(),
        ContactsFansFragment()
    )

    private val myFragmentTitleList: MutableList<String> = mutableListOf(
        "关注","粉丝"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_contacts,container,false)
        viewPager = view.findViewById(R.id.contacts_viewpager)
        tabsLayout = view.findViewById(R.id.tabs)

        contactsAdapter = DiscoverAdapter(childFragmentManager,myFragmentList,myFragmentTitleList)
        viewPager.adapter = contactsAdapter
        tabsLayout.setupWithViewPager(viewPager)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}