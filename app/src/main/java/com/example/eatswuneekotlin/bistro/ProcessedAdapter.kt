package com.example.eatswuneekotlin.bistro


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eatswuneekotlin.MasterApplication
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.orders

class ProcessedAdapter (private val ordersList: List<orders>) :
    RecyclerView.Adapter<ProcessedAdapter.ViewHolder>() {

    lateinit var mRecyclerView: RecyclerView
    lateinit var adapter: ProcessedItemAdapter
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.order_item_list, parent, false)

        context = parent.context

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
        var name: TextView

        init {
            name = itemView.findViewById(R.id.order_item_name)
            mRecyclerView = itemView.findViewById(R.id.order_item_recyclerView) as RecyclerView

            /* initiate recyclerView */
            mRecyclerView!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        fun setItem(orders: orders) {
            name.text = orders.restaurantName

            adapter = ProcessedItemAdapter(orders.menusList)
            mRecyclerView!!.adapter = adapter
        }
    }
}