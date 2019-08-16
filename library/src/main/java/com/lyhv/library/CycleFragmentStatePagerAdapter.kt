package com.lyhv.library

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by lyhv on August 15, 2019
 * Copyright @ est-rouge. All rights reserved
 */
abstract class CycleFragmentStatePagerAdapter(fm: FragmentManager, private var loopCount: Int = IndicatorConfig.LOOP_COUNT) : FragmentStatePagerAdapter(fm) {
    abstract fun getRealItemSize(): Int
    abstract fun getRealItem(position: Int): Fragment
    override fun getItem(position: Int): Fragment {
        return getRealItem(position.rem(getRealItemSize()))
    }

    override fun getCount(): Int {
        return getRealItemSize() * loopCount
    }
}
