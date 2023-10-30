package com.example.eatswuneekotlin.server.chat

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.server.RetrofitClient
import com.example.eatswuneekotlin.server.ServiceApi
import com.example.eatswuneekotlin.server.messages
import okhttp3.*

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ChatActivity : AppCompatActivity() {
    private var mRecyclerView: RecyclerView? = null
    private var adapter: chatAdapter? = null
    private var retrofitClient: RetrofitClient? = null
    private var client: OkHttpClient? = null
    private var ws: WebSocket? = null
    private var serviceApi: ServiceApi? = null
    var chatRoomId: Long = 0
    private var user_id: String? = null
    private val messageType: String? = null
    private var isSend = 0
    var title: TextView? = null
    var date: TextView? = null
    var spot: TextView? = null
    var time: TextView? = null
    var status: TextView? = null
    var nickname: TextView? = null
    var start_message: TextView? = null
    var message: EditText? = null
    var sendBtn: ImageButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        retrofitClient = RetrofitClient.Companion.instance
        serviceApi = RetrofitClient.Companion.serviceApi
        client = OkHttpClient()
        val request: Request = OkHttpClient.Builder()
            .url("ws://43.201.201.163:8080/ws/chat")
            .build()
        val listener: WebSocketListener = WebSocketListener()
        ws = client!!.newWebSocket(request, listener)
        val toolbar = findViewById<View>(R.id.chat_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)
        title = findViewById(R.id.chat_title)
        date = findViewById(R.id.chat_date)
        spot = findViewById(R.id.chat_spot)
        time = findViewById(R.id.chat_time)
        status = findViewById(R.id.chat_status)
        nickname = findViewById(R.id.nickname)
        start_message = findViewById(R.id.start_message)
        message = findViewById(R.id.chat_box)
        sendBtn = findViewById(R.id.send_btn)
        val intent = intent
        chatRoomId = intent.extras!!.getLong("chatRoomId")

        // RecyclerView
        mRecyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        mRecyclerView!!.addItemDecoration(RecyclerViewDecoration(50))
        sendBtn.setOnClickListener(sendOnClickListener())
        serviceApi.getProfile().enqueue(object : Callback<Result?> {
            override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                val result = response.body()
                val data = result.getData()
                user_id = data.user_id
            }

            override fun onFailure(call: Call<Result?>, t: Throwable) {}
        })
        init(chatRoomId)

        /* initiate recyclerView */mRecyclerView!!.layoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun init(chatRoomId: Long) {
        serviceApi!!.enterChat(chatRoomId).enqueue(object : Callback<Result?> {
            override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                val result = response.body()
                val data = result.getData()
                title.setText(data.recruit_title)
                date.setText(data.recruit_created_at)
                spot.setText(data.recruit_spot)
                time.setText(data.recruit_start_time + "-" + data.recruit_end_time)
                status.setText(data.recruit_status)
                nickname.setText(data.sender_name)
                start_message.setText(data.sender_name + "님과의 대화를 시작합니다.")
                if (data.recruit_status === "ONGOING") {
                    status!!.text = "찾는 중..."
                    status!!.setBackgroundResource(R.drawable.community_state_finding)
                } else if (data.recruit_status === "CONNECTING") {
                    status!!.text = "연락 중..."
                    status!!.setBackgroundResource(R.drawable.community_state_talking)
                } else if (data.recruit_status === "COMPLETED") {
                    status!!.text = "구했어요!"
                    status!!.setBackgroundResource(R.drawable.community_state_done)
                }

                /* initiate adapter */adapter =
                    chatAdapter(data.sender_name, data.messagesList, applicationContext)
                mRecyclerView!!.adapter = adapter
                mRecyclerView!!.scrollToPosition(adapter!!.itemCount - 1)
            }

            override fun onFailure(call: Call<Result?>, t: Throwable) {}
        })
    }

    private inner class WebSocketListener : okhttp3.WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
            super.onOpen(webSocket, response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.d("message", text)
            val cal = Calendar.getInstance()
            var year = cal[Calendar.YEAR].toString()
            var month = (cal[Calendar.MONTH] + 1).toString()
            var day = cal[Calendar.DAY_OF_MONTH].toString()
            var hour = cal[Calendar.HOUR].toString()
            var minute = cal[Calendar.MINUTE].toString()
            var second = cal[Calendar.SECOND].toString()
            if (cal[Calendar.YEAR] < 10) {
                year = "0" + cal[Calendar.YEAR]
            }
            if (cal[Calendar.MONTH] + 1 < 10) {
                month = "0" + (cal[Calendar.MONTH] + 1)
            }
            if (cal[Calendar.DAY_OF_MONTH] < 10) {
                day = "0" + cal[Calendar.DAY_OF_MONTH]
            }
            if (cal[Calendar.HOUR] < 10) {
                hour = "0" + cal[Calendar.HOUR]
            }
            if (cal[Calendar.MINUTE] < 10) {
                minute = "0" + cal[Calendar.MINUTE]
            }
            if (cal[Calendar.SECOND] < 10) {
                second = "0" + cal[Calendar.SECOND]
            }
            val created_at = (year + "." + month + "." + day + " " + hour + ":"
                    + minute + ":" + second)
            val messages = messages(created_at, "sender", text, true)
            adapter!!.addChat(messages)
            mRecyclerView!!.scrollToPosition(adapter!!.itemCount - 1)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            webSocket.close(Companion.NORMAL_CLOSURE_STATUS, null)
            webSocket.cancel()
            Log.d("WebSocketConnection", "Closing : $code / $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
            super.onFailure(webSocket, t, response)
            Log.d("WebSocketConnection", "Error : " + t.message)
        }

        companion object {
            private const val NORMAL_CLOSURE_STATUS = 1000
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                isSend = 0
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class RecyclerViewDecoration(private val divHeight: Int) : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.top = divHeight
        }
    }

    private inner class sendOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            if (isSend == 0) {
                val `object` = JSONObject()
                try {
                    `object`.put("messageType", "ENTER")
                    `object`.put("chatRoomId", java.lang.Long.valueOf(chatRoomId))
                    `object`.put("senderId", java.lang.Long.valueOf(user_id))
                    `object`.put("message", message!!.text)
                    Log.d("chat", `object`.toString())
                    ws!!.send(`object`.toString())
                    val cal = Calendar.getInstance()
                    var year = cal[Calendar.YEAR].toString()
                    var month = (cal[Calendar.MONTH] + 1).toString()
                    var day = cal[Calendar.DAY_OF_MONTH].toString()
                    var hour = cal[Calendar.HOUR].toString()
                    var minute = cal[Calendar.MINUTE].toString()
                    var second = cal[Calendar.SECOND].toString()
                    if (cal[Calendar.YEAR] < 10) {
                        year = "0" + cal[Calendar.YEAR]
                    }
                    if (cal[Calendar.MONTH] + 1 < 10) {
                        month = "0" + (cal[Calendar.MONTH] + 1)
                    }
                    if (cal[Calendar.DAY_OF_MONTH] < 10) {
                        day = "0" + cal[Calendar.DAY_OF_MONTH]
                    }
                    if (cal[Calendar.HOUR] < 10) {
                        hour = "0" + cal[Calendar.HOUR]
                    }
                    if (cal[Calendar.MINUTE] < 10) {
                        minute = "0" + cal[Calendar.MINUTE]
                    }
                    if (cal[Calendar.SECOND] < 10) {
                        second = "0" + cal[Calendar.SECOND]
                    }
                    val created_at = (year + "." + month + "." + day + " " + hour + ":"
                            + minute + ":" + second)
                    val messages = messages(created_at, "sender", message!!.text.toString(), false)
                    adapter!!.addChat(messages)
                    mRecyclerView!!.scrollToPosition(adapter!!.itemCount - 1)
                    message!!.setText("")
                    isSend++
                } catch (e: JSONException) {
                    throw RuntimeException(e)
                }
            } else {
                val `object` = JSONObject()
                try {
                    `object`.put("messageType", "TALK")
                    `object`.put("chatRoomId", java.lang.Long.valueOf(chatRoomId))
                    `object`.put("senderId", java.lang.Long.valueOf(user_id))
                    `object`.put("message", message!!.text)
                    Log.d("chat", `object`.toString())
                    ws!!.send(`object`.toString())
                    val cal = Calendar.getInstance()
                    var year = cal[Calendar.YEAR].toString()
                    var month = (cal[Calendar.MONTH] + 1).toString()
                    var day = cal[Calendar.DAY_OF_MONTH].toString()
                    var hour = cal[Calendar.HOUR].toString()
                    var minute = cal[Calendar.MINUTE].toString()
                    var second = cal[Calendar.SECOND].toString()
                    if (cal[Calendar.YEAR] < 10) {
                        year = "0" + cal[Calendar.YEAR]
                    }
                    if (cal[Calendar.MONTH] + 1 < 10) {
                        month = "0" + (cal[Calendar.MONTH] + 1)
                    }
                    if (cal[Calendar.DAY_OF_MONTH] < 10) {
                        day = "0" + cal[Calendar.DAY_OF_MONTH]
                    }
                    if (cal[Calendar.HOUR] < 10) {
                        hour = "0" + cal[Calendar.HOUR]
                    }
                    if (cal[Calendar.MINUTE] < 10) {
                        minute = "0" + cal[Calendar.MINUTE]
                    }
                    if (cal[Calendar.SECOND] < 10) {
                        second = "0" + cal[Calendar.SECOND]
                    }
                    val created_at = (year + "." + month + "." + day + " " + hour + ":"
                            + minute + ":" + second)
                    val messages = messages(created_at, "sender", message!!.text.toString(), false)
                    adapter!!.addChat(messages)
                    mRecyclerView!!.scrollToPosition(adapter!!.itemCount - 1)
                    message!!.setText("")
                } catch (e: JSONException) {
                    throw RuntimeException(e)
                }
            }
        }
    }
}