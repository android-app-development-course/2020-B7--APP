package com.example.dacnce.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.example.dacnce.DanceApplication
import com.example.dacnce.R
import com.example.dacnce.adapter.FansAdapter
import com.example.dacnce.bean.FansItem
import com.example.dacnce.bean.User
import com.example.dacnce.utils.NetworkUtils
import com.example.dacnce.utils.SPUtils
import es.dmoral.toasty.Toasty

class ContactsFansFragment : Fragment() {
    private lateinit var fansAdapter: FansAdapter
    private val fansItemList = ArrayList<FansItem>()
    private lateinit var contactsFansRecyclerView: RecyclerView
    private var count = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_contacts_fans, container, false)


        val layoutManager = LinearLayoutManager(activity)
        contactsFansRecyclerView=view.findViewById(R.id.contacts_fans_rv)

        contactsFansRecyclerView.layoutManager = layoutManager
        fansAdapter = context?.let { FansAdapter(it,fansItemList) }!!
        contactsFansRecyclerView.adapter = fansAdapter

        if(NetworkUtils.isConnected()){
            refreshFansItem()
        }else{
            Toasty.error(DanceApplication.context,"无网络连接",Toast.LENGTH_SHORT).show()
        }

        return view


    }

    private fun refreshFansItem(){
        count = 0
        val currentUser = BmobUser.getCurrentUser(User::class.java)
        //先获取所有user的objectId
        val bmobQuery: BmobQuery<User> = BmobQuery()
        bmobQuery.findObjects(object :FindListener<User>(){
            override fun done(p0: MutableList<User>?, p1: BmobException?) {
                if(p1 == null){
                    if(p0!=null){
                        for(p in p0){
                            val userQuery: BmobQuery<User> = BmobQuery()
                            userQuery.addWhereRelatedTo("follows", BmobPointer(p))
                            userQuery.findObjects(object :FindListener<User>(){
                                override fun done(userList: MutableList<User>?, exception: BmobException?) {
                                    if(exception == null){
                                        if(userList!=null){
                                            var flag = false
                                            for(u in userList){
                                                if(u.objectId == currentUser.objectId){
                                                    flag = true
                                                    count++
                                                    SPUtils.put(DanceApplication.context,"fans_cal",true)
                                                    SPUtils.put(DanceApplication.context,"fans_count",count)
                                                    break
                                                }
                                            }
                                            if(flag){
                                                activity?.runOnUiThread{
                                                    val model = FansItem(
                                                        p.image_url!!,
                                                        p.user_nickname!!,
                                                        p.user_signature!!,
                                                        p.objectId,
                                                        p
                                                    )
                                                    fansItemList.add(model)
                                                    fansAdapter.notifyDataSetChanged()
                                                }
                                            }
                                        }
                                    }
                                }
                            })
                        }
                    }
                }else{
                    Toast.makeText(DanceApplication.context,"${p1.message}", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }




}