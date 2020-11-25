package com.example.dacnce.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacnce.R
import com.example.dacnce.activity.DynamicActivity
import com.example.dacnce.bean.FollowItem
import com.example.dacnce.bean.PictureItem
import de.hdodenhof.circleimageview.CircleImageView

class FollowAdapter(val context:Context, private val followItemList:List<FollowItem>) : RecyclerView.Adapter<FollowAdapter.ViewHolder>() {

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        private val userImage: CircleImageView = view.findViewById(R.id.user_image)
        private val userName: TextView = view.findViewById(R.id.user_name)
        private val personalSignature: TextView = view.findViewById(R.id.personal_signature)
        private val innerRecyclerView: RecyclerView = view.findViewById(R.id.inner_recyclerView)
        private lateinit var pictureAdapter: PictureAdapter

        fun bind(position:Int,followItem:FollowItem){
            Glide.with(context).load(followItem.user_image).into(userImage)
            userName.text = followItem.user_name
            personalSignature.text = followItem.personal_signature

            //获取followItem. userName .的相关图片
            //这里随便插入点数据

            val pictureItemList = ArrayList<PictureItem>()
            pictureItemList.add(PictureItem(R.drawable.example,true))
            pictureItemList.add(PictureItem(R.drawable.example,false))
            pictureItemList.add(PictureItem(R.drawable.example,false))
            pictureItemList.add(PictureItem(R.drawable.example,true))
            pictureItemList.add(PictureItem(R.drawable.example,false))
            pictureItemList.add(PictureItem(R.drawable.example,true))

            val layoutManager = LinearLayoutManager(context)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL

            innerRecyclerView.layoutManager = layoutManager
            pictureAdapter = PictureAdapter(context,pictureItemList)
            innerRecyclerView.adapter = pictureAdapter

            userName.setOnClickListener {
                val intent = Intent(context,DynamicActivity::class.java)
                context.startActivity(intent)
            }
            userImage.setOnClickListener {
                val intent = Intent(context,DynamicActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_follow,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return followItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val followItem = followItemList[position]
        //Glide.with(context).load(followItem.user_image).into(holder.userImage)
        //holder.userName.text = followItem.user_name
        //holder.personalSignature.text = followItem.personal_signature

        holder.bind(position,followItemList[position])
    }
}