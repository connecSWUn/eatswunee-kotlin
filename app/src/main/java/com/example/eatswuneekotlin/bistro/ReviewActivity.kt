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
import com.example.eatswuneekotlin.MasterApplication
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.bistro.recyclerView.MyReviewAdapter
import com.example.eatswuneekotlin.server.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class ReviewActivity : AppCompatActivity() {
    private lateinit var menu_name: TextView
    private lateinit var star_rate: TextView
    private lateinit var review_num: TextView
    private lateinit var review_title: TextView

    private lateinit var score5Cnt: TextView
    private lateinit var score4Cnt: TextView
    private lateinit var score3Cnt: TextView
    private lateinit var score2Cnt: TextView
    private lateinit var score1Cnt: TextView

    private lateinit var score5: ProgressBar
    private lateinit var score4: ProgressBar
    private lateinit var score3: ProgressBar
    private lateinit var score2: ProgressBar
    private lateinit var score1: ProgressBar

    private lateinit var menu_image: ImageView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var adapter: MyReviewAdapter

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
        val masterApp = MasterApplication()
        masterApp.createRetrofit(this@ReviewActivity)

        val service = masterApp.serviceApi

        service.getData("menu", "reviews", menuId)?.enqueue(object : Callback<Result?> {
            override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                val result = response.body()
                val data = result!!.data

                adapter = MyReviewAdapter(data?.reviewsList!!)
                mRecyclerView!!.adapter = adapter

                menu_name!!.text = data.menuName
                DownloadFilesTask().execute(data?.menuImg)

                star_rate!!.text = data.menuAvgRating.toString()
                score5Cnt!!.text = data.reviewRating?.score5Cnt.toString()
                score4Cnt!!.text = data.reviewRating?.score4Cnt.toString()
                score3Cnt!!.text = data.reviewRating?.score3Cnt.toString()
                score2Cnt!!.text = data.reviewRating?.score2Cnt.toString()
                score1Cnt!!.text = data.reviewRating?.score1Cnt.toString()

                val five = data.reviewRating?.score5Cnt?.toDouble()
                val four = data.reviewRating?.score4Cnt?.toDouble()
                val three = data.reviewRating?.score3Cnt?.toDouble()
                val two = data.reviewRating?.score2Cnt?.toDouble()
                val one = data.reviewRating?.score1Cnt?.toDouble()
                val total = data.reviewCnt.toDouble()

                if (five != null) {
                    score5!!.progress = (five / total * 100).toInt()
                }
                if (four != null) {
                    score4!!.progress = (four / total * 100).toInt()
                }
                if (three != null) {
                    score3!!.progress = (three / total * 100).toInt()
                }
                if (two != null) {
                    score2!!.progress = (two / total * 100).toInt()
                }
                if (one != null) {
                    score1!!.progress = (one / total * 100).toInt()
                }

                review_num!!.text = "리뷰 " + data.reviewCnt
                review_title!!.text = "리뷰 (" + data.reviewCnt + ")"
            }

            override fun onFailure(call: Call<Result?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    internal inner class DownloadFilesTask : AsyncTask<String?, Void?, Bitmap?>() {
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
            menu_image!!.setImageBitmap(result)
        }
    }
}