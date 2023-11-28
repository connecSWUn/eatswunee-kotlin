package com.example.eatswuneekotlin.bistro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatswuneekotlin.MasterApplication
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.server.login.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProcessedActivity : AppCompatActivity() {

    private lateinit var order_date: TextView
    private lateinit var order_price: TextView
    private lateinit var order_num: TextView

    private lateinit var confirm_btn: Button

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var adapter: ProcessedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_processed)

        order_date = findViewById(R.id.order_date)
        order_price = findViewById(R.id.order_price)
        order_num = findViewById(R.id.order_number)
        confirm_btn = findViewById(R.id.confirm_btn)
        confirm_btn.setOnClickListener {
            finish()
        }

        val order_id: Long = intent.getLongExtra("order_id", 0)

        init(order_id)

        mRecyclerView = findViewById(R.id.order_recyclerview)

        /* initiate recyclerView */
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun init(order_id: Long) {
        val masterApp = MasterApplication()
        masterApp.createRetrofit(this@ProcessedActivity)

        val service = masterApp.serviceApi
        service.getOrderContext(order_id).enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val result = response.body()
                val data = result!!.data

                order_date.text = data.order_created_at
                order_price.text = "${data.order_total_price} 원"
                order_num.text = data.order_num

                adapter = ProcessedAdapter(data.ordersList)
                mRecyclerView!!.adapter = adapter
            }

            override fun onFailure(call: Call<Result?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}