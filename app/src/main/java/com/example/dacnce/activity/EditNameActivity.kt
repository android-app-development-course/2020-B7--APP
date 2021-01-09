package com.example.dacnce.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.dacnce.R
import kotlinx.android.synthetic.main.activity_edit_name.*

class EditNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_name)

        //初始化Toolbar
        setSupportActionBar(toolbarEditing)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "修改昵称"

        val nickname = intent.getStringExtra("nickName")
        if(nickname != null){
            name_edit_view.setText(nickname.toString())
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bar_title_with_save,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                //toolBar点击返回按钮，销毁单前activity
                finish()
            }
            R.id.toolbarSave -> {
                //数据库操作
                val intent = Intent()
                Log.i("onActivityResult",name_edit_view.text.toString())
                intent.putExtra("changeNickName",name_edit_view.text.toString())
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}