package com.example.eatswuneekotlin.bistro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.eatswuneekotlin.R

class Order_WaitingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_waiting)

        val order_id: Long = intent.getLongExtra("order_id", 0)

        delayedFinish(order_id)
    }

    private fun delayedFinish(order_id: Long) {
        Handler().postDelayed({
            val intent = Intent(this@Order_WaitingActivity, ProcessedActivity::class.java)
            intent.putExtra("order_id", order_id)
            startActivity(intent)
            finish() }, 1000)
    }
}