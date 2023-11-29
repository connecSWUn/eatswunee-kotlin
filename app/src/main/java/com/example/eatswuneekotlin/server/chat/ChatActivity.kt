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
import com.example.eatswuneekotlin.MasterApplication
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.server.messages
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ChatActivity : AppCompatActivity() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var adapter: chatAdapter
    private lateinit var client: OkHttpClient
    private lateinit var websocket: WebSocket

    private lateinit var chatRoomId: String
    private var user_id: Long = 0
    private lateinit var user_name: String
    private lateinit var messageType: String
    private var isSend = 0

    private lateinit var title: TextView
    private lateinit var date: TextView
    private lateinit var spot: TextView
    private lateinit var time: TextView
    private lateinit var status: TextView
    private lateinit var nickname: TextView

    private lateinit var start_message: TextView
    private lateinit var message: EditText
    private lateinit var sendBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val masterApp = MasterApplication()
        masterApp.createRetrofit(this@ChatActivity)

        val service = masterApp.serviceApi

        client = OkHttpClient()

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
        chatRoomId = intent.extras!!.getString("chatRoomId").toString()

        // RecyclerView
        mRecyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        mRecyclerView.addItemDecoration(RecyclerViewDecoration(50))
        sendBtn.setOnClickListener(sendOnClickListener())

        service.getProfile().enqueue(object : Callback<Result?> {
            override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                val result = response.body()
                val data = result?.data
                user_id = data!!.user_id
                user_name = data!!.user_name
            }

            override fun onFailure(call: Call<Result?>, t: Throwable) {}
        })
        init(chatRoomId)

        val request = Request.Builder()
            .url("ws://43.201.201.163:8080/ws/chat")
            .build()

        val listener = object : WebSocketListener() {

            private val NORMAL_CLOSURE_STATUS = 1000

            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                // 웹소켓 연결이 성공적으로 열림
                Log.d("WebSocketConnection", "Open Successfully")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d("onMessage", text)

                runOnUiThread {
                    // 수신된 JSON 문자열을 파싱하여 JSON 객체로 변환
                    val jsonObject = JSONObject(text)

                    // JSON 객체에서 필요한 필드 추출
                    val type = jsonObject.optString("type")
                    val userObject = jsonObject.optJSONObject("user")
                    val userName = userObject?.optString("name")
                    val message = jsonObject.optString("message")

                    if(type != "ENTER")
                    {
                        if(userName != user_name) {
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
                            val messages = userName?.let { messages(created_at, it, message, true) }
                            if (messages != null) {
                                adapter.addChat(messages)
                            }

                            mRecyclerView.scrollToPosition(adapter.itemCount - 1)
                        }
                    }
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                webSocket.close(NORMAL_CLOSURE_STATUS, null)
                webSocket.cancel()
                Log.d("WebSocketConnection", "WebSocket is closing. Closing : $code, Reason : $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                // 연결이 완전히 종료된 후에 호출됩니다.
                Log.d("WebSocketConnection", "WebSocket closed. Code : $code, Reason : $reason")
            }

            override fun onMessage(webSocket: WebSocket, bytes: okio.ByteString) {
                super.onMessage(webSocket, bytes)
                Log.d("onMessage", "ByteString 데이터 확인 : $bytes")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                super.onFailure(webSocket, t, response)
                Log.d("WebSocketConnection", "Error : " + t.message)
            }
        }

        val client = OkHttpClientSingleton.instance
        websocket = client.newWebSocket(request, listener)

        /* initiate recyclerView */
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun init(chatRoomId: String) {
        val masterApp = MasterApplication()
        masterApp.createRetrofit(this@ChatActivity)

        val service = masterApp.serviceApi

        service.enterChat(chatRoomId).enqueue(object : Callback<Result?> {
            override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                val result = response.body()
                val data = result!!.data

                title.text = data.recruit_title
                date.text = data.recruit_created_at
                time.text = data.recruit_start_time + " - " + data.recruit_end_time
                nickname.text = data.sender_name
                start_message.text = data.sender_name + "님과의 대화를 시작합니다."

                when (data.recruit_spot) {
                    "gusia" -> { spot.text = "구시아" }
                    "shalom" -> { spot.text = "샬롬" }
                    "nuri" -> { spot.text = "누리관" }
                    "fiftieth" -> { spot.text = "50주년" }
                    "gyo" -> { spot.text = "교직원" }
                }

                if (data.recruitStatus === "ONGOING") {
                    status.text = "찾는 중..."
                    status.setBackgroundResource(R.drawable.community_state_finding)
                } else if (data.recruitStatus === "CONNECTING") {
                    status.text = "연락 중..."
                    status.setBackgroundResource(R.drawable.community_state_talking)
                } else if (data.recruitStatus === "COMPLETED") {
                    status.text = "구했어요!"
                    status.setBackgroundResource(R.drawable.community_state_done)
                }

                /* initiate adapter */
                adapter =
                    chatAdapter(data.sender_name, data.messagesList.toMutableList(), applicationContext)
                mRecyclerView.adapter = adapter
                mRecyclerView.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onFailure(call: Call<Result?>, t: Throwable) {}
        })
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
            state: RecyclerView.State,
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
                    `object`.put("message", message.text)
                    Log.d("chat", `object`.toString())

                    websocket.send(`object`.toString())

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
                    val messages = messages(created_at, user_name, message.text.toString(), false)

                    adapter.addChat(messages)
                    mRecyclerView.scrollToPosition(adapter.itemCount - 1)
                    message.setText("")
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
                    `object`.put("message", message.text)
                    Log.d("chat", `object`.toString())
                    websocket.send(`object`.toString())

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
                    val messages = messages(created_at, user_name, message.text.toString(), false)

                    adapter.addChat(messages)
                    mRecyclerView.scrollToPosition(adapter.itemCount - 1)
                    message.setText("")

                } catch (e: JSONException) {
                    throw RuntimeException(e)
                }
            }
        }
    }
}