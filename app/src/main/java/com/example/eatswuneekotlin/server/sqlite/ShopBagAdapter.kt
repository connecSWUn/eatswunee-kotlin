package com.example.eatswuneekotlin.server.sqlite

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.eatswuneekotlin.R
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class ShopBagAdapter(var context: Context) : RecyclerView.Adapter<ShopBagAdapter.ViewHolder>() {

    var bags = ArrayList<shop_bag>()
    var menu_image: ImageView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_shopbag, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val bag = bags[position]

        holder.menu_name.text = bag.menu_name
        holder.menu_price.text = bag.menu_price.toString()
        holder.menu_cnt.text = bag.menu_cnt.toString()
        holder.total_price.text = (bag.menu_price * bag.menu_cnt).toString()
        DownloadFilesTask().execute(bag.menu_image)

        holder.cnt_plus.setOnClickListener { view ->
            val cnt: Int = holder.menu_cnt.toString().toInt()
            val price: Int = holder.menu_price.toString().toInt()
            holder.menu_cnt.text = (cnt + 1).toString()
            holder.total_price.text = (price * (cnt + 1)).toString()

            val plus_cnt: Int = holder.menu_cnt.toString().toInt()
            val menu_name = holder.menu_name.text as String

            val db = DBManager(view.context)
            db.updateData(menu_name, plus_cnt)

            val intent = (context as Activity).intent
            (context as Activity).finish() //현재 액티비티 종료 실시
            (context as Activity).overridePendingTransition(0, 0) //효과 없애기
            (context as Activity).startActivity(intent) //현재 액티비티 재실행 실시
            (context as Activity).overridePendingTransition(0, 0) //효과 없애기

        }

        holder.cnt_minus.setOnClickListener { view ->

            val cnt: Int = holder.menu_cnt.toString().toInt()
            val price: Int = holder.menu_price.toString().toInt()

            if (cnt == 1)
                Toast.makeText(view.context, "최소 수량입니다.", Toast.LENGTH_SHORT).show()
            else {
                holder.menu_cnt.text = (cnt - 1).toString()
                holder.total_price.text = (price * (cnt - 1)).toString()

                val minus_cnt: Int = holder.menu_cnt.toString().toInt()
                val menu_name = holder.menu_name.text as String
                val db = DBManager(view.context)
                db.updateData(menu_name, minus_cnt)

                val intent = (context as Activity).intent
                (context as Activity).finish() //현재 액티비티 종료 실시
                (context as Activity).overridePendingTransition(0, 0) //효과 없애기
                (context as Activity).startActivity(intent) //현재 액티비티 재실행 실시
                (context as Activity).overridePendingTransition(0, 0) //효과 없애기

            }
        }

        holder.delete.setOnClickListener { view ->
            val menu_name = holder.menu_name.text as String
            val db = DBManager(view.context)
            db.deleteData(menu_name)

            bags.removeAt(position)
            notifyItemRemoved(position)

            val intent = (context as Activity).intent
            (context as Activity).finish() //현재 액티비티 종료 실시
            (context as Activity).overridePendingTransition(0, 0) //효과 없애기
            (context as Activity).startActivity(intent) //현재 액티비티 재실행 실시
            (context as Activity).overridePendingTransition(0, 0) //효과 없애기

        }
    }

    override fun getItemCount(): Int {
        return bags.size
    }

    /* 아이템 삭제 */
    fun removeItem(position: Int) {
        bags.removeAt(position)
    }

    fun addItem(item: shop_bag) {
        bags.add(item)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var menu_price: TextView
        var menu_cnt: TextView
        var menu_name: TextView
        var total_price: TextView
        var cnt_minus: Button
        var cnt_plus: Button
        var delete: Button

        init {
            menu_name = itemView.findViewById(R.id.shopbag_menu_name)
            menu_price = itemView.findViewById(R.id.shopbag_price)
            menu_cnt = itemView.findViewById(R.id.shopbag_cnt)
            total_price = itemView.findViewById(R.id.shopbag_total_price)
            menu_image = itemView.findViewById(R.id.shop_bag_menu_image)
            cnt_minus = itemView.findViewById(R.id.shopbag_minus)
            cnt_plus = itemView.findViewById(R.id.shopbag_plus)
            delete = itemView.findViewById(R.id.shopbag_delete)
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
            // doInBackground 에서 받아온 total 값 사용 장소
            menu_image!!.setImageBitmap(result)
        }
    }
}