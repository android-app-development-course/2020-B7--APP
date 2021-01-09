package com.example.dacnce.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobRelation
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import com.bumptech.glide.Glide
import com.example.dacnce.DanceApplication
import com.example.dacnce.R
import com.example.dacnce.activity.PersonDynamicActivity
import com.example.dacnce.bean.FollowItemNoPic
import com.example.dacnce.bean.User
import com.example.dacnce.utils.NetworkUtils
import com.example.dacnce.utils.SPUtils
import de.hdodenhof.circleimageview.CircleImageView
import es.dmoral.toasty.Toasty

class FollowAdapterWithUpdate(
    val context: Context,
    private val followItemNoPicList: ArrayList<FollowItemNoPic>,
    val activity: Activity
):RecyclerView.Adapter<FollowAdapterWithUpdate.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val ivIcon: CircleImageView = view.findViewById(R.id.iv_icon)
        val tvUserName: TextView = view.findViewById(R.id.tv_username)
        val tvMessage: TextView = view.findViewById(R.id.tv_message)
        val tvCancelFollow: TextView = view.findViewById(R.id.tv_cancel_follow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_myfollow,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return followItemNoPicList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myFollowItem = followItemNoPicList[position]

        //帖子图片
        Glide.with(context)
            .load(NetworkUtils.PIC_PRE_PATH + "/images" + myFollowItem.icon)
            .placeholder(R.drawable.nav_icon)
            .into(holder.ivIcon)
        //Glide.with(context).load(myFollowItem.icon).into(holder.ivIcon)
        holder.tvUserName.text = myFollowItem.username
        holder.tvMessage.text = myFollowItem.signature

        holder.tvCancelFollow.setOnClickListener {
            val userDelete = User(null,null,null,null,null,null,null,null,null)
            userDelete.objectId = myFollowItem.followUserObjectId
            val relation = BmobRelation()
            relation.remove(userDelete)
            val useNow = BmobUser.getCurrentUser(User::class.java)
            useNow.follows = relation
            useNow.update(object :UpdateListener(){
                override fun done(p0: BmobException?) {
                    if(p0 == null){
                        activity.runOnUiThread{
                            followItemNoPicList.removeAt(position)
                            notifyDataSetChanged()
                            Toasty.info(context,"取消关注成功",Toast.LENGTH_SHORT).show()
                            if(NetworkUtils.isConnected()){
                                if(BmobUser.getCurrentUser(User::class.java) != null &&
                                    SPUtils[DanceApplication.context, "isLogin", false] == true){
                                    if(SPUtils[DanceApplication.context, "follow_cal", false] == true){
                                        val count = SPUtils.get(DanceApplication.context,"follow_count",0).toString().toInt() - 1
                                        SPUtils.put(DanceApplication.context,"follow_count",count)
                                    }
                                }
                            }
                        }
                    }else{
                        Log.i("bmobError","失败${p0.message}")
                    }
                }
            })
        }

        holder.tvUserName.setOnClickListener {
            val intent = Intent(context, PersonDynamicActivity::class.java)
            intent.putExtra("userObjectId",myFollowItem.followUserObjectId)
            context.startActivity(intent)
        }

        holder.ivIcon.setOnClickListener {
            val intent = Intent(context, PersonDynamicActivity::class.java)
            intent.putExtra("userObjectId",myFollowItem.followUserObjectId)
            context.startActivity(intent)
        }

        holder.tvMessage.setOnClickListener {
            val intent = Intent(context, PersonDynamicActivity::class.java)
            intent.putExtra("userObjectId",myFollowItem.followUserObjectId)
            context.startActivity(intent)
        }
    }
}