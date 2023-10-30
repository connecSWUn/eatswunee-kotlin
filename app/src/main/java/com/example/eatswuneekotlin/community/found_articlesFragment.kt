package com.example.eatswuneekotlin.community

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.server.RetrofitClient
import com.example.eatswuneekotlin.server.ServiceApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class found_articlesFragment : Fragment() {
    private var v: View? = null
    private var mRecyclerView: RecyclerView? = null
    private var adapter: MyArticlesAdapter? = null
    private var retrofitClient: RetrofitClient? = null
    private var serviceApi: ServiceApi? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_finding_articles, container, false)
        mRecyclerView = v.findViewById(R.id.finding_RecyclerView)
        mRecyclerView.addItemDecoration(RecyclerViewDecoration(20))

        /* initiate recyclerView */mRecyclerView.setLayoutManager(LinearLayoutManager(context))
        mRecyclerView.setLayoutManager(LinearLayoutManager(context, RecyclerView.VERTICAL, false))
        init("COMPLETED")
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
        retrofitClient = RetrofitClient.instance
        serviceApi = RetrofitClient.serviceApi
        serviceApi.getArticles(category).enqueue(object : Callback<Result?> {
            override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                val result = response.body()
                val data = result!!.data
                Log.d("retrofit", "Data fetch success")
                /* initiate adapter */adapter = MyArticlesAdapter(data.postsList)
                mRecyclerView!!.adapter = adapter
            }

            override fun onFailure(call: Call<Result?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}