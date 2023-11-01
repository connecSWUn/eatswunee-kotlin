package com.example.eatswuneekotlin.bistro

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MyPageAdapter(fm: FragmentManager?, private val numberOfFragment: Int) :
    FragmentStatePagerAdapter(
        fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> Phoini_BistroFragment()
            2 -> Bunsik_BistroFragment()
            3 -> Mankwon_BistroFragment()
            4 -> Choigodang_BistroFragment()
            else -> total_bistroFragment()
        }
    }

    override fun getCount(): Int {
        return numberOfFragment
    }
}