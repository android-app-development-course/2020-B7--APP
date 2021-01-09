package com.example.dacnce.bean

class HotItem(
    val postId:String,
    val imageList:ArrayList<PictureItem>?,
    val videoItem:String,
    val user_name:String,
    val user_image:String,
    val isVideo:Boolean,
    val videoImage:Int?,
    val inputContent:String,
    val user:User,
    var follow: Boolean
)