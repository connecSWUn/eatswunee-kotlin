package com.example.eatswuneekotlin.community

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.eatswuneekotlin.MasterApplication
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.server.RetrofitClient
import com.example.eatswuneekotlin.server.ServiceApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Friend_WriteActivity : AppCompatActivity() {
    private lateinit var start_time_btn: Button
    private lateinit var end_time_btn: Button
    private lateinit var done: Button
    private lateinit var cancel: Button

    private lateinit var edit: Button

    private lateinit var article_title: EditText
    private lateinit var article_content: EditText

    private lateinit var spot: Spinner
    private lateinit var title: String
    private lateinit var recruitStatus: String
    private lateinit var start_time: String
    private lateinit var end_time: String
    private lateinit var recruit_spot: String
    private lateinit var content: String

    private val writer_id: Long = 1

    val masterApp = MasterApplication()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_write)

        masterApp.createRetrofit(this@Friend_WriteActivity)
        val service = masterApp.serviceApi

        spot = findViewById<View>(R.id.spinner_spot) as Spinner
        start_time_btn = findViewById(R.id.start_time)
        end_time_btn = findViewById(R.id.end_time)
        done = findViewById(R.id.button_done)
        cancel = findViewById(R.id.button_cancel)
        article_title = findViewById(R.id.editText_title)
        article_content = findViewById(R.id.write_content)

        edit = findViewById(R.id.button_edit)
        edit.setOnClickListener(editBtnOnClickListener())

        done.setOnClickListener(doneBtnOnClickListener())
        start_time_btn.setOnClickListener(startOnClickListener())
        end_time_btn.setOnClickListener(endOnClickListener())

        val intent = intent
        if (intent.extras!!.getBoolean("edit")) {
            val postId = intent.extras!!.getLong("postId")
            done.visibility = View.GONE
            edit.visibility = View.VISIBLE

            service.getData("recruit", postId)?.enqueue(object : Callback<Result?> {
                override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                    val result = response.body()
                    val data = result!!.data

                    Log.d("retrofit", "Data fetch success")

                    article_title.setText(data?.title)
                    article_title.isEnabled = false

                    article_content.setText(data?.content)

                    when (data.spot) {
                        "gusia" -> { spot.setSelection(1) }
                        "shalom" -> { spot.setSelection(2) }
                        "nuri" -> { spot.setSelection(4) }
                        "fiftieth" -> { spot.setSelection(0) }
                        "gyo" -> { spot.setSelection(3) }
                    }
                    spot.isEnabled = false

                    start_time_btn.text = data?.start_time
                    end_time_btn.text = data?.end_time
                    start_time_btn.isEnabled = false
                    end_time_btn.isEnabled = false
                }

                override fun onFailure(call: Call<Result?>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
        cancel.setOnClickListener { finish() }
    }

    inner class editBtnOnClickListener : View.OnClickListener {
        override fun onClick(p0: View?) {
            val postId = intent.extras!!.getLong("postId")
            content = article_content!!.text.toString()

            val articleEdit = article_edit(postId, content)

            val service = masterApp.serviceApi

            service.editContent(articleEdit).enqueue(object : Callback<Result> {
                override fun onResponse(call: Call<Result>, response: Response<Result>) {
                    when(response.code()) {
                        200 -> {
                            Toast.makeText(this@Friend_WriteActivity, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                            val intent =
                                Intent(this@Friend_WriteActivity, Friend_ViewActivity::class.java)
                            intent.putExtra("recruitId", postId)
                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<Result>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(this@Friend_WriteActivity, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                }

            })

            finish()
            overridePendingTransition(0, 0)
        }

    }

    inner class doneBtnOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            title = article_title!!.text.toString()
            recruitStatus = "CONNECTING"
            start_time = start_time_btn!!.text.toString()
            end_time = end_time_btn!!.text.toString()
            content = article_content!!.text.toString()

            if (spot!!.selectedItem.toString() === "구시아") recruit_spot = "gusia"
            else if (spot!!.selectedItem.toString() === "50주년") recruit_spot = "fiftieth"
            else if (spot!!.selectedItem.toString() === "누리관") recruit_spot = "nuri"
            else if (spot!!.selectedItem.toString() === "샬롬") recruit_spot = "shalom"
            else if (spot!!.selectedItem.toString() === "교직원") recruit_spot = "gyo"

            val article =
                article(title!!, recruitStatus!!, start_time!!, end_time!!, recruit_spot, content!!)

            val service = masterApp.serviceApi

            service.postArticle(article).enqueue(object : Callback<Result> {
                override fun onResponse(call: Call<Result>, response: Response<Result>) {
                    if (response.isSuccessful) {
                        val result = response.body()

                        Log.d("article", "POST Success")

                        val post_id = result!!.data?.post_id
                        Toast.makeText(
                            this@Friend_WriteActivity,
                            "게시글이 등록되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent =
                            Intent(this@Friend_WriteActivity, Friend_ViewActivity::class.java)
                        intent.putExtra("recruitId", post_id)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<Result>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(
                        this@Friend_WriteActivity,
                        "게시글 저장에 실패하였습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

            finish()
            overridePendingTransition(0, 0)

        }
    }

    private inner class startOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val octDialog = TimePickerPopupDialogTwoButton(
                this@Friend_WriteActivity,
                object : TimePickerPopupDialogClickListener {
                    override fun onPositiveClick(setHourValue: Int, setMinuteValue: Int) {
                        Log.d(
                            TAG,
                            "onPositiveClick : " + setHourValue + "시 " + setMinuteValue + "분"
                        )
                        if (setMinuteValue < 10) start_time_btn!!.text =
                            "$setHourValue:0$setMinuteValue" else start_time_btn!!.text =
                            "$setHourValue:$setMinuteValue"
                    }

                    override fun onNegativeClick() {
                        Log.d(TAG, "No click")
                    }
                })

            octDialog.setCanceledOnTouchOutside(false)
            octDialog.setCancelable(true)
            octDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            octDialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            octDialog.show()
        }
    }

    private inner class endOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val octDialog = TimePickerPopupDialogTwoButton(
                this@Friend_WriteActivity,
                object : TimePickerPopupDialogClickListener {
                    override fun onPositiveClick(setHourValue: Int, setMinuteValue: Int) {
                        Log.d(
                            TAG,
                            "onPositiveClick : " + setHourValue + "시 " + setMinuteValue + "분"
                        )
                        if (setMinuteValue < 10) end_time_btn!!.text =
                            "$setHourValue:0$setMinuteValue" else end_time_btn!!.text =
                            "$setHourValue:$setMinuteValue"
                    }

                    override fun onNegativeClick() {
                        Log.d(TAG, "No click")
                    }
                })

            octDialog.setCanceledOnTouchOutside(false)
            octDialog.setCancelable(true)
            octDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            octDialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            octDialog.show()
        }
    }

    companion object {
        var TAG: String? = null
    }
}