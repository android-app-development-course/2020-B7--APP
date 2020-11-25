package com.example.dacnce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacnce.R
import com.example.dacnce.bean.NoticeItem
import de.hdodenhof.circleimageview.CircleImageView

class NoticeAdapter(val context: Context, private val noticeItemList: List<NoticeItem>):RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val userImage: CircleImageView = view.findViewById(R.id.user_image)
        val userName: TextView = view.findViewById(R.id.user_name)
        val content: TextView = view.findViewById(R.id.content_notice)
        val date: TextView = view.findViewById(R.id.text_date)
        val dyOrCom: TextView = view.findViewById(R.id.dy_or_com)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_notice,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return noticeItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noticeItem = noticeItemList[position]
        Glide.with(context).load(noticeItem.userImage).into(holder.userImage)
        holder.userName.text = noticeItem.userName
        holder.date.text = noticeItem.date
        holder.content.text = noticeItem.content
        if(noticeItem.dyOrCom){
            holder.dyOrCom.text = "赞了你的评论:"
        }
        else{
            holder.dyOrCom.text = "赞了你的动态:"
        }
    }
}