package com.example.eatswuneekotlin.community

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.Post

class MyCommunityAdapter(private val items: List<Post>) :
    RecyclerView.Adapter<MyCommunityAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 사진 데이터 추가 시 if문 구분 필요
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_community, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
        holder.serviceItemClickListener = object : ServiceItemClickListener {
            override fun onItemClickListener(v: View, position: Int) {
                val recruitId = items[position].recruitId
                val intent = Intent(v.context, Friend_ViewActivity::class.java)
                intent.putExtra("recruitId", recruitId)
                v.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val title: TextView
        private val place: TextView
        private val app_time: TextView
        private val post_date: TextView
        private val status: TextView
        private val statusImg: ImageView
        var serviceItemClickListener: ServiceItemClickListener? = null

        init {
            statusImg = itemView.findViewById<View>(R.id.community_image) as ImageView
            title = itemView.findViewById<View>(R.id.shopbag_menu_name) as TextView
            place = itemView.findViewById<View>(R.id.com_place) as TextView
            app_time = itemView.findViewById<View>(R.id.shopbag_price) as TextView
            post_date = itemView.findViewById<View>(R.id.my_photoR_date) as TextView
            status = itemView.findViewById<View>(R.id.com_status) as TextView
            itemView.setOnClickListener(this)
        }

        fun setItem(item: Post) {
            title.text = item.title

            when (item.spot) {
                "gusia" -> { place.text = "구시아" }
                "shalom" -> { place.text = "샬롬" }
                "nuri" -> { place.text = "누리관" }
                "fiftieth" -> { place.text = "50주년" }
                "gyo" -> { place.text = "교직원" }
            }
            //place.text = item.spot
            app_time.text = item.startTime + " - " + item.endTime
            post_date.text = item.createdAt
            if (item.recruitStatus == "ONGOING") {
                status.text = "찾는 중..."
                statusImg.setImageResource(R.drawable.baseline_search_24)
                status.setBackgroundResource(R.drawable.community_state_finding)
            } else if (item.recruitStatus == "CONNECTING") {
                status.text = "연락 중..."
                statusImg.setImageResource(R.drawable.baseline_question_answer_24)
                status.setBackgroundResource(R.drawable.community_state_talking)
            } else if (item.recruitStatus == "COMPLETED") {
                status.text = "구했어요!"
                statusImg.setImageResource(R.drawable.baseline_handshake_24)
                status.setBackgroundResource(R.drawable.community_state_done)
            }
        }

        override fun onClick(v: View) {
            serviceItemClickListener!!.onItemClickListener(v, layoutPosition)
        }
    }
}