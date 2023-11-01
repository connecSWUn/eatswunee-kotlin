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
import java.io.IOException
import java.net.MalformedURLException
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
        holder.serviceItemClickListener = object : ServiceItemClickListener {
            override fun onItemClickListener(v: View, position: Int) {
                val menuId = menusList[position].menuId
                val menuImage = menusList[position].menuImg
                val intent = Intent(v.context, menu_infoActivity::class.java)
                intent.putExtra("menuId", menuId)
                intent.putExtra("menuImage", menuImage)
                v.context.startActivity(intent)
            }
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
            DownloadFilesTask().execute(menus.menuImg)
        }

        override fun onClick(v: View) {
            serviceItemClickListener!!.onItemClickListener(v, layoutPosition)
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

}