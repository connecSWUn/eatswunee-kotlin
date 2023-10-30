package com.example.eatswuneekotlin.bistro

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.bistro.recyclerView.MyReviewAdapter
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.server.RetrofitClient
import com.example.eatswuneekotlin.server.ServiceApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.URL

class ReviewActivity : AppCompatActivity() {
    private var retrofitClient: RetrofitClient? = null
    private var serviceApi: ServiceApi? = null
    var menu_name: TextView? = null
    var star_rate: TextView? = null
    var review_num: TextView? = null
    var review_title: TextView? = null
    var score5Cnt: TextView? = null
    var score4Cnt: TextView? = null
    var score3Cnt: TextView? = null
    var score2Cnt: TextView? = null
    var score1Cnt: TextView? = null
    var score5: ProgressBar? = null
    var score4: ProgressBar? = null
    var score3: ProgressBar? = null
    var score2: ProgressBar? = null
    var score1: ProgressBar? = null
    var menu_image: ImageView? = null
    private var mRecyclerView: RecyclerView? = null
    private var adapter: MyReviewAdapter? = null
    var menuId: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        val toolbar = findViewById<View>(R.id.review_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)

        // 메뉴 이름 및 이미지 반영 : 이미지 반영 이전
        menu_name = findViewById(R.id.review_menu_name)
        // 평균 별점 : TextView
        star_rate = findViewById(R.id.review_star_rate)
        review_num = findViewById(R.id.review_num)
        review_title = findViewById(R.id.review_title)
        menu_image = findViewById(R.id.review_menu_image)
        score5Cnt = findViewById(R.id.five_star_num)
        score4Cnt = findViewById(R.id.four_star_num)
        score3Cnt = findViewById(R.id.three_star_num)
        score2Cnt = findViewById(R.id.two_star_num)
        score1Cnt = findViewById(R.id.one_star_num)
        score5 = findViewById(R.id.five_star)
        score4 = findViewById(R.id.four_star)
        score3 = findViewById(R.id.three_star)
        score2 = findViewById(R.id.two_star)
        score1 = findViewById(R.id.one_star)
        val intent = intent
        menuId = intent.extras!!.getLong("menuId")
        init(menuId)

        // RecyclerView
        mRecyclerView = findViewById<View>(R.id.shopbag_RecyclerView) as RecyclerView

        /* initiate recyclerView */mRecyclerView!!.layoutManager = LinearLayoutManager(this)
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

    private fun init(menuId: Long) {
        retrofitClient = RetrofitClient.instance
        serviceApi = RetrofitClient.serviceApi
        serviceApi.getData("menu", "reviews", menuId).enqueue(object : Callback<Result?> {
            override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                val result = response.body()
                val data = result!!.data
                adapter = MyReviewAdapter(data.reviewsList)
                mRecyclerView!!.adapter = adapter
                menu_name!!.text = data.menuName
                ImageLoadTask(data.menuImg, menu_image).execute()
                star_rate!!.text = data.menuAvgRating.toString()
                score5Cnt!!.text = data.reviewRating.score5Cnt.toString()
                score4Cnt!!.text = data.reviewRating.score4Cnt.toString()
                score3Cnt!!.text = data.reviewRating.score3Cnt.toString()
                score2Cnt!!.text = data.reviewRating.score2Cnt.toString()
                score1Cnt!!.text = data.reviewRating.score1Cnt.toString()
                val five = data.reviewRating.score5Cnt.toDouble()
                val four = data.reviewRating.score4Cnt.toDouble()
                val three = data.reviewRating.score3Cnt.toDouble()
                val two = data.reviewRating.score2Cnt.toDouble()
                val one = data.reviewRating.score1Cnt.toDouble()
                val total = data.reviewCnt.toDouble()
                score5!!.progress = (five / total * 100).toInt()
                score4!!.progress = (four / total * 100).toInt()
                score3!!.progress = (three / total * 100).toInt()
                score2!!.progress = (two / total * 100).toInt()
                score1!!.progress = (one / total * 100).toInt()
                review_num!!.text = "리뷰 " + data.reviewCnt
                review_title!!.text = "리뷰 (" + data.reviewCnt + ")"
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
}