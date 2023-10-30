package com.example.eatswuneekotlin.community

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.server.RetrofitClient
import com.example.eatswuneekotlin.server.ServiceApi
import com.example.eatswuneekotlin.server.chat.ChatActivity
import com.example.eatswuneekotlin.server.chat.ChatListActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class friend_viewActivity : AppCompatActivity() {
    private var retrofitClient: RetrofitClient? = null
    private var serviceApi: ServiceApi? = null
    var isWriter = false
    private var postId: Long = 0
    private var user_id: String? = null
    var title: TextView? = null
    var spot: TextView? = null
    var time: TextView? = null
    var created_at: TextView? = null
    var status: TextView? = null
    var name: TextView? = null
    var content: TextView? = null
    var background: LinearLayout? = null
    var inside: LinearLayout? = null
    var profile: ImageView? = null
    var chat_btn: Button? = null
    var chat_list_btn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_view)
        retrofitClient = RetrofitClient.instance
        serviceApi = RetrofitClient.serviceApi
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
        chat_list_btn = findViewById(R.id.chat_list_btn)
        background = findViewById(R.id.friend_view_bottom_back)
        inside = findViewById(R.id.friend_view_bottom_in)
        chat_btn.setOnClickListener(chatOnClickListener())
        val intent = intent
        postId = intent.extras!!.getLong("recruitId")

        serviceApi!!.profile.enqueue(object : Callback<Result?> {
            override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                val result = response.body()
                val data = result!!.data
                user_id = data.user_id
            }

            override fun onFailure(call: Call<Result?>, t: Throwable) {}
        })
        init(postId)
        chat_list_btn.setOnClickListener(chatListOnClickListener())
    }

    private fun init(postId: Long) {
        serviceApi!!.getData("recruit", postId).enqueue(object : Callback<Result?> {
            override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                val result = response.body()
                val data = result!!.data
                Log.d("retrofit", "Data fetch success")
                val user_is_writer = data.isUser_is_writer
                if (user_is_writer) {
                    chat_list_btn!!.visibility = View.VISIBLE
                    background!!.visibility = View.GONE
                    invalidateOptionsMenu()
                }
                title!!.text = data.title
                spot!!.text = data.spot
                time!!.text = data.start_time + " - " + data.end_time
                created_at!!.text = data.created_at
                content!!.text = data.content
                name!!.text = data.writers.user_name
                if (data.recruit_status === "ONGOING") {
                    status!!.text = "찾는 중이군요!"
                    status!!.setBackgroundResource(R.drawable.com_finding_chat_theme)
                    background!!.setBackgroundColor(resources.getColor(R.color.finding))
                    inside!!.setBackgroundResource(R.drawable.com_finding_theme_bottom_s)
                } else if (data.recruit_status === "CONNECTING") {
                    status!!.text = "연락 중이군요!"
                    status!!.setBackgroundResource(R.drawable.com_talking_chat_theme)
                    background!!.setBackgroundColor(resources.getColor(R.color.talking))
                    inside!!.setBackgroundResource(R.drawable.com_talking_theme_bottom_s)
                } else if (data.recruit_status === "COMPLETED") {
                    status!!.text = "이미 구했군요!"
                    status!!.setBackgroundResource(R.drawable.com_done_chat_theme)
                    background!!.setBackgroundColor(resources.getColor(R.color.done))
                    inside!!.setBackgroundResource(R.drawable.com_done_theme_bottom_s)
                }

                // 이미지 주소가 안 되어있음 : 수정 필요
                DownloadFilesTask().execute(data.writers.user_profile_url)
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
                val intent = Intent(this@friend_viewActivity, friend_writeActivity::class.java)
                intent.putExtra("edit", true)
                intent.putExtra("postId", postId)
                startActivity(intent)
                return true
            }
            R.id.delete -> {
                // 게시글 삭제
                serviceApi!!.postDelete(postId).enqueue(object : Callback<Result?> {
                    override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@friend_viewActivity, "삭제되었습니다.", Toast.LENGTH_SHORT)
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
        protected override fun doInBackground(vararg strings: String): Bitmap? {
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
            serviceApi!!.getExist(postId).enqueue(object : Callback<Result?> {
                override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                    val result = response.body()
                    val data = result!!.data
                    if (data.isExist_chatroom == true) {
                        val intent = Intent(this@friend_viewActivity, ChatActivity::class.java)
                        intent.putExtra(
                            "chatRoomId",
                            java.lang.Long.valueOf(user_id + "0" + postId)
                        )
                        startActivity(intent)
                    } else if (data.isExist_chatroom == false) {
                        serviceApi!!.makeChat(postId).enqueue(object : Callback<Result?> {
                            override fun onResponse(
                                call: Call<Result?>,
                                response: Response<Result?>
                            ) {
                                val result = response.body()
                                val data = result!!.data
                                val chat_id = data.chat_room_id
                                val intent =
                                    Intent(this@friend_viewActivity, ChatActivity::class.java)
                                intent.putExtra("chatRoomId", chat_id)
                                startActivity(intent)
                            }

                            override fun onFailure(call: Call<Result?>, t: Throwable) {}
                        })
                    }
                }

                override fun onFailure(call: Call<Result?>, t: Throwable) {}
            })
        }
    }

    private inner class chatListOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val intent = Intent(this@friend_viewActivity, ChatListActivity::class.java)
            startActivity(intent)
        }
    }
}