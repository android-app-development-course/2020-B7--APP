package com.example.dacnce.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacnce.R
import com.example.dacnce.activity.CommentActivity
import com.example.dacnce.activity.ShowMovActivity
import com.example.dacnce.bean.DynamicListItem
import com.example.dacnce.bean.PictureItem
import com.example.dacnce.utils.NetworkUtils
import com.example.dacnce.utils.TextViewSuffixWrapper
import com.sackcentury.shinebuttonlib.ShineButton
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.collections.ArrayList

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
        val userInfoRL: RelativeLayout = view.findViewById(R.id.userInfo_rl)

        lateinit var pictureItemList:ArrayList<PictureItem>
        lateinit var dynamicPictureAdapter: DynamicPictureAdapter

        fun bind(position:Int,dynamicListItem: DynamicListItem){
            //Glide.with(context).load(dynamicListItem.userImage).into(userImage)
            Glide.with(context)
                .load(NetworkUtils.PIC_PRE_PATH + "/images" + dynamicListItem.userImage)
                .placeholder(R.drawable.nav_icon)
                .into(userImage)
            userName.text = dynamicListItem.userName
            more.setOnClickListener{
                Toast.makeText(context,"click more",Toast.LENGTH_SHORT).show()
            }
            //contentMain.text = dynamicListItem.inputContent
            if(dynamicListItem.inputContent != ""){
                TextViewSuffixWrapper(contentMain).apply wrapper@{
                    this.mainContent = dynamicListItem.inputContent
                    this.suffix = "...查看更多"
                    this.suffix?.apply {
                        suffixColor("...".length, this.length, R.color.gray2, listener = View.OnClickListener { view ->
                            Toast.makeText(context,"click ${this}",Toast.LENGTH_SHORT).show()
                        })
                    }
                    this.transition?.duration = 500
                    collapse(false)
                    this.textView.setOnClickListener {
                        this@wrapper.toggle()
                    }
                }
            }else{
                contentMain.visibility = View.GONE
            }
            if(dynamicListItem.isVideo){
                videoCardView.visibility = View.VISIBLE
                pictureRecyclerView.visibility = View.GONE
                Glide.with(context).load(dynamicListItem.imageList?.get(0)?.image.toString()).into(videoImage)

                pictureItemList=dynamicListItem.imageList!!

                videoImage.setOnClickListener {
                    val intent= Intent(context,ShowMovActivity::class.java)
                    intent.putExtra("preview", dynamicListItem.imageList[0].image)
                    intent.putExtra("url",dynamicListItem.videoItem)
                    context.startActivity(intent)
                }


            }else{
                videoCardView.visibility = View.GONE
                pictureRecyclerView.visibility = View.VISIBLE
                pictureItemList = dynamicListItem.imageList!!
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

            like.setOnCheckStateChangeListener { view, checked ->
                if(checked){
                    likeNumber.text =  (likeNumber.text.toString().toInt() + 1).toString()
                }else{
                    likeNumber.text =  (likeNumber.text.toString().toInt() - 1).toString()
                }
            }

            userInfoRL.setOnClickListener {
                val intent = Intent(context,CommentActivity::class.java)
                intent.putExtra("objectId",dynamicListItem.postId)
                context.startActivity(intent)
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