package com.example.eatswuneekotlin.server.chat

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
import com.example.eatswuneekotlin.community.ServiceItemClickListener
import com.example.eatswuneekotlin.server.chatRooms
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class MyChatListAdapter(private val items: List<chatRooms?>?) :
    RecyclerView.Adapter<MyChatListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 사진 데이터 추가 시 if문 구분 필요
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items!![position]
        holder.setItem(item)
        holder.serviceItemClickListener = object : ServiceItemClickListener {

            override fun onItemClickListener(v: View, position: Int) {
                val chatRoomId = items[position]?.chatRoom
                val intent = Intent(v.context, ChatActivity::class.java)
                intent.putExtra("chatRoomId", chatRoomId)
                v.context.startActivity(intent)
            }

        }
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val title: TextView
        private val nickname: TextView
        private val message: TextView
        private val date: TextView
        private val profile: ImageView
        lateinit var serviceItemClickListener: ServiceItemClickListener

        init {
            title = itemView.findViewById(R.id.chat_list_title)
            nickname = itemView.findViewById(R.id.chat_list_name)
            message = itemView.findViewById(R.id.chat_list_message)
            date = itemView.findViewById(R.id.chat_list_date)
            profile = itemView.findViewById(R.id.chat_list_profile)
            itemView.setOnClickListener(this)
        }

        fun setItem(item: chatRooms?) {
            title.text = item?.recruitTitle
            nickname.text = item?.senderNickname
            message.text = item?.lastChatMessage
            date.text = item?.lastChatCreatedAt
            DownloadFilesTask().execute(item?.senderProfileImgUrl)
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
                profile!!.setImageBitmap(result)
            }
        }
    }


}