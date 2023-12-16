package com.example.info_5126_mobile_dev_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }
    fun onClickBack(view: View) {
        val intent = Intent(this, UserInputActivity::class.java)
        startActivity(intent)
    }
}