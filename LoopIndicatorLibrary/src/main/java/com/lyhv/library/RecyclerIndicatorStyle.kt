package com.lyhv.library

import android.content.Context
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet

/**
 * Created by lyhv on August 22, 2019
 * Copyright @ est-rouge. All rights reserved
 */
class RecyclerIndicatorStyle {
    val mIndicatorPaint: Paint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    var mTabTextAppearanceActive: Int = 0
    var mTabTextAppearanceInActive: Int = 0
    var mTabOnScreenLimit: Int = 0
    var mTabMinWidth: Int = 0
    var mTabMaxWidth: Int = 0
    var mTabPaddingStart: Int = 0
    var mTabPadding: Int = 0
    var mTabPaddingTop: Int = 0
    var mTabPaddingEnd: Int = 0
    var mTabPaddingBottom: Int = 0
    var mIndicatorHeight: Int = 0
    var mIndicatorCorner: Int = 0
    var mTabHeight: Int = 0
    var mIndicatorLoopCount: Int = IndicatorConfig.LOOP_COUNT

    fun applyStyle(context: Context, attrs: AttributeSet, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.rtl_RecyclerTabLayout,
            defStyle, R.style.rtl_RecyclerTabLayout
        )
        mIndicatorPaint.color = typedArray.getColor(
            R.styleable
                .rtl_RecyclerTabLayout_rtl_tabIndicatorColor, 0
        )

        mIndicatorHeight = typedArray.getDimensionPixelSize(
            R.styleable
                .rtl_RecyclerTabLayout_rtl_tabIndicatorHeight, 0
        )


        mTabHeight = typedArray.getDimensionPixelSize(
            R.styleable.rtl_RecyclerTabLayout_rtl_tabHeight,
            0
        )

        mIndicatorCorner = typedArray.getDimensionPixelSize(
            R.styleable
                .rtl_RecyclerTabLayout_rtl_tabIndicatorCorner, 0
        )

        mTabTextAppearanceActive = typedArray.getResourceId(
            R.styleable.rtl_RecyclerTabLayout_rtl_tabTextAppearanceActive,
            R.style.style_tabTextAppearanceActive
        )
        mTabTextAppearanceInActive = typedArray.getResourceId(
            R.styleable.rtl_RecyclerTabLayout_rtl_tabTextAppearanceInActive,
            R.style.style_tabTextAppearanceInActive
        )
        mIndicatorLoopCount = typedArray.getInteger(
            R.styleable.rtl_RecyclerTabLayout_rtl_loopCount,
            IndicatorConfig.LOOP_COUNT
        )

        mTabPaddingBottom = typedArray
            .getDimensionPixelSize(R.styleable.rtl_RecyclerTabLayout_rtl_tabPadding, 0)
        mTabPaddingEnd = mTabPaddingBottom
        mTabPaddingTop = mTabPaddingEnd
        mTabPaddingStart = mTabPaddingTop
        mTabPaddingStart = typedArray.getDimensionPixelSize(
            R.styleable.rtl_RecyclerTabLayout_rtl_tabPaddingStart, mTabPaddingStart
        )
        mTabPaddingTop = typedArray.getDimensionPixelSize(
            R.styleable.rtl_RecyclerTabLayout_rtl_tabPaddingTop, mTabPaddingTop
        )
        mTabPaddingEnd = typedArray.getDimensionPixelSize(
            R.styleable.rtl_RecyclerTabLayout_rtl_tabPaddingEnd, mTabPaddingEnd
        )
        mTabPaddingBottom = typedArray.getDimensionPixelSize(
            R.styleable.rtl_RecyclerTabLayout_rtl_tabPaddingBottom, mTabPaddingBottom
        )
        mTabPadding = typedArray.getDimensionPixelSize(
            R.styleable.rtl_RecyclerTabLayout_rtl_tabPadding, 0
        )
        mTabOnScreenLimit = typedArray.getInteger(
            R.styleable.rtl_RecyclerTabLayout_rtl_tabOnScreenLimit, 0
        )
        if (mTabOnScreenLimit == 0) {
            mTabMinWidth = typedArray.getDimensionPixelSize(
                R.styleable.rtl_RecyclerTabLayout_rtl_tabMinWidth, 0
            )
            mTabMaxWidth = typedArray.getDimensionPixelSize(
                R.styleable.rtl_RecyclerTabLayout_rtl_tabMaxWidth, 0
            )
        }
        typedArray.recycle()
    }

}
