package com.example.eatswuneekotlin.server.sqlite

import android.annotation.SuppressLint
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

class ShopBagAdapter(
    private val listener: ShopBagAdapterListener,
    private val context: Context
) : RecyclerView.Adapter<ShopBagAdapter.ViewHolder>() {

    var bags = ArrayList<shop_bag>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_shopbag, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val bag = bags[position]
        holder.setItem(bag)
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

    interface ShopBagAdapterListener {
        fun onCntPlusClicked(position: Int)
        fun onCntMinusClicked(position: Int)
        fun onDeleteClicked(position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var menu_price: TextView
        var menu_cnt: TextView
        var menu_name: TextView
        var total_price: TextView
        var menu_image: ImageView
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

            cnt_plus.setOnClickListener {
                val cnt: Int = menu_cnt.text.toString().toInt()
                val price: Int = menu_price.text.toString().toInt()
                menu_cnt.text = (cnt + 1).toString()
                total_price.text = (price * (cnt + 1)).toString() +"원"

                val plus_cnt: Int = menu_cnt.text.toString().toInt() // 안전한 형변환 사용
                val menuName = menu_name.text?.toString() ?: ""

                val db = DBManager(context)
                db.updateData(menuName, plus_cnt)

                listener.onCntPlusClicked(adapterPosition)
            }

            cnt_minus.setOnClickListener {
                val cnt: Int = menu_cnt.text.toString().toInt()
                val price: Int = menu_price.text.toString().toInt()

                if (cnt == 1)
                    Toast.makeText(context, "최소 수량입니다.", Toast.LENGTH_SHORT).show()
                else {
                    menu_cnt.text = (cnt - 1).toString()
                    total_price.text = (price * (cnt - 1)).toString()

                    val minus_cnt: Int = menu_cnt.text.toString().toInt() // 안전한 형변환 사용
                    val menuName = menu_name.text?.toString() ?: ""
                    val db = DBManager(context)
                    db.updateData(menuName, minus_cnt)

                    listener.onCntMinusClicked(adapterPosition)
                }
            }

            delete.setOnClickListener {
                val menu_name = menu_name.text as String
                val db = DBManager(context)
                db.deleteData(menu_name)

                bags.removeAt(position)
                notifyItemRemoved(position)

                listener.onDeleteClicked(adapterPosition)
            }
        }

        fun setItem(bag: shop_bag) {
            menu_name.text = bag.menu_name
            menu_price.text = bag.menu_price.toString()
            menu_cnt.text = bag.menu_cnt.toString()
            total_price.text = (bag.menu_price * bag.menu_cnt).toString()
            DownloadFilesTask().execute(bag.menu_image)
        }

        inner class DownloadFilesTask : AsyncTask<String?, Void?, Bitmap?>() {
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