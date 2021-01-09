package com.example.dacnce.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dacnce.R
import com.example.dacnce.fragment.SettingsFragment
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.activity_thumbs.*


class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportFragmentManager.beginTransaction()
            .replace(R.id.content, SettingsFragment())
            .commit()

        //初始化Toolbar
        setSupportActionBar(toolbarSetting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "设置"

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                //toolBar点击返回按钮，销毁单前activity
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}