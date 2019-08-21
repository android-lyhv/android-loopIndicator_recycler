package com.lyhv.library

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CycleIndicatorRecyclerAdapter(
    context: Context,
    var cycleFragmentStatePagerAdapter: CycleFragmentStatePagerAdapter,
    var loopCount: Int = IndicatorConfig.LOOP_COUNT
) :
    CycleRecyclerTabLayout.Adapter<CycleIndicatorRecyclerAdapter.IndicatorViewHolder>(context) {
    var onItemListener: OnIndicatorItemListener? = null
    var textTitleColor: Int = 0
    protected var mTabSelectedTextColorSet: Boolean = false
    protected var mTabSelectedTextColor: Int = 0
    protected var mTabNormalTextColorSet: Boolean = false
    protected var mTabNormalTextColor: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndicatorViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.indicator_view, parent, false)
        return IndicatorViewHolder(view)
    }

    override fun onBindViewHolder(holderIndicator: IndicatorViewHolder, position: Int) {
        holderIndicator.tvTitle.isSelected = currentIndicatorPosition == position
        holderIndicator.onBind()
    }


    override fun getItemCount(): Int {
        return cycleFragmentStatePagerAdapter.getRealItemSize() * loopCount
    }


    inner class IndicatorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)

        init {
            tvTitle.setTextColor(textTitleColor)
        }

        init {
            itemView.setOnClickListener {
                onItemListener?.onItemPositionClicked(adapterPosition, getRealPosition(adapterPosition))
            }
        }

        @SuppressLint("SetTextI18n")
        fun onBind() {
            tvTitle.text = cycleFragmentStatePagerAdapter.getPageTitle(adapterPosition)
            if (tvTitle.isSelected) {
                tvTitle.setTextColor(mTabSelectedTextColor)
            } else {
                tvTitle.setTextColor(mTabNormalTextColor)
            }
        }
    }

    fun setTabSelectedTextColor(
        tabSelectedTextColorSet: Boolean,
        tabSelectedTextColor: Int
    ) {
        mTabSelectedTextColorSet = tabSelectedTextColorSet
        mTabSelectedTextColor = tabSelectedTextColor
    }

    fun setTabNormalTextColor(
        tabNormalTextColorSet: Boolean,
        tabNormalTextColor: Int
    ) {
        mTabNormalTextColorSet = tabNormalTextColorSet
        mTabNormalTextColor = tabNormalTextColor
    }

    fun getRealPosition(position: Int) = position.rem(cycleFragmentStatePagerAdapter.getRealItemSize())

    interface OnIndicatorItemListener {
        fun onItemPositionClicked(positionIndex: Int, realPosition: Int)
    }
}