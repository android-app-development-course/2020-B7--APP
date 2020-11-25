package com.example.dacnce.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.dacnce.R
import kotlinx.android.synthetic.main.activity_signature.*

class SignatureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)

        //初始化Toolbar
        setSupportActionBar(toolbarSignature)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "修改个性签名"

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
                Toast.makeText(this,"Finish SignatureActivity", Toast.LENGTH_SHORT).show()
            }
            R.id.toolbarSave -> {
                //数据库操作
                Toast.makeText(this,"保存数据", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}