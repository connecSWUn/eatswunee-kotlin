package com.example.eatswuneekotlin.mypage

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatswuneekotlin.MasterApplication
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.server.RetrofitClient
import com.example.eatswuneekotlin.server.ServiceApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyReviewActivity : AppCompatActivity() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var adapter: MyOwnReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_review)

        val toolbar = findViewById<View>(R.id.my_review_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)

        init()

        // RecyclerView
        mRecyclerView = findViewById<View>(R.id.my_review_RecyclerView) as RecyclerView

        /* initiate recyclerView */
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menuinfo_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.menuinfo_shopping_basket -> return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        val masterApp = MasterApplication()
        masterApp.createRetrofit(this@MyReviewActivity)

        val service = masterApp.serviceApi
        service.getReviews().enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val result = response.body()
                val data = result!!.data
                adapter = MyOwnReviewAdapter(data?.reviewsList!!)
                mRecyclerView!!.adapter = adapter
            }

            override fun onFailure(call: Call<Result?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}