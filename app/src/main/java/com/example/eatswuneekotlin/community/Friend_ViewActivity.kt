package com.example.eatswuneekotlin.community

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.Visibility
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.eatswuneekotlin.MasterApplication
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.server.chat.ChatActivity
import com.example.eatswuneekotlin.server.chat.ChatListActivity
import com.google.api.Distribution.BucketOptions.Linear
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class Friend_ViewActivity : AppCompatActivity() {
    var isWriter = false
    private var postId: Long = 0
    private var user_id: Long = 0

    private lateinit var title: TextView
    private lateinit var spot: TextView
    private lateinit var time: TextView
    private lateinit var created_at: TextView
    private lateinit var status: TextView
    private lateinit var name: TextView
    private lateinit var content: TextView

    private lateinit var background: LinearLayout
    private lateinit var inside: LinearLayout
    private lateinit var profile: ImageView
    private lateinit var chat_btn: Button
    private lateinit var chat_list_btn: Button

    private lateinit var status_spinner: Spinner
    private lateinit var spinner_layout: LinearLayout

    val masterApp = MasterApplication()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_view)

        val toolbar = findViewById<View>(R.id.friend_view_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)


        title = findViewById(R.id.view_title)
        spot = findViewById(R.id.view_spot)
        time = findViewById(R.id.view_time)
        created_at = findViewById(R.id.view_createdAt)
        status = findViewById(R.id.view_status)
        name = findViewById(R.id.view_name)
        content = findViewById(R.id.view_content)
        profile = findViewById(R.id.view_profile)

        chat_btn = findViewById(R.id.chat_btn)
        chat_btn.setOnClickListener(chatOnClickListener())
        chat_list_btn = findViewById(R.id.chat_list_btn)

        background = findViewById(R.id.friend_view_bottom_back)
        inside = findViewById(R.id.friend_view_bottom_in)

        status_spinner = findViewById(R.id.spinner)
        spinner_layout = findViewById(R.id.status_spinner)


        val intent = intent
        postId = intent.extras!!.getLong("recruitId")

        masterApp.createRetrofit(this@Friend_ViewActivity)
        val service = masterApp.serviceApi

        status_spinner.adapter = ArrayAdapter.createFromResource(this, R.array.spinner_array_status, android.R.layout.simple_spinner_dropdown_item)

        service.getProfile().enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val result = response.body()
                val data = result!!.data
                user_id = data.user_id
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {}
        })

        init(postId)
        chat_list_btn.setOnClickListener(chatListOnClickListener())

        status_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    // 찾는 중
                    0 -> {
                        var status = article_status(user_id, "ONGOING")
                        service.editStatus(status).enqueue(object : Callback<Result> {
                            override fun onResponse(
                                call: Call<Result>,
                                response: Response<Result>,
                            ) {
                            }
                            override fun onFailure(call: Call<Result>, t: Throwable) {
                            }

                        })
                    }

                    // 연락 중
                    1 -> {
                        var status = article_status(user_id, "CONNECTING")
                        service.editStatus(status).enqueue(object : Callback<Result> {
                            override fun onResponse(
                                call: Call<Result>,
                                response: Response<Result>,
                            ) {
                            }
                            override fun onFailure(call: Call<Result>, t: Throwable) {
                            }

                        })
                    }

                    // 구함
                    2 -> {
                        var status = article_status(user_id, "COMPLETED")
                        service.editStatus(status).enqueue(object : Callback<Result> {
                            override fun onResponse(
                                call: Call<Result>,
                                response: Response<Result>,
                            ) {
                            }
                            override fun onFailure(call: Call<Result>, t: Throwable) {
                            }

                        })
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun init(postId: Long) {
        val service = masterApp.serviceApi

        service!!.getData("recruit", postId)?.enqueue(object : Callback<Result?> {
            override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                val result = response.body()
                val data = result!!.data

                Log.d("retrofit", "Data fetch success")

                title.text = data.title

                when (data.spot) {
                    "gusia" -> { spot.text = "구시아" }
                    "shalom" -> { spot.text = "샬롬" }
                    "nuri" -> { spot.text = "누리관" }
                    "fiftieth" -> { spot.text = "50주년" }
                    "gyo" -> { spot.text = "교직원" }
                }

                time.text = data.start_time + " - " + data.end_time
                created_at.text = data.created_at
                content.text = data.content
                name.text = data.writers.user_name

                val user_is_writer = data.isUser_is_writer
                if (user_is_writer) {
                    chat_list_btn!!.visibility = View.VISIBLE
                    background!!.visibility = View.GONE
                    invalidateOptionsMenu()

                    status.visibility = View.GONE
                    spinner_layout.visibility = View.VISIBLE

                    chat_list_btn.text = "채팅 목록 (${data.chat_room_number})"

                    when (data.recruit_status) {
                        "ONGOING" -> status_spinner.setSelection(0)
                        "CONNECTING" -> status_spinner.setSelection(1)
                        "COMPLETED" -> status_spinner.setSelection(2)
                    }

                    DownloadFilesTask().execute(data?.writers?.user_profile_url)
                } else {

                    when (data?.recruit_status) {
                        "ONGOING" -> {
                            status!!.text = "찾는 중이군요!"
                            status!!.setBackgroundResource(R.drawable.com_finding_chat_theme)
                            background!!.setBackgroundColor(resources.getColor(R.color.finding))
                            inside!!.setBackgroundResource(R.drawable.com_finding_theme_bottom_s)

                        }
                        "CONNECTING" -> {
                            status!!.text = "연락 중이군요!"
                            status!!.setBackgroundResource(R.drawable.com_talking_chat_theme)
                            background!!.setBackgroundColor(resources.getColor(R.color.talking))
                            inside!!.setBackgroundResource(R.drawable.com_talking_theme_bottom_s)

                        }
                        "COMPLETED" -> {
                            status!!.text = "이미 구했군요!"
                            status!!.setBackgroundResource(R.drawable.com_done_chat_theme)
                            background!!.setBackgroundColor(resources.getColor(R.color.done))
                            inside!!.setBackgroundResource(R.drawable.com_done_theme_bottom_s)

                        }
                    }

                    DownloadFilesTask().execute(data?.writers?.user_profile_url)
                }
            }

            override fun onFailure(call: Call<Result?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (chat_list_btn!!.visibility == View.VISIBLE) {
            val inflater = menuInflater
            inflater.inflate(R.menu.menu_article, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.edit -> {
                val intent = Intent(this@Friend_ViewActivity, Friend_WriteActivity::class.java)
                intent.putExtra("edit", true)
                intent.putExtra("postId", postId)
                startActivity(intent)
                return true
            }
            R.id.delete -> {
                // 게시글 삭제
                val service = masterApp.serviceApi

                service!!.postDelete(postId)?.enqueue(object : Callback<Result?> {
                    override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@Friend_ViewActivity, "삭제되었습니다.", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<Result?>, t: Throwable) {
                        t.printStackTrace()
                    }
                })

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    internal inner class DownloadFilesTask : AsyncTask<String?, Void?, Bitmap?>() {
        override fun doInBackground(vararg strings: String?): Bitmap? {
            var bmp: Bitmap? = null
            try {
                val img_url = strings[0] //url of the image
                val url = URL(img_url)
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return bmp
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: Bitmap?) {
            // doInBackground 에서 받아온 total 값 사용 장소
            profile!!.setImageBitmap(result)
        }
    }

    private inner class chatOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val service = masterApp.serviceApi

            service!!.getExist(postId)?.enqueue(object : Callback<Result?> {
                override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                    val result = response.body()
                    val data = result!!.data
                    if (data != null) {
                        if (data.isExist_chatroom) {

                            val intent = Intent(this@Friend_ViewActivity, ChatActivity::class.java)
                            intent.putExtra(
                                "chatRoomId",
                                java.lang.Long.valueOf("${user_id}0$postId")
                            )
                            startActivity(intent)

                        } else {

                            service!!.makeChat(postId)?.enqueue(object : Callback<Result?> {
                                override fun onResponse(
                                    call: Call<Result?>,
                                    response: Response<Result?>
                                ) {
                                    val result = response.body()
                                    val data = result!!.data
                                    val chat_id = data?.chat_room_id
                                    val intent =
                                        Intent(this@Friend_ViewActivity, ChatActivity::class.java)
                                    intent.putExtra("chatRoomId", chat_id)
                                    startActivity(intent)
                                }

                                override fun onFailure(call: Call<Result?>, t: Throwable) {}
                            })

                        }
                    }
                }

                override fun onFailure(call: Call<Result?>, t: Throwable) {}
            })
        }
    }

    private inner class chatListOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val intent = Intent(this@Friend_ViewActivity, ChatListActivity::class.java)
            startActivity(intent)
        }
    }
}