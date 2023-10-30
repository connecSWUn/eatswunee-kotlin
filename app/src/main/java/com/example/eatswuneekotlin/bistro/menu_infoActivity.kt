package com.example.eatswuneekotlin.bistro

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.server.RetrofitClient
import com.example.eatswuneekotlin.server.ServiceApi
import com.example.eatswuneekotlin.server.sqlite.DBManager
import com.example.eatswuneekotlin.ShopBagActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.URL

class menu_infoActivity : AppCompatActivity() {
    private var retrofitClient: RetrofitClient? = null
    private var serviceApi: ServiceApi? = null
    var RestaurantName: TextView? = null
    var menuName: TextView? = null
    var menuRating: TextView? = null
    var menuPrice: TextView? = null
    var menuCnt: TextView? = null
    var putBtn: Button? = null
    var reviewBtn: Button? = null
    var cnt_plus: Button? = null
    var cnt_minus: Button? = null
    var menuImage: ImageView? = null
    var menuId: Long = 0
    var menu_image: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_info)
        val toolbar = findViewById<View>(R.id.menu_info_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)


        // 메뉴 따라 텍스트 반영
        RestaurantName = findViewById(R.id.info_bistro_name)
        menuName = findViewById(R.id.info_menu_name)
        menuImage = findViewById(R.id.info_menu_image)
        menuRating = findViewById(R.id.info_star_rate)
        menuPrice = findViewById(R.id.info_price1)
        menuCnt = findViewById(R.id.menu_info_pcs)
        putBtn = findViewById(R.id.put_btn)

        // 리뷰 확인 버튼 : 리뷰 페이지로 이동
        reviewBtn = findViewById(R.id.reviewBtn)

        // 수량 변경 버튼
        cnt_plus = findViewById(R.id.plusBtn)
        cnt_minus = findViewById(R.id.minBtn)
        val intent = intent
        menuId = intent.extras!!.getLong("menuId")
        menu_image = intent.extras!!.getString("menuImage")
        init(menuId)

        reviewBtn.setOnClickListener(reviewOnClickListener())
        putBtn.setOnClickListener(putOnClickListener())
        cnt_plus.setOnClickListener(View.OnClickListener {
            val cnt: Int = menuCnt.getText() as kotlin.String?. toInt ()
            val price: Int = menuPrice.getText() as kotlin.String?. toInt ()
            menuCnt.setText((cnt + 1).toString())
            putBtn.setText((price * (cnt + 1)).toString() + " 원 담기")
        })
        cnt_minus.setOnClickListener(View.OnClickListener {
            val cnt: Int = menuCnt.getText() as kotlin.String?. toInt ()
            val price: Int = menuPrice.getText() as kotlin.String?. toInt ()
            if (cnt == 1) Toast.makeText(this@menu_infoActivity, "최소 수량입니다.", Toast.LENGTH_SHORT)
                .show() else {
                menuCnt.setText((cnt - 1).toString())
                putBtn.setText((price * (cnt - 1)).toString() + " 원 담기")
            }
        })
    }

    private fun init(menuId: Long) {
        retrofitClient = RetrofitClient.instance
        serviceApi = RetrofitClient.serviceApi
        serviceApi.getData(menuId).enqueue(object : Callback<Result?> {
            override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                val result = response.body()
                val data = result!!.data
                RestaurantName!!.text = data.restaurantName
                menuName!!.text = data.menuName
                menuRating!!.text = data.menuRating.toString()
                menuPrice!!.text = data.menuPrice.toString()
                putBtn!!.text = data.menuPrice.toString() + " 원 담기"
                ImageLoadTask(data.menuImg, menuImage).execute()
            }

            override fun onFailure(call: Call<Result?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    inner class ImageLoadTask(private val url: String, private val imageView: ImageView) :
        AsyncTask<Void?, Void?, Bitmap?>() {
        protected override fun doInBackground(vararg params: Void): Bitmap? {
            try {
                val urlConnection = URL(url)
                val connection = urlConnection
                    .openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                return BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            imageView.setImageBitmap(result)
        }
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

    private inner class reviewOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val intent = Intent(this@menu_infoActivity, ReviewActivity::class.java)
            intent.putExtra("menuId", menuId)
            startActivity(intent)
        }
    }

    private inner class putOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val res_name = RestaurantName!!.text.toString()
            val menu_name = menuName!!.text.toString()
            val menu_price: Int = menuPrice!!.text as kotlin.String?. toInt ()
            val menu_cnt: Int = menuCnt!!.text as kotlin.String?. toInt ()

            // DB 객체 생성
            val dbManager = DBManager(this@menu_infoActivity)
            // DB에 저장하기
            dbManager.addBag(menuId, menu_name, menu_image, menu_price, res_name, menu_cnt)
            val intent = Intent(this@menu_infoActivity, ShopBagActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}