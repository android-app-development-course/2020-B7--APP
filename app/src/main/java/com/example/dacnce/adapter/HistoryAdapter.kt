package com.example.dacnce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacnce.R
import com.example.dacnce.bean.HistoryItem

class HistoryAdapter(val context:Context,private val historyItemList:List<HistoryItem>):RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val videoImage:ImageView = view.findViewById(R.id.video_image)
        val contentTitle:TextView = view.findViewById(R.id.content_title)
        val timePass:TextView = view.findViewById(R.id.time_pass)
        val userName:TextView = view.findViewById(R.id.user_name)
        val videoTime:TextView = view.findViewById(R.id.video_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_history,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historyItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyItem = historyItemList[position]
        Glide.with(context).load(historyItem.videoImage).into(holder.videoImage)
        holder.contentTitle.text = historyItem.contentTitle
        holder.timePass.text = historyItem.timePass
        holder.userName.text = historyItem.userName
        holder.videoTime.text = historyItem.videoTime
    }
}