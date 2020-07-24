package com.example.androidkotlintemplate

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class PageAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager){
    override fun getItem(position: Int): PageFragment {
        var fragment:PageFragment = PageFragment.newInstance()
        fragment.position = position
        return fragment
    }

    override fun getCount(): Int {
        return 4
    }
}