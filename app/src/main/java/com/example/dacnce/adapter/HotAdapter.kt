package com.example.dacnce.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.datatype.BmobRelation
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.bumptech.glide.Glide
import com.example.dacnce.DanceApplication
import com.example.dacnce.R
import com.example.dacnce.activity.*
import com.example.dacnce.bean.HotItem
import com.example.dacnce.bean.PictureItem
import com.example.dacnce.bean.User
import com.example.dacnce.utils.NetworkUtils
import com.example.dacnce.utils.SPUtils
import com.example.dacnce.utils.TextViewSuffixWrapper
import com.sackcentury.shinebuttonlib.ShineButton
import de.hdodenhof.circleimageview.CircleImageView
import es.dmoral.toasty.Toasty

class HotAdapter(val context: Context, private val hotItemList:List<HotItem>,val activity: FragmentActivity) :
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
        val followBtn: ImageView = view.findViewById(R.id.followBtn)
        val contentMain: TextView = view.findViewById(R.id.content_main)
        val pOmLayout: LinearLayout = view.findViewById(R.id.picture_or_movie)
        val like: ShineButton = view.findViewById(R.id.like)
        val likeNumber: TextView = view.findViewById(R.id.like_number)
        val reply: ImageView = view.findViewById(R.id.reply)
        val replyNumber: TextView = view.findViewById(R.id.reply_number)
        val share: ImageView = view.findViewById(R.id.share)
        val peopleNumber: TextView = view.findViewById(R.id.people_number)
        val videoCardView: CardView = view.findViewById(R.id.video_cardView)
        val pictureRecyclerView: RecyclerView = view.findViewById(R.id.picture_recyclerView)
        val videoImage: ImageView = view.findViewById(R.id.video_image)

        lateinit var pictureItemList:ArrayList<PictureItem>
        lateinit var dynamicPictureAdapter: DynamicPictureAdapter

        fun bind(position:Int, hotItem: HotItem){

            //帖子图片
            Glide.with(context)
                .load(NetworkUtils.PIC_PRE_PATH + "/images" + hotItem.user_image)
                .placeholder(R.drawable.nav_icon)
                .into(userImage)

            //名称
            userName.text = hotItem.user_name

            //帖子内容
            if(hotItem.inputContent != ""){
                TextViewSuffixWrapper(contentMain).apply wrapper@{
                    this.mainContent = hotItem.inputContent
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

            if(hotItem.isVideo){
                videoCardView.visibility = View.VISIBLE
                pictureRecyclerView.visibility = View.GONE
                Glide.with(context).load(hotItem.imageList?.get(0)?.image.toString()).into(videoImage)

                videoImage.setOnClickListener {
                    val intent= Intent(context,ShowMovActivity::class.java)
                    intent.putExtra("preview", hotItem.imageList?.get(0)?.image)
                    intent.putExtra("url",hotItem.videoItem)
                    context.startActivity(intent)
                }


            }else{
                videoCardView.visibility = View.GONE
                pictureRecyclerView.visibility = View.VISIBLE
                pictureItemList = hotItem.imageList!!
                val layoutManager = GridLayoutManager(context,3)
                pictureRecyclerView.layoutManager = layoutManager
                dynamicPictureAdapter = DynamicPictureAdapter(context,pictureItemList)
                pictureRecyclerView.adapter = dynamicPictureAdapter
            }

            userImage.setOnClickListener {
                val intent = Intent(context,PersonDynamicActivity::class.java)
                intent.putExtra("userObjectId",hotItem.user.objectId)
                context.startActivity(intent)
            }
            userName.setOnClickListener {
                val intent = Intent(context,PersonDynamicActivity::class.java)
                intent.putExtra("userObjectId",hotItem.user.objectId)
                context.startActivity(intent)
            }
            reply.setOnClickListener{
                val intent = Intent(context,CommentActivity::class.java)
                intent.putExtra("objectId",hotItem.postId)
                context.startActivity(intent)
            }
            share.setOnClickListener {
                val intent = Intent(context,ShareActivity::class.java)
                context.startActivity(intent)
            }
            userInfoRL.setOnClickListener {
                val intent = Intent(context,CommentActivity::class.java)
                intent.putExtra("objectId",hotItem.postId)
                context.startActivity(intent)
            }
            userInfoRL2.setOnClickListener {
                val intent = Intent(context,CommentActivity::class.java)
                intent.putExtra("objectId",hotItem.postId)
                context.startActivity(intent)
            }

            if(onItemViewClickLisenter!=null){
                share.setOnClickListener{
                    onItemViewClickLisenter.OnItemClick(share,position)
                }
            }

            like.setOnCheckStateChangeListener { view, checked ->
                if(checked){
                    likeNumber.text =  (likeNumber.text.toString().toInt() + 1).toString()
                }else{
                    likeNumber.text =  (likeNumber.text.toString().toInt() - 1).toString()
                }
            }

            val userQuery: BmobQuery<User> = BmobQuery<User>()
            userQuery.addWhereRelatedTo("follows", BmobPointer(BmobUser.getCurrentUser(User::class.java)))
            userQuery.findObjects(object : FindListener<User>() {
                override fun done(userList: MutableList<User>?, exception: BmobException?) {
                    if(exception == null){
                        //Log.d("runOnUiThread1",exception.toString())
                        if(userList != null){
                            //Log.d("runOnUiThread2",userList.toString())
                            var flag: Boolean = false
                            for(u in userList){
                                if(u.objectId == hotItem.user.objectId){
                                    flag = true
                                }
                                //Log.d("runOnUiThread1",flag.toString())
                                activity.runOnUiThread{
                                    if(flag){
                                        Glide.with(context).load(R.drawable.follow_after).into(followBtn)
                                        hotItemList[position].follow = flag
                                    }
                                    //Log.d("runOnUiThread",flag.toString())
                                }
                            }
                        }
                    }else{
                        //Toast.makeText(DanceApplication.context,exception.message.toString(),Toast.LENGTH_SHORT).show()
                        Log.i("bmobError",exception.message.toString())
                    }
                }
            })

            //followBtn
            followBtn.setOnClickListener {
                if(BmobUser.getCurrentUser(User::class.java) == null||
                    SPUtils.get(DanceApplication.context,"isLogin",false) == false ){
                    Toasty.info(DanceApplication.context,"请登录",Toast.LENGTH_SHORT).show()
                }else{
                    if(!hotItem.follow){
//                        val userNow = BmobUser.getCurrentUser(User::class.java)
//                        val postUser = User(null,null,null,null,null,null,null,null,null)
//                        postUser.objectId = hotItem.user.objectId
//                        val relation = BmobRelation()
//                        relation.add(userNow)
//                        postUser.fans = relation
//                        postUser.update(postUser.objectId,object : UpdateListener(){
//                            override fun done(p0: BmobException?) {
//                                if(p0==null){
//                                    val relate = BmobRelation()
//                                    relate.add(hotItem.user)
//                                    userNow.follows = relate
//                                    userNow.update(object :UpdateListener(){
//                                        override fun done(ex: BmobException?) {
//                                            if(ex == null){
//                                                Log.i("bmobSuccess","多对多关联成功")
//                                                activity.runOnUiThread {
//                                                    Glide.with(context).load(R.drawable.follow_after).into(followBtn)
//                                                }
//                                            }else{
//                                                Log.i("bmobError","失败${ex.message}")
//                                            }
//                                        }
//                                    })
//                                }else{
//                                    Log.i("bmobError","失败${p0.message}")
//                                }
//                            }
//                        })
                        val userNow = BmobUser.getCurrentUser(User::class.java)
                        val relate = BmobRelation()
                        relate.add(hotItem.user)
                        userNow.follows = relate
                        userNow.update(object :UpdateListener(){
                            override fun done(ex: BmobException?) {
                                if(ex == null){
                                    Log.i("bmobSuccess","多对多关联成功")
                                    activity.runOnUiThread {
                                        Glide.with(context).load(R.drawable.follow_after).into(followBtn)
                                        hotItemList[position].follow = true
                                        Toasty.success(context,"关注成功",Toast.LENGTH_SHORT).show()
                                        if(SPUtils.get(DanceApplication.context,"isLogin",false) == true){
                                            val user: BmobUser = BmobUser()
                                            user.username = SPUtils.get(DanceApplication.context,"account","") as String
                                            user.setPassword(SPUtils.get(DanceApplication.context,"password","")as String)
                                            user.login(object: SaveListener<BmobUser>(){
                                                override fun done(p0: BmobUser?, p1: BmobException?) {
                                                    SPUtils.put(DanceApplication.context,"isLogin",true)
                                                    SPUtils.put(DanceApplication.context,"account",SPUtils.get(DanceApplication.context,"account","") as String)
                                                    SPUtils.put(DanceApplication.context,"password",SPUtils.get(DanceApplication.context,"password","")as String)

                                                    val count = SPUtils.get(DanceApplication.context,"follow_count",0).toString().toInt() + 1
                                                    SPUtils.put(DanceApplication.context,"follow_count",count)
                                                }
                                            })
                                        }
                                    }
                                }else{
                                    Log.i("bmobError","失败${ex.message}")
                                }
                            }
                        })
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_hot,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hotItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position,hotItemList[position])
    }
}