package com.example.eatswuneekotlin.bistro

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.eatswuneekotlin.R

class order_waitingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_waiting)
        delayedFinish()
    }

    private fun delayedFinish() {
        Handler().postDelayed({ finish() }, 2000)
    }
}