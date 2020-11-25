package com.example.dacnce.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacnce.R
import com.example.dacnce.adapter.NoticeAdapter
import com.example.dacnce.adapter.PrivateMessageAdapter
import com.example.dacnce.bean.NoticeItem
import com.example.dacnce.bean.PrivateMessageItem

class NoticeFragment : Fragment() {

    private var noticeItemList = ArrayList<NoticeItem>()
    private lateinit var noticeRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_notice, container, false)
        noticeItemList = ArrayList<NoticeItem>()
        noticeItemList.add(NoticeItem(R.drawable.nav_icon,"一只小沐白","11月24日","真tmd帅",true))
        noticeItemList.add(NoticeItem(R.drawable.example2,"耗子尾汁","11月23日","爱了爱了，教教我，好吗",true))
        noticeItemList.add(NoticeItem(R.drawable.example,"年轻人不讲武德","11月23日","我分享：赛博朋克 x Hiphop舞蹈，荷兰大神Kevin Paradox超帅FreeStyle小集合",false))
        noticeItemList.add(NoticeItem(R.drawable.nav_icon,"一只小沐白","11月22日","真tmd帅",true))
        noticeItemList.add(NoticeItem(R.drawable.example2,"偷袭","11月21日","爱了爱了，教教我，好吗",true))
        noticeItemList.add(NoticeItem(R.drawable.example,"发生了什么事","11月21日","我分享：赛博朋克 x Hiphop舞蹈，荷兰大神Kevin Paradox超帅FreeStyle小集合",false))

        val layoutManager = LinearLayoutManager(activity)
        noticeRecyclerView = view.findViewById(R.id.notice_rv)
        noticeRecyclerView.layoutManager = layoutManager

        val adapter = context?.let { NoticeAdapter(it,noticeItemList) }
        noticeRecyclerView.adapter = adapter

        return view
    }

}