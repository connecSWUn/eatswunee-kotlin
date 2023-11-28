package com.example.eatswuneekotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatswuneekotlin.bistro.Order_WaitingActivity
import com.example.eatswuneekotlin.bistro.orderMenus
import com.example.eatswuneekotlin.bistro.order_menu
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.server.sqlite.DBManager
import com.example.eatswuneekotlin.server.sqlite.shop_bag
import com.example.eatswuneekotlin.server.sqlite.ShopBagAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopBagActivity : AppCompatActivity(),  ShopBagAdapter.ShopBagAdapterListener {
    private lateinit var dbManager: DBManager

    var bags = ArrayList<shop_bag>()
    lateinit var recyclerView: RecyclerView
    lateinit var payment_btn: TextView
    lateinit var adapter: ShopBagAdapter
    lateinit var noDataText: TextView
    lateinit var total_cnt: TextView
    lateinit var total_price: TextView

    var cnt_sum = 0
    var price_sum = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopbag)

        val masterApp = MasterApplication()
        masterApp.createRetrofit(this@ShopBagActivity)

        val service = masterApp.serviceApi

        // 데이터 유무 텍스트
        noDataText = findViewById(R.id.noDataText)
        payment_btn = findViewById(R.id.pay_btn)
        total_cnt = findViewById(R.id.total_cnt)
        total_price = findViewById(R.id.total_price)

        // toolbar setting
        val toolbar = findViewById<View>(R.id.shopbag_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)

        // recyclerView setting
        recyclerView = findViewById(R.id.shopbag_RecyclerView)
        recyclerView.layoutManager = (LinearLayoutManager(this))
        adapter = ShopBagAdapter(this, this)
        recyclerView!!.adapter = adapter

        // dbManager setting
        dbManager = DBManager(this@ShopBagActivity)
        storeDataInArrays()
        total_cnt.text = cnt_sum.toString()
        total_price.text = price_sum.toString() + "원"

        payment_btn.setOnClickListener(View.OnClickListener {
            val cursor = dbManager!!.readAllData()
            if (cursor!!.count == 0) {
                Toast.makeText(this@ShopBagActivity, "주문 항목이 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val list = mutableListOf<order_menu>()

                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(0)
                        val cnt = cursor.getInt(5)
                        val menu = order_menu(id, cnt)

                        list.add(menu)
                    }
                    val orderMenus = orderMenus(list)


                    service!!.postOrder(orderMenus).enqueue(object : Callback<Result> {
                        override fun onResponse(call: Call<Result>, response: Response<Result>) {
                            Log.d("order", response.isSuccessful.toString())

                            val result = response.body()
                            val data = result?.data

                            var order_num = data?.order_id

                            dbManager!!.deleteAllData()
                            val intent = Intent(this@ShopBagActivity, Order_WaitingActivity::class.java)
                            intent.putExtra("order_id", order_num)
                            startActivity(intent)
                            finish()
                        }

                        override fun onFailure(call: Call<Result?>, t: Throwable) {}
                    })

                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        })
    }

    /** 데이터 가져오기  */
    fun storeDataInArrays() {
        val cursor = dbManager!!.readAllData()
        if (cursor!!.count == 0) {
            noDataText!!.visibility = View.VISIBLE
        } else {
            noDataText!!.visibility = View.GONE
            while (cursor.moveToNext()) {
                val bag = shop_bag(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getInt(5)
                )
                cnt_sum += cursor.getInt(5)
                price_sum += cursor.getInt(3) * cursor.getInt(5)

                // 데이터 등록
                bags.add(bag)
                adapter!!.addItem(bag)

                // 적용
                adapter!!.notifyDataSetChanged()
            }
        }
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

    override fun onCntPlusClicked(position: Int) {
        val intent = intent
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun onCntMinusClicked(position: Int) {
        val intent = intent
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun onDeleteClicked(position: Int) {
        // 버튼 클릭에 대한 작업을 처리합니다.
    }
}