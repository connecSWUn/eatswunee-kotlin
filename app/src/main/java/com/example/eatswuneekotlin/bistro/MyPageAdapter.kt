package com.example.eatswuneekotlin.bistro

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.eatswuneekotlin.bistro.bunsik_bistroFragment

class MyPageAdapter(fm: FragmentManager?, private val numberOfFragment: Int) :
    FragmentStatePagerAdapter(
        fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> phoini_bistroFragment()
            2 -> bunsik_bistroFragment()
            3 -> mankwon_bistroFragment()
            4 -> choigodang_bistroFragment()
            else -> total_bistroFragment()
        }
    }

    override fun getCount(): Int {
        return numberOfFragment
    }
}