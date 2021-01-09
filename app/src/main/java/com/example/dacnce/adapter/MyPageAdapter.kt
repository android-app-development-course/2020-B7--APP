package com.example.dacnce.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView


class MyPageAdapter(private var imagesUrl: List<String>?, var context: Context) : PagerAdapter() {
    override fun getCount(): Int {
        return if (imagesUrl == null || imagesUrl!!.isEmpty()) 0 else imagesUrl!!.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val url = imagesUrl!![position]
        val photoView = PhotoView(context)
        Glide.with(context)
            .load(url)
            .into(photoView)
        container.addView(photoView)
        photoView.setOnClickListener { }
        return photoView
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        myObj: Any
    ) {
        container.removeView(myObj as View)
    }

    override fun isViewFromObject(
        view: View,
        myObj: Any
    ): Boolean {
        return view === myObj
    }



}