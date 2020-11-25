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
import com.example.dacnce.bean.FansItem
import de.hdodenhof.circleimageview.CircleImageView

class FansAdapter(val context: Context,private val fansItemList:List<FansItem>):RecyclerView.Adapter<FansAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val ivIcon:CircleImageView = view.findViewById(R.id.iv_icon)
        val tvUserName:TextView = view.findViewById(R.id.tv_username)
        val tvMessage:TextView = view.findViewById(R.id.tv_message)
        val tvCancelFollow:TextView = view.findViewById(R.id.tv_cancel_follow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_fans,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return fansItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fansItem = fansItemList[position]
        Glide.with(context).load(fansItem.icon).into(holder.ivIcon)
        holder.tvUserName.text = fansItem.username
        holder.tvMessage.text = fansItem.message

        holder.tvCancelFollow.setOnClickListener {
            Toast.makeText(context,"Click cancel_follow",Toast.LENGTH_SHORT).show()
        }
    }
}