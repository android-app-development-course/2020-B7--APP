package com.example.dacnce.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import com.bumptech.glide.Glide
import com.example.dacnce.R
import com.example.dacnce.adapter.DynamicPictureAdapter
import com.example.dacnce.bean.PictureItem
import com.example.dacnce.bean.PostModel
import com.example.dacnce.comment.LocalServer
import com.example.dacnce.utils.NetworkUtils
import com.example.dacnce.utils.TextViewSuffixWrapper
import com.google.gson.Gson
import com.jidcoo.android.widget.commentview.CommentView
import com.jidcoo.android.widget.commentview.callback.*
import com.jidcoo.android.widget.commentview.defaults.DefaultCommentModel
import com.jidcoo.android.widget.commentview.defaults.DefaultCommentModel.Comment.Reply
import com.jidcoo.android.widget.commentview.defaults.DefaultViewStyleConfigurator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.comment_detail.*
import kotlinx.android.synthetic.main.editor.*
import kotlinx.android.synthetic.main.item_dynamic.*

class CommentActivity : AppCompatActivity() {

    lateinit var commentView: CommentView
    lateinit var gson: Gson
    lateinit var localServer: LocalServer

    var isReply: Boolean = false
    var isChildReply: Boolean = false
    var fid: Long = 0
    var pid: Long = 0
    var cp: Int = 0
    var rp: Int = 0

    var objectId = "0"

