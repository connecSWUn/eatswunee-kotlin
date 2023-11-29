package com.example.eatswuneekotlin.mypage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eatswuneekotlin.MasterApplication
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.server.RetrofitClient
import com.example.eatswuneekotlin.server.ServiceApi
import com.example.eatswuneekotlin.server.reviews
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class MyOwnReviewAdapter(private val reviewsList: List<reviews>) :
    RecyclerView.Adapter<MyOwnReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_review_photo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = reviewsList[position]
        holder.setItem(item)
    }

    override fun getItemCount(): Int {
        return reviewsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var restaurant_name: TextView
        var menu_name: TextView
        var content: TextView
        var created_at: TextView
        var star_rate: RatingBar
        var review_img: ImageView
        var deleteBtn: Button

        init {
            restaurant_name = itemView.findViewById<View>(R.id.my_photoR_res_name) as TextView
            menu_name = itemView.findViewById<View>(R.id.my_photoR_menu_name) as TextView
            content = itemView.findViewById<View>(R.id.my_photoR_content) as TextView
            created_at = itemView.findViewById<View>(R.id.my_photoR_date) as TextView
            star_rate = itemView.findViewById<View>(R.id.my_photoR_rate) as RatingBar
            review_img = itemView.findViewById<View>(R.id.my_review_photo) as ImageView
            deleteBtn = itemView.findViewById<View>(R.id.my_photoR_deleteBtn) as Button
        }

        fun setItem(reviews: reviews) {
            restaurant_name.text = "[" + reviews.restaurant_name + "]"
            menu_name.text = reviews.menu_name
            content.text = reviews.review_content
            created_at.text = reviews.review_created_at
            star_rate.rating = reviews.menu_review_rating.toFloat()
            if (reviews.review_image_url == null) {
                review_img.visibility = View.GONE
            } else {
                DownloadFilesTask().execute(reviews?.review_image_url)
            }
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
                review_img!!.setImageBitmap(result)
            }
        }
    }
}