package com.example.eatswuneekotlin.bistro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.eatswuneekotlin.MainActivity
import com.example.eatswuneekotlin.MasterApplication
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.bistro.recyclerView.MyBistroAdapter
import com.example.eatswuneekotlin.bistro.recyclerView.MyViewPagerAdapter
import com.example.eatswuneekotlin.server.Result
import com.example.eatswuneekotlin.server.RetrofitClient
import com.example.eatswuneekotlin.server.ServiceApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Phoini_BistroFragment : Fragment() {
    private lateinit var v: View
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var bistroAdapter: MyBistroAdapter
    private lateinit var viewPagerAdapter: MyViewPagerAdapter
    private lateinit var viewPager: ViewPager2

    private lateinit var activity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_total_bistro, container, false)
        init(1)

        mRecyclerView = v.findViewById(R.id.total_RecyclerView)
        viewPager = v.findViewById(R.id.view_pager)

        val gridLayoutManager = GridLayoutManager(activity, 2)
        mRecyclerView.layoutManager = gridLayoutManager

        return v
    }

    private fun init(restaurantId: Long) {
        val masterApp = MasterApplication()
        masterApp.createRetrofit(activity)

        val service = masterApp.serviceApi

        service.getData("gusia", restaurantId)?.enqueue(object : Callback<Result?> {
            override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                val result = response.body()
                val data = result!!.data

                if (data?.homeOrdersList == null) {
                    viewPager!!.visibility = View.GONE
                } else {

                    viewPagerAdapter = MyViewPagerAdapter(data?.homeOrdersList!!)
                    viewPager!!.adapter = viewPagerAdapter
                    viewPager!!.orientation = ViewPager2.ORIENTATION_HORIZONTAL

                }

                bistroAdapter = MyBistroAdapter(data?.menusList!!)
                mRecyclerView!!.adapter = bistroAdapter
            }

            override fun onFailure(call: Call<Result?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}