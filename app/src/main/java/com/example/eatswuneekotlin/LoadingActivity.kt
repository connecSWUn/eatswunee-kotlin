package com.example.eatswuneekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        delayedFinish()
    }

    private fun delayedFinish() {
        Handler().postDelayed({
            val intent = Intent(this@LoadingActivity, StartActivity::class.java)
            startActivity(intent)
            finish() }, 2000)
    }
}