package com.example.dacnce.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacnce.R
import com.example.dacnce.adapter.ContactsFollowAdapter
import com.example.dacnce.adapter.PrivateMessageAdapter
import com.example.dacnce.bean.ContactsFollowItem
import com.example.dacnce.bean.PrivateMessageItem

class ContactsFollowFragment : Fragment() {

    private var contactsFollowItemList = ArrayList<ContactsFollowItem>()
    private lateinit var contactsFollowRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_contacts_follow, container, false)
        contactsFollowItemList = ArrayList<ContactsFollowItem>()
        contactsFollowItemList.add(ContactsFollowItem(R.drawable.nav_icon,"一只小沐白","傻人多怪事"))
        contactsFollowItemList.add(ContactsFollowItem(R.drawable.example2,"耗子尾汁","让我们成为朋友吧"))
        contactsFollowItemList.add(ContactsFollowItem(R.drawable.example,"年轻人不讲武德","好兄弟"))


        val layoutManager = LinearLayoutManager(activity)
        contactsFollowRecyclerView = view.findViewById(R.id.contacts_follow_rv)
        contactsFollowRecyclerView.layoutManager = layoutManager

        val adapter = context?.let { ContactsFollowAdapter(it,contactsFollowItemList) }
        contactsFollowRecyclerView.adapter = adapter

        return view
    }

}