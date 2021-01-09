package com.example.dacnce.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacnce.R
import com.example.dacnce.activity.PersonDynamicActivity
import com.example.dacnce.bean.FansItem
import com.example.dacnce.utils.NetworkUtils
import de.hdodenhof.circleimageview.CircleImageView

class FansAdapter(val context: Context,private val fansItemList:List<FansItem>):RecyclerView.Adapter<FansAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val ivIcon:CircleImageView = view.findViewById(R.id.iv_icon)
        val tvUserName:TextView = view.findViewById(R.id.tv_username)
        val tvMessage:TextView = view.findViewById(R.id.tv_message)
        val rlFans: RelativeLayout = view.findViewById(R.id.rl_fans)
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
        Glide.with(context)
            .load(NetworkUtils.PIC_PRE_PATH + "/images" + fansItem.icon)
            .placeholder(R.drawable.nav_icon)
            .into(holder.ivIcon)
        holder.tvUserName.text = fansItem.username
        holder.tvMessage.text = fansItem.message

        holder.tvUserName.setOnClickListener {
            val intent = Intent(context, PersonDynamicActivity::class.java)
            intent.putExtra("userObjectId",fansItem.fanUserObjectId)
            context.startActivity(intent)
        }

        holder.ivIcon.setOnClickListener {
            val intent = Intent(context, PersonDynamicActivity::class.java)
            intent.putExtra("userObjectId",fansItem.fanUserObjectId)
            context.startActivity(intent)
        }

        holder.tvMessage.setOnClickListener {
            val intent = Intent(context, PersonDynamicActivity::class.java)
            intent.putExtra("userObjectId",fansItem.fanUserObjectId)
            context.startActivity(intent)
        }

        holder.rlFans.setOnClickListener {
            val intent = Intent(context, PersonDynamicActivity::class.java)
            intent.putExtra("userObjectId",fansItem.fanUserObjectId)
            context.startActivity(intent)
        }
    }
}