package com.example.eatswuneekotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {

    lateinit var login: Button
    lateinit var register: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        login = findViewById(R.id.start_login_btn)
        register = findViewById(R.id.start_register_btn)

        login.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@StartActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        })

        register.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@StartActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        })
    }
}