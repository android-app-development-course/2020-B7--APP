package com.example.dacnce.bean

data class FollowItem(
    val user_image:String,
    val user_name:String,
    val personal_signature:String,
    val pictureList: ArrayList<FollowChildItem>,
    val userObjectId: String,
    val user: User
)
