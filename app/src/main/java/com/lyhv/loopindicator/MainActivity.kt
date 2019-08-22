package com.lyhv.loopindicator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mMyPagerAdapter: MyPagerAdapter
    private var isShowDefault: Boolean = false
    private var titleItems: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMyPagerAdapter = MyPagerAdapter(supportFragmentManager, titleItems)
        viewPager.adapter = mMyPagerAdapter
        myRecyclerTabLayout.setUpWithViewPager(this, viewPager, mMyPagerAdapter)
        onInit()
    }

    private fun onInit() {
        btnSwitch.setOnClickListener {
            isShowDefault = !isShowDefault
            createTitleItems(isShowDefault)
        }
        createTitleItems(false)
    }

    private fun createTitleItems(isShowDefault: Boolean) {
        Toast.makeText(this, "Change Mode", Toast.LENGTH_SHORT).show()
        if (isShowDefault) {
            titleItems.clear()
            titleItems.add(
                "Tab 0" +
                        "1"
            )
            titleItems.add("Tab 1")
            titleItems.add("Tab 2")
            titleItems.add("Tab 3")
            titleItems.add("Tab 4")
            titleItems.add("Tab 5")
        } else {
            titleItems.clear()
            titleItems.add("マイアスリート")
            titleItems.add("ピックアップ")
            for (index in 0 until 9) {
                titleItems.add("Index $index")
            }
            titleItems.add("クリップ")
        }
        mMyPagerAdapter.setItems(titleItems)
        mMyPagerAdapter.notifyDataSetChanged()
        viewPager.setCurrentItem(myRecyclerTabLayout.getItemCenterPosition(0), false)
    }
}
