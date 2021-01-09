package com.example.dacnce.bean

import cn.bmob.v3.BmobObject

class PostModel(
    var post_user_id: String = "",
    var post_content: String = "",
    var post_pic_path: String = "",
    var post_mov_path: String = "",
    var pic_or_mov: String = "",
    var user: User
) : BmobObject()