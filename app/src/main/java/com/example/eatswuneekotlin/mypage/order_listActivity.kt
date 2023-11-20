package com.example.eatswuneekotlin.mypage

import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.eatswuneekotlin.MasterApplication
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.server.RetrofitClient
import com.example.eatswuneekotlin.server.ServiceApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class order_listActivity : AppCompatActivity() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var adapter: MyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        val toolbar = findViewById<View>(R.id.order_list_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)

        // RecyclerView
        mRecyclerView = findViewById<View>(R.id.order_list_RecyclerView) as RecyclerView
        mRecyclerView!!.addItemDecoration(RecyclerViewDecoration(50))

        init()

        /* initiate recyclerView */
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun init() {
        val masterApp = MasterApplication()
        masterApp.createRetrofit(this@order_listActivity)

        val service = masterApp.serviceApi
        service.getOrderList().enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val result = response.body()
                val data = result!!.data
                adapter = MyListAdapter(applicationContext, data?.ordersList!!)
                mRecyclerView!!.adapter = adapter
            }

            override fun onFailure(call: Call<Result?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
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
}