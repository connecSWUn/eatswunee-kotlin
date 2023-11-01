package com.example.eatswuneekotlin.bistro.recyclerView

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.reviews
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class MyReviewAdapter(private val reviewsList: List<reviews>) :
    RecyclerView.Adapter<MyReviewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_review_photo, parent, false)
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
        var name: TextView
        var context: TextView
        var date: TextView
        var profile: ImageView
        var review_photo: ImageView
        var star_rate: RatingBar

        init {
            profile = itemView.findViewById<View>(R.id.review_profile) as ImageView
            name = itemView.findViewById<View>(R.id.shopbag_menu_name) as TextView
            context = itemView.findViewById<View>(R.id.shopbag_price) as TextView
            date = itemView.findViewById<View>(R.id.my_photoR_date) as TextView
            star_rate = itemView.findViewById<View>(R.id.my_photoR_rate) as RatingBar
            review_photo = itemView.findViewById<View>(R.id.my_review_photo) as ImageView
        }

        fun setItem(reviews: reviews) {
            name.text = reviews.writer?.name
            context.text = reviews.reviewContent
            date.text = reviews.createdAt
            star_rate.rating = reviews.menuRating.toFloat()
            DownloadFilesTask().execute(reviews.writer?.profileUrl)
            DownloadFilesTask().execute(reviews.reviewImgsList?.get(0))
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
}