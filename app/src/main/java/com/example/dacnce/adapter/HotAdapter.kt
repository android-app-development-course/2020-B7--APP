package com.example.dacnce.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacnce.R
import com.example.dacnce.activity.CommentActivity
import com.example.dacnce.activity.DynamicActivity
import com.example.dacnce.activity.ShareActivity
import com.example.dacnce.bean.HotItem
import com.example.dacnce.comment.DefaultUseInLocalActivity
import com.example.dacnce.utils.TextViewSuffixWrapper
import com.sackcentury.shinebuttonlib.ShineButton
import de.hdodenhof.circleimageview.CircleImageView

class HotAdapter(val context:Context, private val hotItemList:List<HotItem>) :
    RecyclerView.Adapter<HotAdapter.ViewHolder>() {

    public interface OnItemViewClickLisenter{
        fun OnItemClick(view:View,position:Int)
    }

    lateinit var onItemViewClickLisenter: OnItemViewClickLisenter

    public fun setOnItemViewClickListener(onItemViewClickLisenter:OnItemViewClickLisenter) {
        this.onItemViewClickLisenter = onItemViewClickLisenter
    }

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val userInfoRL: RelativeLayout = view.findViewById(R.id.userInfo_rl)
        val userInfoRL2: RelativeLayout = view.findViewById(R.id.userInfo_rl2)
        val userImage: CircleImageView = view.findViewById(R.id.user_image)
        val userName: TextView = view.findViewById(R.id.user_name)
        val more: ImageView = view.findViewById(R.id.more)
        val contentMain: TextView = view.findViewById(R.id.content_main)
        val pOmLayout: LinearLayout = view.findViewById(R.id.picture_or_movie)
        val like: ShineButton = view.findViewById(R.id.like)
        val likeNumber: TextView = view.findViewById(R.id.like_number)
        val reply: ImageView = view.findViewById(R.id.reply)
        val replyNumber: TextView = view.findViewById(R.id.reply_number)
        val share: ImageView = view.findViewById(R.id.share)
        val peopleNumber: TextView = view.findViewById(R.id.people_number)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_hot,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hotItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hotItem = hotItemList[position]
        Glide.with(context).load(hotItem.user_image).into(holder.userImage)
        holder.userName.text = hotItem.user_name
        //holder.contentMain.text = "大赛大大大所大所大所多阿萨德卡吉利金刚wit我跟我姐案件发开发建瓯家家爱手动阀骄傲as解放军奥法金佛爱福家·1爱是奇偶发交付件奥法阿佛法讲奥法金佛奥法骄傲法鸡埃及法经发局"
        TextViewSuffixWrapper(holder.contentMain).apply wrapper@{
            this.mainContent = "这就是街舞，为自己而舞，请不要装酷。I say hei，这就是街舞，为自己而舞，请不要装酷。这就是街舞，为自己而舞，请不要装酷。这就是街舞，为自己而舞，请不要装酷。"
            this.suffix = "...查看更多"
            this.suffix?.apply {
                suffixColor("...".length, this.length, R.color.gray2, listener = View.OnClickListener { view ->
                    Toast.makeText(context,"click ${this}",Toast.LENGTH_SHORT).show()
                })
            }
            this.transition?.duration = 500
            collapse(false)
            this.textView.setOnClickListener {
                Toast.makeText(context,"click view",Toast.LENGTH_SHORT).show()
                this@wrapper.toggle()
            }
        }
        holder.userImage.setOnClickListener {
            val intent = Intent(context,DynamicActivity::class.java)
            context.startActivity(intent)
        }
        holder.userName.setOnClickListener {
            val intent = Intent(context,DynamicActivity::class.java)
            context.startActivity(intent)
        }
        holder.reply.setOnClickListener{
            val intent = Intent(context,CommentActivity::class.java)
            context.startActivity(intent)
        }
        holder.share.setOnClickListener {
            val intent = Intent(context,ShareActivity::class.java)
            context.startActivity(intent)
        }
        holder.userInfoRL.setOnClickListener {
            val intent = Intent(context,CommentActivity::class.java)
            context.startActivity(intent)
        }
        holder.userInfoRL2.setOnClickListener {
            val intent = Intent(context,CommentActivity::class.java)
            context.startActivity(intent)
        }

        if(onItemViewClickLisenter!=null){
            holder.share.setOnClickListener{
                onItemViewClickLisenter.OnItemClick(holder.share,position)
            }
        }


    }
}