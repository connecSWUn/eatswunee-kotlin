package com.example.eatswuneekotlin.bistro.recyclerView

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.bistro.menu_infoActivity
import com.example.eatswuneekotlin.community.ServiceItemClickListener
import com.example.eatswuneekotlin.server.menus
import java.net.HttpURLConnection
import java.net.URL

class MyBistroAdapter(private val menusList: List<menus>) :
    RecyclerView.Adapter<MyBistroAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bistro_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = menusList[position]
        holder.setItem(item)
        holder.serviceItemClickListener = ServiceItemClickListener { v, position ->
            val menuId = menusList[position].menuId
            val menuImage = menusList[position].menuImg
            val intent = Intent(v.context, menu_infoActivity::class.java)
            intent.putExtra("menuId", menuId)
            intent.putExtra("menuImage", menuImage)
            v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return menusList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var restaurant_name: TextView
        var menu_name: TextView
        var menu_price: TextView
        var menu_avg_rating: TextView
        var menu_image: ImageView
        var serviceItemClickListener: ServiceItemClickListener? = null

        init {
            menu_image = itemView.findViewById<View>(R.id.menu_image) as ImageView
            restaurant_name = itemView.findViewById<View>(R.id.bistro_name) as TextView
            menu_avg_rating = itemView.findViewById<View>(R.id.star_rate) as TextView
            menu_name = itemView.findViewById<View>(R.id.menu_name) as TextView
            menu_price = itemView.findViewById<View>(R.id.price) as TextView
            itemView.setOnClickListener(this)
        }

        fun setItem(menus: menus) {
            restaurant_name.text = menus.restaurantName
            menu_avg_rating.text = menus.menuRating.toString()
            menu_name.text = menus.menuName
            menu_price.text = menus.menuPrice
            ImageLoadTask(menus.menuImg, menu_image).execute()
        }

        override fun onClick(v: View) {
            serviceItemClickListener!!.onItemClickListener(v, layoutPosition)
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