package com.lyhv.loopindicator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var mMyPagerAdapter: MyPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMyPagerAdapter = MyPagerAdapter(supportFragmentManager)
        viewPager.adapter = mMyPagerAdapter
        onInit()
    }

    private fun onInit() {
        // recyclerTabLayout.setUpWithViewPager(viewPager)
        myRecyclerTabLayout.setUpWithViewPager(this, viewPager, mMyPagerAdapter)
        viewPager.currentItem =
            myRecyclerTabLayout.getItemCenterPosition(Random.nextInt(mMyPagerAdapter.getRealItemSize()))
    }
}
