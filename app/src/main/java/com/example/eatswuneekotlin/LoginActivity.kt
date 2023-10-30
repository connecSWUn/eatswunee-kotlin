package com.example.eatswuneekotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.eatswuneekotlin.server.login.LoginBackendResponse
import com.example.eatswuneekotlin.server.login.UserModel
import com.example.eatswuneekotlin.MainActivity
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var id_input: TextInputEditText
    lateinit var password_input: TextInputEditText
    lateinit var login_btn: Button
    lateinit var join_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        id_input = findViewById(R.id.idInput)
        password_input = findViewById(R.id.passwordInput)
        login_btn = findViewById(R.id.login_btn)
        join_btn = findViewById(R.id.register_button)

        val masterApp = MasterApplication()
        masterApp.createRetrofit(this@LoginActivity)

        val service = masterApp.serviceApi

        login_btn.setOnClickListener {
            val id = id_input.text.toString().trim() // 문자열 공백 제거
            val pw = password_input.text.toString().trim()

            val data = UserModel(id, pw)

            service.userLogin(data).enqueue(object : Callback<LoginBackendResponse> {
                override fun onResponse(
                    call: Call<LoginBackendResponse>,
                    response: Response<LoginBackendResponse>
                ) {
                    Log.d("로그인 통신 성공", response.toString())
                    Log.d("로그인 통신 성공", response.body().toString())

                    when (response.code()) {
                        200 -> {
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        405 -> Toast.makeText(this@LoginActivity, "로그인 정보가 올바르지 않습니다", Toast.LENGTH_SHORT).show()
                        500 -> Toast.makeText(this@LoginActivity, "로그인 실패 : 서버 오류", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginBackendResponse>, t: Throwable) {
                    Log.d("로그인 통신 실패", t.message.toString())
                    Log.d("로그인 통신 실패", "fail")
                }
            })
        }

        join_btn.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
