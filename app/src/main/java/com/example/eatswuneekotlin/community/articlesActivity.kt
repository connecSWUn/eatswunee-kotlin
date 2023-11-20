package com.example.eatswuneekotlin.community

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.eatswuneekotlin.MasterApplication
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.Result
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class articlesActivity : AppCompatActivity() {
    private lateinit var myCommunityPageAdapter: MyCommunityPageAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    private var tabCurrentIdx = 0

    private lateinit var nickname: TextView
    private lateinit var cnt: TextView
    private lateinit var profile: ImageView
    private lateinit var write: Button

    override fun onResume() {
        super.onResume()
        init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articles)

        val toolbar = findViewById<View>(R.id.articles_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)


        tabLayout = findViewById(R.id.myPagetabLayout)
        viewPager = findViewById(R.id.myPageviewPager)
        nickname = findViewById(R.id.articles_name)
        cnt = findViewById(R.id.articles_cnt)
        profile = findViewById(R.id.articles_profile)
        write = findViewById(R.id.articles_write_btn)

        init()

        write.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@articlesActivity, Friend_WriteActivity::class.java)
            intent.putExtra("edit", false)
            startActivity(intent)
        })


        //피드 구성하는 탭레이아웃 + 뷰페이저
        tabLayout.addTab(tabLayout.newTab().setText("찾는 중"))
        tabLayout.addTab(tabLayout.newTab().setText("연락 중"))
        tabLayout.addTab(tabLayout.newTab().setText("구함"))

        //커스텀 어댑터 생성
        myCommunityPageAdapter =
            MyCommunityPageAdapter(supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = myCommunityPageAdapter
        viewPager.currentItem = tabCurrentIdx
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                tabCurrentIdx = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
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

    private fun init() {
        val masterApp = MasterApplication()
        masterApp.createRetrofit(this@articlesActivity)

        val service = masterApp.serviceApi

        service.getProfile().enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val result = response.body()
                val data = result!!.data

                Log.d("retrofit", "Data fetch success")
                nickname!!.text = data?.user_name
                DownloadFilesTask().execute(data?.user_profile_url)
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
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
            profile!!.setImageBitmap(result)
        }
    }
}