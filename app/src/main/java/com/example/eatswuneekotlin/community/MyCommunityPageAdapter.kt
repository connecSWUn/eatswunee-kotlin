package com.example.eatswuneekotlin.community

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MyCommunityPageAdapter(fm: FragmentManager?, private val numberOfFragment: Int) :
    FragmentStatePagerAdapter(
        fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> Connecting_ArticlesFragment()
            2 -> Found_ArticlesFragment()
            else -> Finding_ArticlesFragment()
        }
    }

    override fun getCount(): Int {
        return numberOfFragment
    }
}