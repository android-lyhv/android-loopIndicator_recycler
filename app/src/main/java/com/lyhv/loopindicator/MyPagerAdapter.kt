package com.lyhv.loopindicator

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.lyhv.library.CycleFragmentStatePagerAdapter

/**
 * Created by lyhv on August 15, 2019
 * Copyright @ est-rouge. All rights reserved
 */
class MyPagerAdapter(fm: FragmentManager) : CycleFragmentStatePagerAdapter(fm) {
    override fun getRealItemSize(): Int {
        return 14
    }

    override fun getRealItem(position: Int): Fragment {
        return ItemFragment.newInstance(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "${position.rem(getRealItemSize())}"
    }
}
