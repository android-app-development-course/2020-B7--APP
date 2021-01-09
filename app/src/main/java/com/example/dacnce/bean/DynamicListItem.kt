package com.example.dacnce.bean

class DynamicListItem(
    val postId:String,
    val imageList: ArrayList<PictureItem>?,
    val videoItem:String,
    val userName:String,
    val userImage:String,
    val isVideo:Boolean,
    val videoImage:Int?,
    val inputContent:String,
    val user: User
)