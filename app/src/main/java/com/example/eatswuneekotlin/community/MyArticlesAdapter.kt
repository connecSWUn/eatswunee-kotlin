package com.example.eatswuneekotlin.community

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.posts

class MyArticlesAdapter(private val postsList: List<posts>) :
    RecyclerView.Adapter<MyArticlesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_community, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = postsList[position]
        holder.setItem(item)
        holder.serviceItemClickListener = object : ServiceItemClickListener {
            override fun onItemClickListener(v: View, position: Int) {
                val recruitId = postsList[position].postId
                val intent = Intent(v.context, Friend_ViewActivity::class.java)
                intent.putExtra("recruitId", recruitId)
                v.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return postsList.size
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

        fun setItem(item: posts) {
            title.text = item.postTitle

            when (item.postSpot) {
                "gusia" -> { place.text = "구시아" }
                "shalom" -> { place.text = "샬롬" }
                "nuri" -> { place.text = "누리관" }
                "fiftieth" -> { place.text = "50주년" }
                "gyo" -> { place.text = "교직원" }
            }

            app_time.text = item.postStartTime + "-" + item.postEndTime
            post_date.text = item.postCreatedAt

            when (item.postRecruitStatus) {
                "ONGOING" -> {
                    status.text = "찾는 중..."
                    statusImg.setImageResource(R.drawable.baseline_search_24)
                    status.setBackgroundResource(R.drawable.community_state_finding)
                }
                "CONNECTING" -> {
                    status.text = "연락 중..."
                    statusImg.setImageResource(R.drawable.baseline_question_answer_24)
                    status.setBackgroundResource(R.drawable.community_state_talking)
                }
                "COMPLETED" -> {
                    status.text = "구했어요!"
                    statusImg.setImageResource(R.drawable.baseline_handshake_24)
                    status.setBackgroundResource(R.drawable.community_state_done)
                }
            }
        }

        override fun onClick(v: View) {
            serviceItemClickListener!!.onItemClickListener(v, layoutPosition)
        }
    }
}