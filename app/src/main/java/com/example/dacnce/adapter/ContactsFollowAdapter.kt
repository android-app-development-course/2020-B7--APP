package com.example.dacnce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacnce.R
import com.example.dacnce.bean.ContactsFollowItem
import de.hdodenhof.circleimageview.CircleImageView

class ContactsFollowAdapter(val context:Context,private val contactsFollowItemList:List<ContactsFollowItem>):RecyclerView.Adapter<ContactsFollowAdapter.ViewHolder>(){

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val userImage: CircleImageView = view.findViewById(R.id.user_image)
        val userName: TextView = view.findViewById(R.id.user_name)
        val userSignature: TextView = view.findViewById(R.id.user_signature)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_contacts_follow,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactsFollowItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contactsFollowItem = contactsFollowItemList[position]
        Glide.with(context).load(contactsFollowItem.userImage).into(holder.userImage)
        holder.userName.text = contactsFollowItem.userName
        holder.userSignature.text = contactsFollowItem.userSignature

    }

}