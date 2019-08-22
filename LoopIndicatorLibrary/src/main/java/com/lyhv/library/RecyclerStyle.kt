package com.lyhv.library

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet

/**
 * Created by lyhv on August 22, 2019
 * Copyright @ est-rouge. All rights reserved
 */
class RecyclerStyle {
    var mIndicatorPaint: Paint = Paint()
    protected var mTabBackgroundResId: Int = 0
    protected var mTabOnScreenLimit: Int = 0
    var mTabMinWidth: Int = 0
    var mTabMaxWidth: Int = 0
    protected var mTabTextAppearance: Int = 0
    var mTabSelectedTextColor: Int = 0
    var mTabNormalTextColor: Int = 0
    var mTabSelectedTextColorSet: Boolean = false
    var mTabNormalTextColorSet: Boolean = false
    protected var mTabPaddingStart: Int = 0
    protected var mTabPaddingTop: Int = 0
    protected var mTabPaddingEnd: Int = 0
    protected var mTabPaddingBottom: Int = 0
    var mIndicatorHeight: Int = 0
    var mIndicatorRadius: Int = 0
    protected var mIndicatorPadding: Int = 0
    protected var mScrollEnabled: Boolean = false

    fun applyStyle(context: Context, attrs: AttributeSet, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.rtl_RecyclerTabLayout,
                defStyle, R.style.rtl_RecyclerTabLayout
        )

        mIndicatorPaint.color = typedArray.getColor(R.styleable.rtl_RecyclerTabLayout_rtl_tabIndicatorColor, 0)
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.rtl_RecyclerTabLayout_rtl_tabIndicatorHeight, 0)
        mIndicatorPadding = typedArray.getDimensionPixelSize(R.styleable.rtl_RecyclerTabLayout_rtl_tabIndicatorPadding, 0)
        mIndicatorRadius = typedArray.getDimensionPixelSize(R.styleable.rtl_RecyclerTabLayout_rtl_tabIndicatorCorner, 0)
        mTabTextAppearance = typedArray.getResourceId(R.styleable.rtl_RecyclerTabLayout_rtl_tabTextAppearance, R.style.rtl_RecyclerTabLayout_Tab)
        mTabPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.rtl_RecyclerTabLayout_rtl_tabPadding, 0)
        mTabPaddingEnd = mTabPaddingBottom
        mTabPaddingTop = mTabPaddingEnd
        mTabPaddingStart = mTabPaddingTop
        mTabPaddingStart = typedArray.getDimensionPixelSize(R.styleable.rtl_RecyclerTabLayout_rtl_tabPaddingStart, mTabPaddingStart)
        mTabPaddingTop = typedArray.getDimensionPixelSize(R.styleable.rtl_RecyclerTabLayout_rtl_tabPaddingTop, mTabPaddingTop)
        mTabPaddingEnd = typedArray.getDimensionPixelSize(R.styleable.rtl_RecyclerTabLayout_rtl_tabPaddingEnd, mTabPaddingEnd)
        mTabPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.rtl_RecyclerTabLayout_rtl_tabPaddingBottom, mTabPaddingBottom)

        if (typedArray.hasValue(R.styleable.rtl_RecyclerTabLayout_rtl_tabSelectedTextColor)) {
            mTabSelectedTextColor = typedArray
                    .getColor(R.styleable.rtl_RecyclerTabLayout_rtl_tabSelectedTextColor, 0)
            mTabSelectedTextColorSet = true
        }

        if (typedArray.hasValue(R.styleable.rtl_RecyclerTabLayout_rtl_tabNormalTextColor)) {
            mTabNormalTextColor = typedArray
                    .getColor(R.styleable.rtl_RecyclerTabLayout_rtl_tabNormalTextColor, 0)
            mTabNormalTextColorSet = true
        }
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

        mTabBackgroundResId = typedArray.getResourceId(R.styleable.rtl_RecyclerTabLayout_rtl_tabBackground, 0)
        mScrollEnabled = typedArray.getBoolean(R.styleable.rtl_RecyclerTabLayout_rtl_scrollEnabled, true)
        typedArray.recycle()
    }

}
