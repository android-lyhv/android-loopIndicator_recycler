package com.lyhv.library

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by lyhv on August 15, 2019
 * Copyright @ est-rouge. All rights reserved
 */
abstract class CycleFragmentStatePagerAdapter(
    fm: FragmentManager,
    private var loopCount: Int = IndicatorConfig.LOOP_COUNT
) : FragmentStatePagerAdapter(fm) {
    abstract fun getRealItemSize(): Int
    abstract fun getRealItem(realPosition: Int): Fragment
    final override fun getItem(position: Int): Fragment {
        return getRealItem(getRealPosition(position))
    }

    final override fun getCount(): Int {
        return getRealItemSize() * loopCount
    }

    fun getRealPosition(position: Int): Int {
        val realSize = getRealItemSize()
        return if (realSize <= 0) 0 else position.rem(realSize)
    }
}
