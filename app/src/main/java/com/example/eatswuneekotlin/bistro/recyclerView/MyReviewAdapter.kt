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
import java.net.HttpURLConnection
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
            name.text = reviews.writer.name
            context.text = reviews.reviewContent
            date.text = reviews.createdAt
            star_rate.rating = reviews.menuRating.toFloat()
            ImageLoadTask(reviews.writer.profileUrl, profile).execute()
            ImageLoadTask(reviews.reviewImgsList[0], review_photo).execute()
        }
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