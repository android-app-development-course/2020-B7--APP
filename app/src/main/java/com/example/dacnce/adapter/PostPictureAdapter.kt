package com.example.dacnce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacnce.R
import com.example.dacnce.bean.PostPicture


class PostPictureAdapter(val context: Context,val postPictureList:List<PostPicture>)
    :RecyclerView.Adapter<PostPictureAdapter.ViewHolder>(){

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val postPictureImage:ImageView=view.findViewById(R.id.postPictureImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.post_picture_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val postPicture=postPictureList[position]
        Glide.with(context).load(postPicture.imageId).into(holder.postPictureImage)

    }

    override fun getItemCount()=postPictureList.size
}