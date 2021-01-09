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
import com.example.dacnce.adapter.FollowAdapterWithUpdate
import com.example.dacnce.bean.FollowItemNoPic
import com.example.dacnce.bean.User
import com.example.dacnce.utils.NetworkUtils
import com.example.dacnce.utils.SPUtils
import es.dmoral.toasty.Toasty

class ContactsFollowFragment : Fragment() {

    private lateinit var followAdapterWithUpdate: FollowAdapterWithUpdate
    private val followItemListNoPic = ArrayList<FollowItemNoPic>()
    private var count = 0
    private lateinit var contactsFollowRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_contacts_follow, container, false)


        val layoutManager = LinearLayoutManager(activity)
        contactsFollowRecyclerView = view.findViewById(R.id.contacts_follow_rv)
        contactsFollowRecyclerView.layoutManager = layoutManager

        followAdapterWithUpdate = context?.let { activity?.let { it1 -> FollowAdapterWithUpdate(it, followItemListNoPic, it1) } }!!
        contactsFollowRecyclerView.adapter = followAdapterWithUpdate

        if(NetworkUtils.isConnected()){
            refreshFollowItem()
        }else{
            Toasty.error(DanceApplication.context,"无网络连接", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun refreshFollowItem(){
        count = 0
        val userQuery: BmobQuery<User> = BmobQuery()
        userQuery.addWhereRelatedTo("follows", BmobPointer(BmobUser.getCurrentUser(User::class.java)))
        userQuery.findObjects(object : FindListener<User>() {
            override fun done(p0: MutableList<User>?, p1: BmobException?) {
                if(p1 == null){
                    if(p0!=null){
                        activity?.runOnUiThread{
                            for(u in p0){
                                val model = FollowItemNoPic(
                                    u.image_url!!,
                                    u.user_nickname!!,
                                    u.user_signature!!,
                                    u.objectId,
                                    u
                                )
                                count++
                                SPUtils.put(DanceApplication.context,"follow_cal",true)
                                SPUtils.put(DanceApplication.context,"follow_count",count)
                                followItemListNoPic.add(model)
                            }
                            followAdapterWithUpdate.notifyDataSetChanged()
                        }
                    }
                }
            }
        })
    }

}