    private val handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1 ->                     //commentView.loadFailed(true);//实际网络请求中如果加载失败调用此方法
                    commentView.loadComplete(
                        gson.fromJson(
                            msg.obj as String,
                            DefaultCommentModel::class.java
                        )
                    )
                2 ->                     //commentView.refreshFailed();//实际网络请求中如果加载失败调用此方法
                    commentView.refreshComplete(
                        gson.fromJson(
                            msg.obj as String,
                            DefaultCommentModel::class.java
                        )
                    )
                3 ->                     //commentView.loadFailed();//实际网络请求中如果加载失败调用此方法
                    commentView.loadMoreComplete(
                        gson.fromJson(
                            msg.obj as String,
                            DefaultCommentModel::class.java
                        )
                    )
                4 ->                     //commentView.loadMoreReplyFailed();//实际网络请求中如果加载失败调用此方法
                    commentView.loadMoreReplyComplete(
                        gson.fromJson(
                            msg.obj as String,
                            DefaultCommentModel::class.java
                        )
                    )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.comment_detail)

        //初始化Toolbar
        setSupportActionBar(toolbarComment)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "详情"

        objectId = intent.getStringExtra("objectId").toString()

        if(!(objectId == "0" || objectId == null || objectId == "null")){
            if(NetworkUtils.isConnected()){

                refreshUI()

                gson = Gson();
                localServer = LocalServer(this, "api1");
                commentView = findViewById(R.id.myCommentView);//初始化控件
                commentView.setViewStyleConfigurator(DefaultViewStyleConfigurator(this));
                commentView.callbackBuilder()
                    //下拉刷新回调
                    .setOnPullRefreshCallback(MyOnPullRefreshCallback())
                    //上拉加载更多回调（加载更多评论数据）
                    .setOnCommentLoadMoreCallback(MyOnCommentLoadMoreCallback())
                    //回复数据加载更多回调（加载更多回复）
                    .setOnReplyLoadMoreCallback(MyOnReplyLoadMoreCallback())
                    //评论、回复Item的点击回调（点击事件回调）
                    .setOnItemClickCallback(MyOnItemClickCallback())
                    //滚动事件回调
                    .setOnScrollCallback(MyOnScrollCallback())
                    //设置完成后必须调用CallbackBuilder的buildCallback()方法，否则设置的回调无效
                    .buildCallback();

                button.setOnClickListener{
                    val userStr = user.text.toString()
                    val data = editor.text.toString()
                    if (!userStr.isEmpty() && !data.isEmpty()) {
                        if (isReply && isChildReply) {
                            //现在需要构建一个回复数据实体类
                            val reply = Reply()
                            reply.setKid(fid)
                            reply.setReplierName(userStr)
                            reply.setReply(data)
                            reply.setDate(System.currentTimeMillis())
                            reply.setPid(pid)
                            commentView.addReply(reply, cp)
                        } else if (isReply && !isChildReply) {
                            //现在需要构建一个回复数据实体类
                            val reply = Reply()
                            reply.setKid(fid)
                            reply.setReplierName(userStr)
                            reply.setReply(data)
                            reply.setDate(System.currentTimeMillis())
                            reply.setPid(0)
                            commentView.addReply(reply, cp)
                        } else {
                            val comment = DefaultCommentModel.Comment()
                            comment.setFid(System.currentTimeMillis())
                            comment.setId(comment.getFid() + 1)
                            comment.setDate(comment.getFid())
                            comment.setPid(0)
                            comment.setPosterName(userStr)
                            comment.setComment(data)
                            commentView.addComment(comment)
                        }
                    } else {
                        //Toast.makeText(this, "用户名和内容都不能为空", Toast.LENGTH_LONG).show()
                    }
                }
                load(1, 1);
            }else{
                Toasty.error(this,"没有网络连接",Toast.LENGTH_SHORT).show()
            }
        }

    }

    //初始化上部分UI数据s
    private fun refreshUI(){
        val bmobQuery: BmobQuery<PostModel> = BmobQuery()
        bmobQuery.include("user")
        bmobQuery.getObject(objectId,object:QueryListener<PostModel>(){
            override fun done(post: PostModel?, ex: BmobException?) {
                if(ex == null){
                    if(post!=null){
                        runOnUiThread{
                            //Log.d("objectId",post.toString())
                            var isVideo = false
                            var videoPath = "NoPath"

                            if (post.pic_or_mov == "mov") {
                                isVideo = true
                                videoPath = post.post_mov_path
                            }

                            val picItemList = ArrayList<PictureItem>()
                            val picPaths = post.post_pic_path.split(",")
                            if (picPaths.isNotEmpty()) {
                                for (i in 0 until picPaths.size - 1) {
                                    picItemList.add(PictureItem(picPaths[i], isVideo,videoPath))
                                }
                            }

                            //Log.d("CommentActivity",NetworkUtils.PIC_PRE_PATH + "/images" + post.user.image_url)
                            Glide.with(this@CommentActivity)
                                .load(NetworkUtils.PIC_PRE_PATH + "/images" + post.user.image_url)
                                .placeholder(R.drawable.nav_icon)
                                .into(user_image)

                            user_name.text = post.user.user_nickname

                            if(post.post_content != ""){
                                TextViewSuffixWrapper(content_main).apply wrapper@{
                                    this.mainContent = post.post_content
                                    this.suffix = "...查看更多"
                                    this.suffix?.apply {
                                        suffixColor("...".length, this.length, R.color.gray2, listener = View.OnClickListener { view ->
                                        })
                                    }
                                    this.transition?.duration = 500
                                    collapse(false)
                                    this.textView.setOnClickListener {
                                        this@wrapper.toggle()
                                    }
                                }
                            }else{
                                content_main.visibility = View.GONE
                            }
                            lateinit var dynamicPictureAdapter: DynamicPictureAdapter
                            if(isVideo){
                                video_cardView.visibility = View.VISIBLE
                                picture_recyclerView.visibility = View.GONE
                                Glide.with(this@CommentActivity).load(picItemList[0].image.toString()).into(video_image)

                                video_cardView.setOnClickListener{
                                    val intent= Intent(this@CommentActivity,ShowMovActivity::class.java)
                                    intent.putExtra("preview", picItemList[0].image)
                                    intent.putExtra("url",picItemList[0].videoUrl)
                                    startActivity(intent)
                                }

                            }else{
                                video_cardView.visibility = View.GONE
                                picture_recyclerView.visibility = View.VISIBLE
                                val layoutManager = GridLayoutManager(this@CommentActivity,3)
                                picture_recyclerView.layoutManager = layoutManager
                                dynamicPictureAdapter = DynamicPictureAdapter(this@CommentActivity,picItemList)
                                picture_recyclerView.adapter = dynamicPictureAdapter
                            }

                            like.setOnCheckStateChangeListener { view, checked ->
                                if(checked){
                                    like_number.text =  (like_number.text.toString().toInt() + 1).toString()
                                }else{
                                    like_number.text =  (like_number.text.toString().toInt() - 1).toString()
                                }
                            }
                        }
                    }
                }else{

                    Toast.makeText(this@CommentActivity, ex.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun load(code: Int, handlerId: Int) {
        localServer[code, handler, handlerId]
    }

    /**
     * 下拉刷新回调类
     */
    inner class MyOnPullRefreshCallback: OnPullRefreshCallback{
        override fun refreshing() {
            load(1, 2);
        }

        override fun complete() {
            //加载完成后的操作
        }

        override fun failure(msg: String?) {
            if (msg != null)
                Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
        }

    }

    /**
     * 上拉加载更多回调类
     */
    inner class MyOnCommentLoadMoreCallback: OnCommentLoadMoreCallback {
        override fun complete() {
            //加载完成后的操作
        }

        override fun loading(currentPage: Int, willLoadPage: Int, isLoadedAllPages: Boolean) {
            //因为测试数据写死了，所以这里的逻辑也是写死的
            if (!isLoadedAllPages) {
                if (willLoadPage == 2) {
                    load(2, 3)
                } else if (willLoadPage == 3) {
                    load(3, 3)
                }
            }
        }

        override fun failure(msg: String?) {
            if (msg != null)
                Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 回复加载更多回调类
     */
    inner class MyOnReplyLoadMoreCallback: OnReplyLoadMoreCallback<DefaultCommentModel.Comment.Reply>{
        override fun complete() {
            //加载完成后的操作
        }

        override fun loading(reply: DefaultCommentModel.Comment.Reply?, willLoadPage: Int) {
            //因为测试数据写死了，所以这里的逻辑也是写死的
            //在默认回复数据模型中，kid作为父级索引
            //为了扩展性，把对应的具体模型传了出来，可根据具体需求具体使用
            if (reply!!.getKid() == 1593699394031L) {
                load(4, 4)
            } else {
                if (willLoadPage == 2) {
                    load(5, 4)
                } else if (willLoadPage == 3) {
                    load(6, 4)
                }
            }
        }

        override fun failure(msg: String?) {
            if (msg != null)
                Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 点击事件回调
     */
    inner class MyOnItemClickCallback:
        OnItemClickCallback<DefaultCommentModel.Comment, DefaultCommentModel.Comment.Reply>{
        override fun commentItemOnClick(
            position: Int,
            comment: DefaultCommentModel.Comment?,
            view: View?
        ) {
            isReply = true
            cp = position
            isChildReply = false
            fid = comment!!.getFid()
            editor.setHint("回复@" + comment.getPosterName() + ":")
        }

        override fun replyItemOnClick(
            c_position: Int,
            r_position: Int,
            reply: DefaultCommentModel.Comment.Reply?,
            view: View?
        ) {
            isReply = true
            cp = c_position
            rp = r_position
            isChildReply = true
            fid = reply!!.getKid()
            pid = reply.getId()
            editor.hint = "回复@" + reply.getReplierName() + ":"
        }
    }

    inner class MyOnScrollCallback: OnScrollCallback{
        override fun onScroll(
            view: AbsListView?,
            firstVisibleItem: Int,
            visibleItemCount: Int,
            totalItemCount: Int
        ) {
            isReply = false
        }

        override fun onScrollChange(
            v: View?,
            scrollX: Int,
            scrollY: Int,
            oldScrollX: Int,
            oldScrollY: Int
        ) {

        }

        override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                //toolBar点击返回按钮，销毁单前activity
                finish()
                //Toast.makeText(this,"Finish PersonInfoActivity",Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}