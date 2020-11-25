package com.example.dacnce.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dacnce.R

class LoginActivity : AppCompatActivity() {

    private var choice: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        choice = intent.getIntExtra("choice",0)
        if(choice == 0){
            setContentView(R.layout.activity_loginbymessage)
        }else{
            setContentView(R.layout.activity_loginbypassword)
        }
    }
}