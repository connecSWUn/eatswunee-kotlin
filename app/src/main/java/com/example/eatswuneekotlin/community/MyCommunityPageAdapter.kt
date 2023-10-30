package com.example.eatswuneekotlin.community

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.eatswuneekotlin.community.finding_articlesFragment

class MyCommunityPageAdapter(fm: FragmentManager?, private val numberOfFragment: Int) :
    FragmentStatePagerAdapter(
        fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> connecting_articlesFragment()
            2 -> found_articlesFragment()
            else -> finding_articlesFragment()
        }
    }

    override fun getCount(): Int {
        return numberOfFragment
    }
}