package com.example.eatswuneekotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.eatswuneekotlin.server.login.Utils

class StartActivity : AppCompatActivity() {

    private lateinit var login: Button
    private lateinit var join: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        Utils.init(applicationContext)

        val masterApp = MasterApplication()
        masterApp.createRetrofit(this@StartActivity)

        login = findViewById(R.id.start_login_btn)
        join = findViewById(R.id.start_register_button)

        login.setOnClickListener {
            val intent = Intent(this@StartActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        join.setOnClickListener {
            val intent = Intent(this@StartActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}