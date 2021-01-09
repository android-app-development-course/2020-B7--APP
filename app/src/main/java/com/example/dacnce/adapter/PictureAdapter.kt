package com.example.dacnce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacnce.R
import com.example.dacnce.bean.PictureItem

class PictureAdapter(val context: Context,private val pictureItemList:List<PictureItem>):RecyclerView.Adapter<PictureAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val imageView: ImageView = view.findViewById(R.id.picture)
        val video: ImageView = view.findViewById(R.id.video)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_picture,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pictureItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val pictureItem = pictureItemList[position]
        //val resources: Resources = context.resources
        //val drawable: Drawable = resources.getDrawable(pictureItem.image)
        //holder.imageView.background = drawable
        Glide.with(context).load(pictureItem.image).into(holder.imageView)
        if(pictureItem.isVideo){
            holder.video.visibility = View.VISIBLE
        }
        else{
            holder.video.visibility = View.GONE
        }
    }
}