package com.lyhv.loopindicator

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.lyhv.library.CycleFragmentStatePagerAdapter

/**
 * Created by lyhv on August 15, 2019
 * Copyright @ est-rouge. All rights reserved
 */
class MyPagerAdapter(fm: FragmentManager, items: List<String>) : CycleFragmentStatePagerAdapter(fm) {
    private val itemList: ArrayList<String> = ArrayList()

    init {
        itemList.addAll(items)
    }

    fun setItems(items: List<String>) {
        this.itemList.clear()
        this.itemList.addAll(items)
    }

    override fun getRealItemSize(): Int {
        return itemList.size
    }

    override fun getRealItem(position: Int): Fragment {
        return ItemFragment.newInstance(position, itemList[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return itemList.get(position.rem(getRealItemSize()))
    }
}
