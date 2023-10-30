package com.example.eatswuneekotlin

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.eatswuneekotlin.mypage.MyReviewActivity
import com.example.eatswuneekotlin.mypage.order_listActivity
import com.example.eatswuneekotlin.mypage.profile_editActivity
import com.example.eatswuneekotlin.mypage.settingActivity
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class MyPageFragment : Fragment() {
    private lateinit var listBtn: Button
    lateinit var reviewBtn: Button
    private lateinit var settingBtn: Button
    private lateinit var profileBtn: LinearLayout
    private lateinit var name: TextView
    private lateinit var img: ImageView

    lateinit var activity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_mypage, container, false)

        profileBtn = v.findViewById(R.id.profile_edit_btn)
        name = v.findViewById(R.id.profile_user_name)
        img = v.findViewById(R.id.profile_img)
        listBtn = v.findViewById(R.id.list_btn)
        reviewBtn = v.findViewById(R.id.review_btn)
        settingBtn = v.findViewById(R.id.setting_btn)

        init()

        profileBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, profile_editActivity::class.java)
            startActivity(intent)
        })

        listBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, order_listActivity::class.java)
            startActivity(intent)
        })

        reviewBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, MyReviewActivity::class.java)
            startActivity(intent)
        })

        settingBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, settingActivity::class.java)
            startActivity(intent)
        })

        return v
    }

    private fun init() {
        val masterApp = MasterApplication()
        masterApp.createRetrofit(activity)

        val service = masterApp.serviceApi

        service!!.profile.enqueue(object : Callback<Result?> {
            override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                val result = response.body()
                val data = result!!.data
                Log.d("retrofit", "Data fetch success")

                name!!.text = data!!.user_name
                DownloadFilesTask().execute(data.user_profile_url)
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
            img!!.setImageBitmap(result)
        }
    }
}