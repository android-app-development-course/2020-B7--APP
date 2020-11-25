package com.example.dacnce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacnce.R
import com.example.dacnce.bean.DynamicListItem
import com.example.dacnce.bean.PictureItem
import com.sackcentury.shinebuttonlib.ShineButton
import de.hdodenhof.circleimageview.CircleImageView

class DynamicAdapter(val context: Context,private val dynamicListItemList:List<DynamicListItem>):RecyclerView.Adapter<DynamicAdapter.ViewHolder>(){

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val userImage: CircleImageView = view.findViewById(R.id.user_image)
        val userName: TextView = view.findViewById(R.id.user_name)
        val more: ImageView = view.findViewById(R.id.more)
        val contentMain: TextView = view.findViewById(R.id.content_main)
        val pOmLayout: LinearLayout = view.findViewById(R.id.picture_or_movie)
        val pictureRecyclerView: RecyclerView = view.findViewById(R.id.picture_recyclerView)
        val videoCardView: CardView = view.findViewById(R.id.video_cardView)
        val videoImage: ImageView = view.findViewById(R.id.video_image)
        val like: ShineButton = view.findViewById(R.id.like)
        val likeNumber: TextView = view.findViewById(R.id.like_number)
        val reply: ImageView = view.findViewById(R.id.reply)
        val replyNumber: TextView = view.findViewById(R.id.reply_number)
        val share: ImageView = view.findViewById(R.id.share)
        val peopleNumber: TextView = view.findViewById(R.id.people_number)

        lateinit var pictureItemList:ArrayList<PictureItem>
        lateinit var dynamicPictureAdapter: DynamicPictureAdapter

        fun bind(position:Int,dynamicListItem: DynamicListItem){
            Glide.with(context).load(dynamicListItem.userImage).into(userImage)
            userName.text = dynamicListItem.userName
            more.setOnClickListener{
                Toast.makeText(context,"click more",Toast.LENGTH_SHORT).show()
            }
            contentMain.text = dynamicListItem.inputContent
            if(dynamicListItem.isVideo){
                videoCardView.visibility = View.VISIBLE
                pictureRecyclerView.visibility = View.GONE
                Glide.with(context).load(dynamicListItem.videoImage).into(videoImage)
            }else{
                videoCardView.visibility = View.GONE
                pictureRecyclerView.visibility = View.VISIBLE
                pictureItemList = dynamicListItem.imageList!!
//                pictureItemList.add(PictureItem(R.drawable.example9,false))
//                pictureItemList.add(PictureItem(R.drawable.example3,false))
//                pictureItemList.add(PictureItem(R.drawable.example4,false))
//                pictureItemList.add(PictureItem(R.drawable.example2,false))
//                pictureItemList.add(PictureItem(R.drawable.example7,false))
//                pictureItemList.add(PictureItem(R.drawable.example,false))
//                pictureItemList.add(PictureItem(R.drawable.example6,false))
//                pictureItemList.add(PictureItem(R.drawable.example5,false))
//                pictureItemList.add(PictureItem(R.drawable.example8,false))

                val layoutManager = GridLayoutManager(context,3)
                pictureRecyclerView.layoutManager = layoutManager
                dynamicPictureAdapter = DynamicPictureAdapter(context,pictureItemList)
                pictureRecyclerView.adapter = dynamicPictureAdapter
            }

            reply.setOnClickListener {
                Toast.makeText(context,"click reply",Toast.LENGTH_SHORT).show()
            }
            share.setOnClickListener {
                Toast.makeText(context,"click share",Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_dynamic,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dynamicListItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position,dynamicListItemList[position])
    }
}