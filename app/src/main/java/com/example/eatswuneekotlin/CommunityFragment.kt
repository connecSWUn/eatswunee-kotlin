package com.example.eatswuneekotlin

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.eatswuneekotlin.community.MyCommunityAdapter
import com.example.eatswuneekotlin.community.friend_writeActivity
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityFragment : Fragment() {
    lateinit var total: Button
    lateinit var gusia: Button
    lateinit var nuri: Button
    lateinit var fiftieth: Button
    lateinit var shalom: Button
    lateinit var gyo: Button

    private lateinit var writeBtn: Button
    lateinit var v: View

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var adapter: MyCommunityAdapter

    lateinit var activity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        init("ALL")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_community, container, false)
        total = v.findViewById(R.id.community_totalBtn)
        gusia = v.findViewById(R.id.community_gusia)
        nuri = v.findViewById(R.id.community_nuri)
        fiftieth = v.findViewById(R.id.community_50th)
        shalom = v.findViewById(R.id.community_shalom)
        gyo = v.findViewById(R.id.community_gyo)
        writeBtn = v.findViewById(R.id.write_button)

        /* 초기 세팅
         * 어플리케이션 실행 시 커뮤니티 화면 기본 선택 버튼 : 전체 버튼
         */
        total.setSelected(true)
        gusia.setSelected(false)
        nuri.setSelected(false)
        fiftieth.setSelected(false)
        shalom.setSelected(false)
        gyo.setSelected(false)
        init("ALL")

        /* 커뮤니티 버튼 리스너 */
        total.setOnClickListener(totalOnClickListener())
        gusia.setOnClickListener(gusiaOnClickListener())
        nuri.setOnClickListener(nuriOnClickListener())
        fiftieth.setOnClickListener(fiftiethOnClickListener())
        shalom.setOnClickListener(shalomOnClickListener())
        gyo.setOnClickListener(gyoOnClickListener())

        /* 글 쓰기 버튼 리스너 */
        writeBtn.setOnClickListener(writeOnClickListener())

        /* RecyclerView */
        mRecyclerView = v.findViewById<View>(R.id.recyclerView2) as RecyclerView
        mRecyclerView.addItemDecoration(RecyclerViewDecoration(20))

        /* initiate recyclerView */
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        return v
    }


    inner class RecyclerViewDecoration(private val divHeight: Int) : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.top = divHeight
        }
    }


    private fun init(category: String) {
        val masterApp = MasterApplication()
        masterApp.createRetrofit(activity)

        val service = masterApp.serviceApi

        service.getData(category).enqueue(object : Callback<Result?> {
            override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                val result = response.body()
                val data = result!!.data
                Log.d("retrofit", "Data fetch success")
                
                /* initiate adapter */
                adapter = MyCommunityAdapter(data!!.postList!!)
                mRecyclerView!!.adapter = adapter
            }

            override fun onFailure(call: Call<Result?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private inner class writeOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val intent = Intent(activity, friend_writeActivity::class.java)
            intent.putExtra("edit", false)
            startActivity(intent)
        }
    }

    private inner class totalOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            total!!.isSelected = true
            gusia!!.isSelected = false
            nuri!!.isSelected = false
            fiftieth!!.isSelected = false
            shalom!!.isSelected = false
            gyo!!.isSelected = false
            init("ALL")
        }
    }

    private inner class gusiaOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            total!!.isSelected = false
            gusia!!.isSelected = true
            nuri!!.isSelected = false
            fiftieth!!.isSelected = false
            shalom!!.isSelected = false
            gyo!!.isSelected = false
            init("gusia")
        }
    }

    private inner class nuriOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            total!!.isSelected = false
            gusia!!.isSelected = false
            nuri!!.isSelected = true
            fiftieth!!.isSelected = false
            shalom!!.isSelected = false
            gyo!!.isSelected = false
            init("nuri")
        }
    }

    private inner class fiftiethOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            total!!.isSelected = false
            gusia!!.isSelected = false
            nuri!!.isSelected = false
            fiftieth!!.isSelected = true
            shalom!!.isSelected = false
            gyo!!.isSelected = false
            init("fiftieth")
        }
    }

    private inner class shalomOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            total!!.isSelected = false
            gusia!!.isSelected = false
            nuri!!.isSelected = false
            fiftieth!!.isSelected = false
            shalom!!.isSelected = true
            gyo!!.isSelected = false
            init("shalom")
        }
    }

    private inner class gyoOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            total!!.isSelected = false
            gusia!!.isSelected = false
            nuri!!.isSelected = false
            fiftieth!!.isSelected = false
            shalom!!.isSelected = false
            gyo!!.isSelected = true

            //init("gyo");
        }
    }
}