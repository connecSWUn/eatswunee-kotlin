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
import com.example.eatswuneekotlin.MasterApplication
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.server.RetrofitClient
import com.example.eatswuneekotlin.server.ServiceApi
import com.example.eatswuneekotlin.server.sqlite.DBManager
import com.example.eatswuneekotlin.ShopBagActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class menu_infoActivity : AppCompatActivity() {
    private lateinit var RestaurantName: TextView
    private lateinit var menuName: TextView
    private lateinit var menuRating: TextView
    private lateinit var menuPrice: TextView
    private lateinit var menuCnt: TextView

    private lateinit var putBtn: Button
    private lateinit var reviewBtn: Button
    private lateinit var cnt_plus: Button
    private lateinit var cnt_minus: Button
    private lateinit var menuImage: ImageView

    var menuId: Long = 0
    private lateinit var menu_image: String


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
        menu_image = intent.extras!!.getString("menuImage").toString()
        init(menuId)

        reviewBtn.setOnClickListener(reviewOnClickListener())
        putBtn.setOnClickListener(putOnClickListener())

        cnt_plus.setOnClickListener(View.OnClickListener {
            val cnt: Int = menuCnt.text.toString().toInt()
            val price: Int = menuPrice.text.toString().toInt()

            menuCnt.text = (cnt + 1).toString()
            putBtn.text = (price * (cnt + 1)).toString() + " 원 담기"
        })

        cnt_minus.setOnClickListener(View.OnClickListener {
            val cnt: Int = menuCnt.text.toString().toInt()
            val price: Int = menuPrice.text.toString().toInt()

            if (cnt == 1) Toast.makeText(this@menu_infoActivity, "최소 수량입니다.", Toast.LENGTH_SHORT)
                .show() else {
                menuCnt.text = (cnt - 1).toString()
                putBtn.text = (price * (cnt - 1)).toString() + " 원 담기"
            }
        })

    }

    private fun init(menuId: Long) {
        val masterApp = MasterApplication()
        masterApp.createRetrofit(this@menu_infoActivity)

        val service = masterApp.serviceApi

        service.getData(menuId)?.enqueue(object : Callback<Result?> {
            override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                val result = response.body()
                val data = result!!.data

                RestaurantName!!.text = data?.restaurantName
                menuName!!.text = data?.menuName
                menuRating!!.text = data?.menuRating.toString()
                menuPrice!!.text = data?.menuPrice.toString()
                putBtn!!.text = data?.menuPrice.toString() + " 원 담기"
                DownloadFilesTask().execute(data?.menuImg)
            }

            override fun onFailure(call: Call<Result?>, t: Throwable) {
                t.printStackTrace()
            }

            inner class DownloadFilesTask : AsyncTask<String?, Void?, Bitmap?>() {
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
                    menuImage!!.setImageBitmap(result)
                }
            }
        })
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
            val menu_price: Int = menuPrice!!.text.toString().toInt()
            val menu_cnt: Int = menuCnt!!.text.toString().toInt()

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