package com.example.eatswuneekotlin.mypage

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.eatswuneekotlin.R

class settingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val toolbar = findViewById<View>(R.id.setting_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}