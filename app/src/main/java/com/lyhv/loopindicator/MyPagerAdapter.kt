package com.lyhv.loopindicator

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.lyhv.library.CycleFragmentStatePagerAdapter

/**
 * Created by lyhv on August 15, 2019
 * Copyright @ est-rouge. All rights reserved
 */
class MyPagerAdapter(fm: FragmentManager, items: List<String>) : CycleFragmentStatePagerAdapter(fm) {
    private val mItemList: ArrayList<String> = ArrayList()

    init {
        mItemList.addAll(items)
    }

    fun setItems(items: List<String>) {
        this.mItemList.clear()
        this.mItemList.addAll(items)
    }

    override fun getRealItemSize(): Int {
        return mItemList.size
    }

    override fun getRealItem(realPosition: Int): Fragment {
        return ItemFragment.newInstance(realPosition, mItemList[realPosition])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mItemList[getRealPosition(position)]
    }
}
