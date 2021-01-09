package com.example.dacnce.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacnce.R
import com.example.dacnce.activity.ShowMovActivity
import com.example.dacnce.activity.ShowPicActivity
import com.example.dacnce.bean.FollowChildItem
import com.example.dacnce.bean.PictureItem

class FollowPictureAdapter(val context: Context,private val pictureItemList:List<FollowChildItem>):RecyclerView.Adapter<FollowPictureAdapter.ViewHolder>() {

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
        Glide.with(context).load(pictureItem.image_url).into(holder.imageView)
        if(pictureItem.isVideo){
            holder.video.visibility = View.VISIBLE

            holder.imageView.setOnClickListener {
                val intent= Intent(context, ShowMovActivity::class.java)
                intent.putExtra("preview", pictureItem.image_url)
                intent.putExtra("url",pictureItem.videoUrl)
                context.startActivity(intent)
            }


        }
        else{
            holder.video.visibility = View.GONE

            val list:ArrayList<String> = ArrayList()
            for (i : FollowChildItem in pictureItemList){
                list.add(i.image_url)
            }

            holder.imageView.setOnClickListener {
                val intent = Intent(context, ShowPicActivity::class.java)
                intent.putStringArrayListExtra("images", list)
                intent.putExtra("position", position)
                context.startActivity(intent)
            }
        }



    }
}