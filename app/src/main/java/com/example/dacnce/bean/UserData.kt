package com.example.dacnce.bean

import cn.bmob.v3.BmobObject

/**
 * @param user 用户名 关联 User表
 * @param follows_id 关注用户名
 * @param fans_id 粉丝用户名
 * @param like_post_id 点赞帖子id
 * @param history_post_id 历史帖子id
 */
data class UserData(
    val user: User,
    val userObjectId: String,
    val follows_id: String,
    val fans_id: String,
    val like_post_id: String,
    val history_post_id: String
):BmobObject()