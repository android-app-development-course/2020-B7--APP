package com.example.dacnce.fragment


import com.example.dacnce.activity.PostPictureActivity
import com.example.dacnce.activity.PostVideoActivity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import com.example.dacnce.R
import com.mingle.entity.MenuEntity
import com.mingle.sweetpick.DimEffect
import com.mingle.sweetpick.SweetSheet
import com.mingle.sweetpick.ViewPagerDelegate

class NewFragment: Fragment(),View.OnClickListener {

    private lateinit var rl: RelativeLayout
    private lateinit var mySweetSheet: SweetSheet
    private lateinit var click: Button
    //private lateinit var postPicture: Button
    //private lateinit var postVideo:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new, container, false)

        rl = view.findViewById(R.id.root)
        click = view.findViewById(R.id.click)
        click.setOnClickListener {
//            if (mySweetSheet.isShow)
//                mySweetSheet.dismiss()
//            else
//                mySweetSheet.show()
            if(mySweetSheet == null){
                setupRecyclerView()
                mySweetSheet.show()
            }
            else if(mySweetSheet.isShow){
                mySweetSheet.dismiss()
            }else{
                setupRecyclerView()
                mySweetSheet.show()
            }


        }

        setupRecyclerView()

        return view
    }


//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.activity_post, container, false)
//
//        postPicture = view.findViewById(R.id.postPicture)
//        postVideo = view.findViewById(R.id.postVideo)
//
//        postPicture.setOnClickListener(this)
//        postVideo.setOnClickListener(this)
//
//        return view
//    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun setupRecyclerView(){
        val list = ArrayList<MenuEntity>()
//        for (i in 0 until 5){
//            val menuEntity = MenuEntity()
//            menuEntity.iconId = R.drawable.ic_post_picture
//            menuEntity.title = "图片"
//            list.add(menuEntity)
//        }

        var menuEntity = MenuEntity()
        menuEntity.iconId = R.drawable.ic_post_picture
        menuEntity.title = "图片"
        list.add(menuEntity)

        menuEntity = MenuEntity()
        menuEntity.iconId = R.drawable.ic_post_video
        menuEntity.title = "视频"
        list.add(menuEntity)

        menuEntity = MenuEntity()
        menuEntity.iconId = R.drawable.ic_post_picture
        menuEntity.title = "问答"
        list.add(menuEntity)

        menuEntity = MenuEntity()
        menuEntity.iconId = R.color.white
        menuEntity.title = ""
        list.add(menuEntity)

        menuEntity = MenuEntity()
        menuEntity.iconId = R.drawable.ic_post_cancel
        menuEntity.title = ""
        list.add(menuEntity)

        menuEntity = MenuEntity()
        menuEntity.iconId = R.color.white
        menuEntity.title = ""
        list.add(menuEntity)


        mySweetSheet = SweetSheet(rl)

        mySweetSheet.setMenuList(list)
        mySweetSheet.setDelegate(ViewPagerDelegate())

        mySweetSheet.setBackgroundEffect(DimEffect(10f))

    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }


//    override fun onClick(v: View?) {
//        when (v?.id) {
//            R.id.postPicture -> {
//                val intent = Intent(context, PostPictureActivity::class.java)
//                startActivity(intent)
//            }
//            R.id.postVideo -> {
//                val intent = Intent(context, PostVideoActivity::class.java)
//                startActivity(intent)
//            }
//        }
//    }
}