package com.example.eatswuneekotlin.mypage

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.bistro.review_writeActivity
import com.example.eatswuneekotlin.server.orders

class MyListAdapter(var context: Context, private val ordersList: List<orders>) :
    RecyclerView.Adapter<MyListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_order_list_one, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = ordersList[position]
        holder.reviewBtn.setOnClickListener {
            val intent = Intent(context, review_writeActivity::class.java)
            intent.putExtra("restaurant_name", item.restaurantName)
            intent.putExtra("menu_name", item.menuName)
            intent.putExtra("menu_id", item.orderMenuId)
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
        holder.setItem(item)
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bistro_name: TextView
        var menu_name: TextView
        var price: TextView
        var cnt: TextView
        var total_price: TextView
        var date: TextView
        var reviewBtn: Button

        init {
            bistro_name = itemView.findViewById<View>(R.id.list_bistro_name) as TextView
            menu_name = itemView.findViewById<View>(R.id.list_menu_name) as TextView
            price = itemView.findViewById<View>(R.id.list_price) as TextView
            cnt = itemView.findViewById<View>(R.id.list_cnt) as TextView
            total_price = itemView.findViewById<View>(R.id.list_total_price) as TextView
            date = itemView.findViewById<View>(R.id.list_date) as TextView
            reviewBtn = itemView.findViewById<View>(R.id.list_review_btn) as Button
        }

        fun setItem(item: orders) {
            bistro_name.text = "[" + item.restaurantName + "]"
            menu_name.text = item.menuName
            price.text = item.menuPrice.toString() + "원"
            cnt.text = item.menuCnt.toString() + "개"
            total_price.text = item.menuTotalPrice.toString() + "원"
            date.text = item.orderCreatedAt
            if (item.isUserWriteReview) {
                reviewBtn.isEnabled = false
                reviewBtn.setBackgroundResource(R.drawable.order_list_btn_unclickable)
                reviewBtn.text = "리뷰 작성 완료"
            } else {
                reviewBtn.isEnabled = true
            }
        }
    }
}