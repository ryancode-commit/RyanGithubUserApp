package com.ra.ryangithubuserapp.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ra.ryangithubuserapp.R
import com.ra.ryangithubuserapp.ui.fragment.FollowersFragment
import com.ra.ryangithubuserapp.ui.fragment.FollowingFragment

class SectionsPagerAdapter (private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)  {
    var username : String? = null


    override fun getCount(): Int {
        return 2
    }
    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0-> fragment = FollowersFragment.newInstance(username)
            1-> fragment = FollowingFragment.newInstance(username)
        }
        return fragment as Fragment
    }
    @StringRes
    private val  TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
    )
}