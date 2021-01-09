package com.example.dacnce.utils

import android.content.Context
import android.util.AttributeSet
import cn.jzvd.JZDataSource
import cn.jzvd.JZMediaInterface
import cn.jzvd.JZMediaSystem
import cn.jzvd.JzvdStd

class MyJzvdStd : JzvdStd {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    /**
     * 左右两边不会有明显的黑边
     */
    override fun onVideoSizeChanged(width: Int, height: Int) {
        if(textureView!=null){
            textureView.setVideoSize(textureViewContainer.width,textureViewContainer.height)
        }

    }
}