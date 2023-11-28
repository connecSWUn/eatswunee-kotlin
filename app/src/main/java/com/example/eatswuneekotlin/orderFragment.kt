package com.example.eatswuneekotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.eatswuneekotlin.bistro.MyPageAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener

class orderFragment : Fragment() {
    private lateinit var myPageAdapter: MyPageAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private var tabCurrentIdx = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_order, container, false)
        tabLayout = v.findViewById(R.id.myPagetabLayout)
        viewPager = v.findViewById(R.id.myPageviewPager)

        //피드 구성하는 탭레이아웃 + 뷰페이저
        tabLayout.addTab(tabLayout.newTab().setText("전체"))
        tabLayout.addTab(tabLayout.newTab().setText("포아이니"))
        tabLayout.addTab(tabLayout.newTab().setText("분식대첩"))
        tabLayout.addTab(tabLayout.newTab().setText("만권화밥"))
        tabLayout.addTab(tabLayout.newTab().setText("최고당\n돈가스"))

        //커스텀 어댑터 생성
        myPageAdapter = MyPageAdapter(childFragmentManager, tabLayout.getTabCount())
        viewPager.adapter = myPageAdapter
        viewPager.currentItem = tabCurrentIdx
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                tabCurrentIdx = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        return v
    }
}