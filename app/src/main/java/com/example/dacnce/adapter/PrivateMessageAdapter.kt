package com.example.dacnce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacnce.R
import com.example.dacnce.bean.PrivateMessageItem
import de.hdodenhof.circleimageview.CircleImageView

class PrivateMessageAdapter(val context: Context,private val privateMessageItemList: List<PrivateMessageItem>):RecyclerView.Adapter<PrivateMessageAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val userImage: CircleImageView = view.findViewById(R.id.user_image)
        val userName: TextView = view.findViewById(R.id.user_name)
        val content: TextView = view.findViewById(R.id.content_private_message)
        val date: TextView = view.findViewById(R.id.text_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_private_message,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return privateMessageItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val privateMessageItem = privateMessageItemList[position]
        Glide.with(context).load(privateMessageItem.userImage).into(holder.userImage)
        holder.userName.text = privateMessageItem.userName
        holder.date.text = privateMessageItem.date
        holder.content.text = privateMessageItem.content
    }
}