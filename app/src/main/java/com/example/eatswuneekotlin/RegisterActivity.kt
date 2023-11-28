package com.example.eatswuneekotlin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eatswuneekotlin.server.AccountRegisterDto
import com.example.eatswuneekotlin.server.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {
    private lateinit var idInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var nickname: EditText

    private lateinit var is_id_duplicated: Button
    private lateinit var is_nickname_duplicated: Button
    private lateinit var registerBtn: Button
    private lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val masterApp = MasterApplication()
        masterApp.createRetrofit(this@RegisterActivity)

        val service = masterApp.serviceApi

        idInput = findViewById(R.id.idInput)
        passwordInput = findViewById(R.id.join_pw)
        nickname = findViewById(R.id.nickInput)

        confirmPassword = findViewById(R.id.join_pwck)
        is_id_duplicated = findViewById(R.id.is_id)
        is_nickname_duplicated = findViewById(R.id.is_nick)

        registerBtn = findViewById(R.id.join_button)
        loginBtn = findViewById(R.id.register_login_btn)

        loginBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        })

        registerBtn.setOnClickListener(View.OnClickListener {
            if (is_id_duplicated.text.toString() == "중복확인") {
                Toast.makeText(this@RegisterActivity, "아이디 중복확인을 해 주세요.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else if (is_nickname_duplicated.text.toString() == "중복확인") {
                Toast.makeText(this@RegisterActivity, "닉네임 중복확인을 해 주세요.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else if (TextUtils.isEmpty(passwordInput.text.toString())) {
                Toast.makeText(this@RegisterActivity, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else if (TextUtils.isEmpty(confirmPassword.text.toString())) {
                Toast.makeText(this@RegisterActivity, "비밀번호 확인을 입력해 주세요.", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            } else {

                if (passwordInput.text.toString() == confirmPassword.text.toString()) {

                    val id = idInput.text.toString()
                    val pw = passwordInput.text.toString()
                    val name = nickname.text.toString()
                    val account = AccountRegisterDto(id, name, pw)

                    service.postRegister(account).enqueue(object : Callback<Result> {
                        override fun onResponse(call: Call<Result>, response: Response<Result>) {
                            if (!response.isSuccessful) {
                                Log.e("연결이 비정상적 : ", "error code : " + response.code())
                            } else {
                                Toast.makeText(this@RegisterActivity, "가입되었습니다.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                            }
                        }

                        override fun onFailure(call: Call<Result>, t: Throwable) {}
                    })
                } else {
                    Toast.makeText(this@RegisterActivity, "비밀번호가 동일하지 않습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        // 아이디 중복 체크
        is_id_duplicated.setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(idInput.text.toString())) {
                Toast.makeText(this@RegisterActivity, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else {
                val loginId = idInput.text.toString()
                service.isIdDuplicated(loginId).enqueue(object : Callback<Result?> {
                    override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                        val result = response.body()
                        val data = result!!.data
                        if (data!!.isIs_duplicated) {
                            Toast.makeText(this@RegisterActivity, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            is_id_duplicated.setBackgroundResource(R.drawable.order_list_btn_unclickable)
                            is_id_duplicated.setTextColor(Color.WHITE)
                            is_id_duplicated.text = "확인 완료"
                        }
                        return
                    }

                    override fun onFailure(call: Call<Result?>, t: Throwable) {}
                })
            }
        })

        is_nickname_duplicated.setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(nickname.text.toString())) {
                Toast.makeText(this@RegisterActivity, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else {
                val name = nickname.text.toString()
                service.isNicknameDuplicated(name).enqueue(object : Callback<Result?> {
                    override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                        val result = response.body()
                        val data = result!!.data

                        if (data.isIs_duplicated) {
                            Toast.makeText(this@RegisterActivity, "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            is_nickname_duplicated.setBackgroundResource(R.drawable.order_list_btn_unclickable)
                            is_nickname_duplicated.setTextColor(Color.WHITE)
                            is_nickname_duplicated.text = "확인 완료"
                        }
                        return
                    }

                    override fun onFailure(call: Call<Result?>, t: Throwable) {}
                })
            }
        })
    }
}