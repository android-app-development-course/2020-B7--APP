package com.example.dacnce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacnce.R
import com.example.dacnce.bean.MyFollowItem
import de.hdodenhof.circleimageview.CircleImageView

class MyFollowAdapter(val context: Context,private val myFollowItemList:List<MyFollowItem>):RecyclerView.Adapter<MyFollowAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val ivIcon: CircleImageView = view.findViewById(R.id.iv_icon)
        val tvUserName: TextView = view.findViewById(R.id.tv_username)
        val tvMessage: TextView = view.findViewById(R.id.tv_message)
        val tvCancelFollow: TextView = view.findViewById(R.id.tv_cancel_follow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_myfollow,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myFollowItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myFollowItem = myFollowItemList[position]
        Glide.with(context).load(myFollowItem.icon).into(holder.ivIcon)
        holder.tvUserName.text = myFollowItem.username
        holder.tvMessage.text = myFollowItem.signature

        holder.tvCancelFollow.setOnClickListener {
            Toast.makeText(context,"Click cancel_follow",Toast.LENGTH_SHORT).show()
        }
    }
}