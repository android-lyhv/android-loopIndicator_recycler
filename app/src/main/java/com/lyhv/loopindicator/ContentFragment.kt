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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mMyCyclePagerAdapter = MyCyclePagerAdapter(childFragmentManager, listOf())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.content_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.adapter = mMyCyclePagerAdapter
        context?.let { myRecyclerTabLayout.setUpWithViewPager(it, viewPager, mMyCyclePagerAdapter) }
        createTitleItems(false)
        btnSwitch.setOnClickListener {
            createTitleItems(!btnSwitch.isSelected)
            btnSwitch.isSelected = !btnSwitch.isSelected

        }
    }


    private fun createTitleItems(isShowDefault: Boolean) {
        Toast.makeText(context, "Change Mode", Toast.LENGTH_SHORT).show()
        val titleItems: ArrayList<String> = ArrayList()
        if (isShowDefault) {
            for (index in 0 until 5) {
                titleItems.add("Tab $index")
            }
        } else {
            titleItems.add("マイアスリート")
            titleItems.add("ピックアップ")
            for (index in 0 until 5) {
                titleItems.add("Index $index")
            }
            titleItems.add("クリップ")
        }
        mMyCyclePagerAdapter.setItems(titleItems)
        mMyCyclePagerAdapter.notifyDataSetChanged()
        myRecyclerTabLayout.setCenterPositionItem(0)
    }
}
