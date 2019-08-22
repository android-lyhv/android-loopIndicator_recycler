package com.lyhv.loopindicator

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.content_fragment.*

class ContentFragment : Fragment() {
    private lateinit var mMyCyclePagerAdapter: MyCyclePagerAdapter
    private var isShowDefault: Boolean = false
    private var titleItems: ArrayList<String> = ArrayList()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mMyCyclePagerAdapter = MyCyclePagerAdapter(childFragmentManager, titleItems)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.adapter = mMyCyclePagerAdapter
        context?.let { myRecyclerTabLayout.setUpWithViewPager(it, viewPager, mMyCyclePagerAdapter) }
        createTitleItems(false)
        btnSwitch.setOnClickListener {
            isShowDefault = !isShowDefault
            createTitleItems(isShowDefault)
        }
    }


    private fun createTitleItems(isShowDefault: Boolean) {
        Toast.makeText(context, "Change Mode", Toast.LENGTH_SHORT).show()
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
        mMyCyclePagerAdapter.setItems(titleItems)
        mMyCyclePagerAdapter.notifyDataSetChanged()
        myRecyclerTabLayout.setCurrentItem(myRecyclerTabLayout.getItemCenterPosition(0), false)
    }
}
