package com.ctpi.senasoftcauca.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ctpi.senasoftcauca.MenuActivity
import com.ctpi.senasoftcauca.R

private val TAB_TITLES = arrayOf(
    R.string.Login,
    R.string.Login2,
    R.string.Login3
)

class SectionsPagerAdapter(private val context: MenuActivity, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position + 1, context)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}