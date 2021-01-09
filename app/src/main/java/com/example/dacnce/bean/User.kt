package com.example.dacnce.bean

import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobRelation

/**
 * @see cn.bmob.v3.BmobUser.objectId  objectId：云数据唯一标识符, 继承自BmobUser->BmobObject
 * @see cn.bmob.v3.BmobUser.username username:用户名
 * @see cn.bmob.v3.BmobUser.password password:密码
 * @param user_id：用户id
 * @param user_nickname：昵称
 * @param user_sex：性别
 * @param user_bd：生日
 * @param user_signature：签名
 * @param user_dance_type：舞种
 * @param image_url: 头像图片url链接后缀
 * @param follows: 关注的人
 * @param fans: 粉丝
 */
data class User(
    val user_id: Int?,
    var user_nickname: String?,
    var user_sex: String?,
    var user_bd: String?,
    var user_signature: String?,
    val user_dance_type: String?,
    var image_url: String?,
    var follows: BmobRelation?,
    var fans: BmobRelation?
): BmobUser(){

}