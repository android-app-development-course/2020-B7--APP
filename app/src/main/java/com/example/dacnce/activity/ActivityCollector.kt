package com.example.dacnce.activity

import android.app.Activity

/**
 * 单例类
 * 作为Activity的集合
 */
object ActivityCollector {
    private val activities = ArrayList<Activity>()

    //添加
    fun addActivity(activity: Activity){
        activities.add(activity)
    }

    //删除
    fun removeActivity(activity: Activity){
        activities.remove(activity)
    }

    //结束清空
    fun finishAll(){
        for(activity in activities){
            if(!activity.isFinishing){
                activity.finish()
            }
        }
        activities.clear()
    }
}