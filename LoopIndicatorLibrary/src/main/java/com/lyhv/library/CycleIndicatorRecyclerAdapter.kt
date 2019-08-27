package com.lyhv.library

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView


class CycleIndicatorRecyclerAdapter(
    context: Context,
    var style: RecyclerIndicatorStyle,
    var cycleFragmentStatePagerAdapter: CycleFragmentStatePagerAdapter
) :
    CycleRecyclerTabLayout.Adapter<CycleIndicatorRecyclerAdapter.IndicatorViewHolder>(context) {
    var onItemListener: OnIndicatorItemListener? = null
    private var mDrawableIndicator: GradientDrawable = GradientDrawable().apply {
        setColor(style.mIndicatorPaint.color)
        cornerRadius = style.mIndicatorCorner.toFloat()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndicatorViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.indicator_view, parent, false)
        return IndicatorViewHolder(view)
    }

    override fun onBindViewHolder(holderIndicator: IndicatorViewHolder, position: Int) {
        holderIndicator.onBind()
    }


    override fun getItemCount(): Int {
        return cycleFragmentStatePagerAdapter.count
    }


    inner class IndicatorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)

        init {
            applyStyle(itemView)
            itemView.setOnClickListener {
                onItemListener?.onItemPositionClicked(
                    adapterPosition,
                    getRealPosition(adapterPosition)
                )
            }
        }

        private fun applyStyle(itemView: View) {
            itemView.apply {
                layoutParams = if (style.mTabPadding > 0) {
                    setPadding(style.mTabPadding)
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        style.mTabHeight
                    )
                } else {
                    setPadding(
                        style.mTabPaddingStart,
                        style.mTabPaddingTop,
                        style.mTabPaddingEnd,
                        style.mTabPaddingBottom
                    )
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        style.mTabHeight
                    )
                }

            }
        }

        @SuppressLint("SetTextI18n")
        fun onBind() {
            val isActive =
                getRealPosition(currentIndicatorPosition) == getRealPosition(adapterPosition)
            if (isActive && currentIndicatorPosition != adapterPosition) {
                tvTitle.background = mDrawableIndicator
            } else {
                tvTitle.setBackgroundResource(android.R.color.transparent)
            }
            tvTitle.text = cycleFragmentStatePagerAdapter.getPageTitle(adapterPosition)
            if (isActive) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tvTitle.setTextAppearance(style.mTabTextAppearanceActive)
                } else {
                    tvTitle.setTextAppearance(context, style.mTabTextAppearanceActive)
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tvTitle.setTextAppearance(style.mTabTextAppearanceInActive)
                } else {
                    tvTitle.setTextAppearance(context, style.mTabTextAppearanceInActive)
                }
            }
        }
    }

    fun getRealPosition(position: Int): Int {
        val realSize = cycleFragmentStatePagerAdapter.getRealItemSize()
        return if (realSize <= 0) 0 else position.rem(realSize)
    }

    interface OnIndicatorItemListener {
        fun onItemPositionClicked(positionIndex: Int, realPosition: Int)
    }
}