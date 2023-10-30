package com.example.eatswuneekotlin.bistro.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.homeOrders

class MyViewPagerAdapter(private val ordersList: List<homeOrders>) :
    RecyclerView.Adapter<MyViewPagerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_header, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = ordersList[position]
        holder.setItem(item)
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var order_num: TextView
        var waiting_time: TextView

        init {
            order_num = itemView.findViewById<View>(R.id.order_num) as TextView
            waiting_time = itemView.findViewById<View>(R.id.waiting_time) as TextView
        }

        fun setItem(item: homeOrders) {
            order_num.text = item.orderNum.toString()
            waiting_time.text = item.expectedWaitingTime.toString() + " 분"
        }
    }
}