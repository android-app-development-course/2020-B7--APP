package com.example.dacnce.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacnce.R
import com.example.dacnce.bean.PictureItem

class DynamicPictureAdapter(val context: Context,private val pictureItemList:List<PictureItem>):RecyclerView.Adapter<DynamicPictureAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val imageView: ImageView = view.findViewById(R.id.picture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_dynamic_picture,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pictureItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pictureItem = pictureItemList[position]
        Glide.with(context).load(pictureItem.image).into(holder.imageView)
    }
}