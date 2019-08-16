/**
 * Copyright (C) 2015 nshmura
 * Copyright (C) 2015 The Android Open Source Project
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lyhv.library

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager

open class MyRecyclerTabLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        RecyclerView(context, attrs, defStyle) {

    protected var mIndicatorPaint: Paint
    protected var mTabBackgroundResId: Int = 0
    protected var mTabOnScreenLimit: Int = 0
    protected var mTabMinWidth: Int = 0
    protected var mTabMaxWidth: Int = 0
    protected var mTabTextAppearance: Int = 0
    protected var mTabSelectedTextColor: Int = 0
    protected var mTabSelectedTextColorSet: Boolean = false
    protected var mTabPaddingStart: Int = 0
    protected var mTabPaddingTop: Int = 0
    protected var mTabPaddingEnd: Int = 0
    protected var mTabPaddingBottom: Int = 0
    protected var mIndicatorHeight: Int = 0

    protected var mLinearLayoutManager: LinearLayoutManager
    protected var mRecyclerOnScrollListener: RecyclerOnScrollListener? = null
    protected var mViewPager: ViewPager? = null
    protected var mAdapter: Adapter<*>? = null

    protected var mIndicatorPosition: Int = 0
    protected var mIndicatorGap: Int = 0
    protected var mIndicatorScroll: Int = 0
    protected var mOldPositionOffset: Float = 0.toFloat()
    protected var mPositionThreshold: Float = 0.toFloat()
    protected var mRequestScrollToTab: Boolean = false
    protected var mScrollEanbled: Boolean = false
    private var mOldPosition: Int = 0
    private var mOldScrollOffset: Int = 0

    private val isLayoutRtl: Boolean
        get() = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL

    init {
        setWillNotDraw(false)
        mIndicatorPaint = Paint()
        getAttributes(context, attrs!!, defStyle)
        mLinearLayoutManager = object : LinearLayoutManager(getContext()) {
            override fun canScrollHorizontally(): Boolean {
                return mScrollEanbled
            }
        }
        mLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        layoutManager = mLinearLayoutManager
        itemAnimator = null
        mPositionThreshold = DEFAULT_POSITION_THRESHOLD
    }

    private fun getAttributes(context: Context, attrs: AttributeSet, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.rtl_RecyclerTabLayout,
                defStyle, R.style.rtl_RecyclerTabLayout)
        setIndicatorColor(a.getColor(R.styleable
                .rtl_RecyclerTabLayout_rtl_tabIndicatorColor, 0))
        setIndicatorHeight(a.getDimensionPixelSize(R.styleable
                .rtl_RecyclerTabLayout_rtl_tabIndicatorHeight, 0))

        mTabTextAppearance = a.getResourceId(R.styleable.rtl_RecyclerTabLayout_rtl_tabTextAppearance,
                R.style.rtl_RecyclerTabLayout_Tab)

        mTabPaddingBottom = a
                .getDimensionPixelSize(R.styleable.rtl_RecyclerTabLayout_rtl_tabPadding, 0)
        mTabPaddingEnd = mTabPaddingBottom
        mTabPaddingTop = mTabPaddingEnd
        mTabPaddingStart = mTabPaddingTop
        mTabPaddingStart = a.getDimensionPixelSize(
                R.styleable.rtl_RecyclerTabLayout_rtl_tabPaddingStart, mTabPaddingStart)
        mTabPaddingTop = a.getDimensionPixelSize(
                R.styleable.rtl_RecyclerTabLayout_rtl_tabPaddingTop, mTabPaddingTop)
        mTabPaddingEnd = a.getDimensionPixelSize(
                R.styleable.rtl_RecyclerTabLayout_rtl_tabPaddingEnd, mTabPaddingEnd)
        mTabPaddingBottom = a.getDimensionPixelSize(
                R.styleable.rtl_RecyclerTabLayout_rtl_tabPaddingBottom, mTabPaddingBottom)

        if (a.hasValue(R.styleable.rtl_RecyclerTabLayout_rtl_tabSelectedTextColor)) {
            mTabSelectedTextColor = a
                    .getColor(R.styleable.rtl_RecyclerTabLayout_rtl_tabSelectedTextColor, 0)
            mTabSelectedTextColorSet = true
        }

        mTabOnScreenLimit = a.getInteger(
                R.styleable.rtl_RecyclerTabLayout_rtl_tabOnScreenLimit, 0)
        if (mTabOnScreenLimit == 0) {
            mTabMinWidth = a.getDimensionPixelSize(
                    R.styleable.rtl_RecyclerTabLayout_rtl_tabMinWidth, 0)
            mTabMaxWidth = a.getDimensionPixelSize(
                    R.styleable.rtl_RecyclerTabLayout_rtl_tabMaxWidth, 0)
        }

        mTabBackgroundResId = a
                .getResourceId(R.styleable.rtl_RecyclerTabLayout_rtl_tabBackground, 0)
        mScrollEanbled = a.getBoolean(R.styleable.rtl_RecyclerTabLayout_rtl_scrollEnabled, true)
        a.recycle()
    }


    override fun onDetachedFromWindow() {
        if (mRecyclerOnScrollListener != null) {
            removeOnScrollListener(mRecyclerOnScrollListener!!)
            mRecyclerOnScrollListener = null
        }
        super.onDetachedFromWindow()
    }


    private fun setIndicatorColor(color: Int) {
        mIndicatorPaint.color = color
    }

    private fun setIndicatorHeight(indicatorHeight: Int) {
        mIndicatorHeight = indicatorHeight
    }

    fun setAutoSelectionMode(autoSelect: Boolean) {
        if (mRecyclerOnScrollListener != null) {
            removeOnScrollListener(mRecyclerOnScrollListener!!)
            mRecyclerOnScrollListener = null
        }
        if (autoSelect) {
            mRecyclerOnScrollListener = RecyclerOnScrollListener(this, mLinearLayoutManager)
            addOnScrollListener(mRecyclerOnScrollListener!!)
        }
    }

    fun setPositionThreshold(positionThreshold: Float) {
        mPositionThreshold = positionThreshold
    }

    fun setUpWithViewPager(context: Context, titlesItems: List<String>, viewPager: ViewPager) {
        mViewPager = viewPager
        val adapter = IndicatorRecyclerAdapter(context, titlesItems)
        adapter.onItemListener = object : IndicatorRecyclerAdapter.OnIndicatorItemListener {
            override fun onItemPositionClicked(positionIndex: Int, realPosition: Int) {
                viewPager.currentItem = positionIndex
            }
        }
        setUpWithAdapter(adapter)
    }

    private fun setUpWithAdapter(adapter: Adapter<*>) {
        mAdapter = adapter
        if (mViewPager?.adapter == null) {
            throw IllegalArgumentException("ViewPager does not have a PagerAdapter set")
        }
        viewPagerListener = ViewPagerOnPageChangeListener(this)
        mViewPager?.addOnPageChangeListener(viewPagerListener!!)
        setAdapter(adapter)
        scrollToTab(mViewPager!!.currentItem)
    }

    fun setCurrentItem(position: Int, smoothScroll: Boolean) {
        if (mViewPager != null) {
            mViewPager?.setCurrentItem(position, smoothScroll)
            scrollToTab(mViewPager?.currentItem ?: 0)
            return
        }

        if (smoothScroll && position != mIndicatorPosition) {
            startAnimation(position)
        } else {
            scrollToTab(position)
        }
    }

    private fun startAnimation(position: Int) {
        var distance = 1f

        val view = mLinearLayoutManager.findViewByPosition(position)
        if (view != null) {
            val currentX = view.x + view.measuredWidth / 2f
            val centerX = measuredWidth / 2f
            distance = Math.abs(centerX - currentX) / view.measuredWidth
        }

        val animator: ValueAnimator
        animator = if (position < mIndicatorPosition) {
            ValueAnimator.ofFloat(distance, 0F)
        } else {
            ValueAnimator.ofFloat(-distance, 0F)
        }
        animator.duration = DEFAULT_SCROLL_DURATION
        animator.addUpdateListener { animation -> scrollToTab(position, animation.animatedValue as Float, true) }
        animator.start()
    }

    fun scrollToTab(position: Int) {
        scrollToTab(position, 0f, false)
        mAdapter?.currentIndicatorPosition = position
        mAdapter?.notifyDataSetChanged()
    }

    fun scrollToTab(position: Int, positionOffset: Float, fitIndicator: Boolean) {
        var scrollOffset = 0
        val selectedView = mLinearLayoutManager.findViewByPosition(position)
        val nextView = mLinearLayoutManager.findViewByPosition(position + 1)

        if (selectedView != null) {
            val width = measuredWidth
            val sLeft =
                    if (position == 0) 0F else width / 2f - selectedView.measuredWidth / 2f // left edge of selected tab
            val sRight = sLeft + selectedView.measuredWidth // right edge of selected tab

            if (nextView != null) {
                val nLeft = width / 2f - nextView.measuredWidth / 2f // left edge of next tab
                val distance = sRight - nLeft // total distance that is needed to distance to next tab
                val dx = distance * positionOffset
                scrollOffset = (sLeft - dx).toInt()

                if (position == 0) {
                    val indicatorGap = ((nextView.measuredWidth - selectedView.measuredWidth) / 2).toFloat()
                    mIndicatorGap = (indicatorGap * positionOffset).toInt()
                    mIndicatorScroll = ((selectedView.measuredWidth + indicatorGap) * positionOffset).toInt()

                } else {
                    val indicatorGap = ((nextView.measuredWidth - selectedView.measuredWidth) / 2).toFloat()
                    mIndicatorGap = (indicatorGap * positionOffset).toInt()
                    mIndicatorScroll = dx.toInt()
                }

            } else {
                scrollOffset = sLeft.toInt()
                mIndicatorScroll = 0
                mIndicatorGap = 0
            }
            if (fitIndicator) {
                mIndicatorScroll = 0
                mIndicatorGap = 0
            }

        } else {
            if (measuredWidth > 0 && mTabMaxWidth > 0 && mTabMinWidth == mTabMaxWidth) { //fixed size
                val width = mTabMinWidth
                val offset = (positionOffset * -width).toInt()
                val leftOffset = ((measuredWidth - width) / 2f).toInt()
                scrollOffset = offset + leftOffset
            }
            mRequestScrollToTab = true
        }

        updateCurrentIndicatorPosition(position, positionOffset - mOldPositionOffset, positionOffset)
        mIndicatorPosition = position

        stopScroll()

        if (position != mOldPosition || scrollOffset != mOldScrollOffset) {
            mLinearLayoutManager.scrollToPositionWithOffset(position, scrollOffset)
        }
        if (mIndicatorHeight > 0) {
            invalidate()
        }

        mOldPosition = position
        mOldScrollOffset = scrollOffset
        mOldPositionOffset = positionOffset
    }

    private fun updateCurrentIndicatorPosition(position: Int, dx: Float, positionOffset: Float) {
        if (mAdapter == null) {
            return
        }
        var indicatorPosition = -1
        if (dx > 0 && positionOffset >= mPositionThreshold - POSITION_THRESHOLD_ALLOWABLE) {
            indicatorPosition = position + 1

        } else if (dx < 0 && positionOffset <= 1 - mPositionThreshold + POSITION_THRESHOLD_ALLOWABLE) {
            indicatorPosition = position
        }
        if (indicatorPosition >= 0 && indicatorPosition != mAdapter!!.currentIndicatorPosition) {
            mAdapter?.currentIndicatorPosition = indicatorPosition
            mAdapter?.notifyDataSetChanged()
        }
    }

    override fun onDraw(canvas: Canvas) {
        val view = mLinearLayoutManager.findViewByPosition(mIndicatorPosition)
        if (view == null) {
            if (mRequestScrollToTab) {
                mRequestScrollToTab = false
                scrollToTab(mViewPager!!.currentItem)
            }
            return
        }
        mRequestScrollToTab = false

        val left: Int
        val right: Int
        if (isLayoutRtl) {
            left = view.left - mIndicatorScroll - mIndicatorGap
            right = view.right - mIndicatorScroll + mIndicatorGap
        } else {
            left = view.left + mIndicatorScroll - mIndicatorGap
            right = view.right + mIndicatorScroll + mIndicatorGap
        }

        val top = height - mIndicatorHeight
        val bottom = height

        canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mIndicatorPaint)
    }

    protected open class RecyclerOnScrollListener(
            private var mRecyclerTabLayout: MyRecyclerTabLayout,
            private var mLinearLayoutManager: LinearLayoutManager
    ) : RecyclerView.OnScrollListener() {

        var mDx: Int = 0

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            mDx += dx
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when (newState) {
                SCROLL_STATE_IDLE -> {
                    if (mDx > 0) {
                        selectCenterTabForRightScroll()
                    } else {
                        selectCenterTabForLeftScroll()
                    }
                    mDx = 0
                }
            }
        }

        private fun selectCenterTabForRightScroll() {
            val first = mLinearLayoutManager.findFirstVisibleItemPosition()
            val last = mLinearLayoutManager.findLastVisibleItemPosition()
            val center = mRecyclerTabLayout.width / 2
            for (position in first..last) {
                val view = mLinearLayoutManager.findViewByPosition(position)
                if (view!!.left + view.width >= center) {
                    mRecyclerTabLayout.setCurrentItem(position, false)
                    break
                }
            }
        }

        private fun selectCenterTabForLeftScroll() {
            val first = mLinearLayoutManager.findFirstVisibleItemPosition()
            val last = mLinearLayoutManager.findLastVisibleItemPosition()
            val center = mRecyclerTabLayout.width / 2
            for (position in last downTo first) {
                val view = mLinearLayoutManager.findViewByPosition(position)
                if (view?.left ?: 0 <= center) {
                    mRecyclerTabLayout.setCurrentItem(position, false)
                    break
                }
            }
        }
    }

    private var viewPagerListener: ViewPagerOnPageChangeListener? = null

    open class ViewPagerOnPageChangeListener(private val mRecyclerTabLayout: MyRecyclerTabLayout) :
            ViewPager.OnPageChangeListener {
        private var mScrollState: Int = 0

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            mRecyclerTabLayout.scrollToTab(position, positionOffset, false)
        }

        override fun onPageScrollStateChanged(state: Int) {
            mScrollState = state
        }

        override fun onPageSelected(position: Int) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                if (mRecyclerTabLayout.mIndicatorPosition != position) {
                    mRecyclerTabLayout.scrollToTab(position)
                }
            }
        }
    }

    abstract class Adapter<T : ViewHolder>(var context: Context) : RecyclerView.Adapter<T>() {
        var currentIndicatorPosition: Int = 0
    }


    companion object {
        protected const val DEFAULT_SCROLL_DURATION: Long = 200
        protected const val DEFAULT_POSITION_THRESHOLD = 0.6f
        protected const val POSITION_THRESHOLD_ALLOWABLE = 0.001f
    }
}
