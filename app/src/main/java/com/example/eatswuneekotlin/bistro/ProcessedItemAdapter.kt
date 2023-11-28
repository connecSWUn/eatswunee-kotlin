package com.example.eatswuneekotlin.bistro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.menus

class ProcessedItemAdapter (private val menusList: List<menus>) :
    RecyclerView.Adapter<ProcessedItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.order_item_list2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = menusList[position]
        holder.setItem(item)
    }

    override fun getItemCount(): Int {
        return menusList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var menu_name: TextView
        var menu_price: TextView
        var menu_cnt: TextView
        var menu_total_price: TextView

        init {
            menu_name = itemView.findViewById<View>(R.id.order_item_menu) as TextView
            menu_price = itemView.findViewById<View>(R.id.menu_item_total_price) as TextView
            menu_cnt = itemView.findViewById<View>(R.id.menu_item_cnt) as TextView
            menu_total_price = itemView.findViewById<View>(R.id.menu_item_price) as TextView
        }

        fun setItem(menus: menus) {
            var price = menus.menuTotalPrice
            var cnt = menus.menuCnt

            menu_name.text = menus.menuName
            menu_price.text = "가격: ${price/cnt}원"
            menu_cnt.text = "수량: ${menus.menuCnt.toString()}개"
            menu_total_price.text = "${menus.menuTotalPrice.toString()}원"
        }
    }
}