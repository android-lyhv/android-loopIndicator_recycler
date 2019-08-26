/**
 * Copyright (C) 2015 nshmura
 * Copyright (C) 2015 The Android Open Source Project
 * Copyright (C) 2019 lyhv
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
 *
 */
package com.lyhv.library

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

open class CycleRecyclerTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    RecyclerView(context, attrs, defStyle) {
    // Style config
    private var mRecyclerIndicatorStyle: RecyclerIndicatorStyle
    private var mLinearLayoutManager: LinearLayoutManager
    private var mViewPager: ViewPager? = null
    private var mAdapter: Adapter<*>? = null
    // Scroll Config
    protected var mIndicatorPosition: Int = 0
    private var mIndicatorGap: Int = 0
    private var mIndicatorScroll: Int = 0
    private var mOldPositionOffset: Float = 0.toFloat()
    private var mPositionThreshold: Float = DEFAULT_POSITION_THRESHOLD
    private var mRequestScrollToTab: Boolean = false
    private var mOldPosition: Int = 0
    private var mOldScrollOffset: Int = 0
    private val isLayoutRtl: Boolean get() = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
    private lateinit var mCycleIndicatorRecyclerAdapter: CycleIndicatorRecyclerAdapter

    init {
        setWillNotDraw(false)
        mRecyclerIndicatorStyle = RecyclerIndicatorStyle().apply {
            applyStyle(context, attrs!!, defStyle)
        }
        mLinearLayoutManager =
            LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        layoutManager = mLinearLayoutManager
        itemAnimator = null
    }

    private fun resetScrollConfig() {
        mIndicatorPosition = 0
        mIndicatorGap = 0
        mIndicatorScroll = 0
        mOldPositionOffset = 0F
        mPositionThreshold = DEFAULT_POSITION_THRESHOLD
        mRequestScrollToTab = false
        mOldPosition = 0
        mOldScrollOffset = 0
    }


    fun setUpWithViewPager(
        context: Context,
        viewPager: ViewPager,
        cycleFragmentStatePagerAdapter: CycleFragmentStatePagerAdapter
    ) {
        mViewPager = viewPager
        mCycleIndicatorRecyclerAdapter =
            CycleIndicatorRecyclerAdapter(
                context,
                mRecyclerIndicatorStyle,
                cycleFragmentStatePagerAdapter
            )
        setUpWithAdapter(mCycleIndicatorRecyclerAdapter.apply {
            onItemListener = object : CycleIndicatorRecyclerAdapter.OnIndicatorItemListener {
                override fun onItemPositionClicked(positionIndex: Int, realPosition: Int) {
                    if (abs(positionIndex.minus(mIndicatorPosition)) <= IndicatorConfig.MAX_STEP_INDEX_ANIMATION) {
                        setCurrentItem(positionIndex, true)
                    } else {
                        setCurrentItem(positionIndex, false)
                    }
                }
            }
        })
    }

    private fun getItemCenterPosition(realPosition: Int): Int {
        val targetCenterPosition =
            mCycleIndicatorRecyclerAdapter.getRealPosition(mCycleIndicatorRecyclerAdapter.itemCount / 2)
        val range = realPosition - targetCenterPosition
        return mCycleIndicatorRecyclerAdapter.itemCount / 2 + range
    }

    private fun setUpWithAdapter(adapter: Adapter<*>) {
        mAdapter = adapter
        requireNotNull(mViewPager?.adapter) { "ViewPager does not have a PagerAdapter set" }
        viewPagerListener = ViewPagerOnPageChangeListener(this)
        mViewPager?.addOnPageChangeListener(viewPagerListener!!)
        setAdapter(adapter)
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
            distance = abs(centerX - currentX) / view.measuredWidth
        }

        val animator: ValueAnimator
        animator = if (position < mIndicatorPosition) {
            ValueAnimator.ofFloat(distance, 0F)
        } else {
            ValueAnimator.ofFloat(-distance, 0F)
        }
        animator.duration = DEFAULT_SCROLL_DURATION
        animator.addUpdateListener { animation ->
            scrollToTab(
                position,
                animation.animatedValue as Float,
                true
            )
        }
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
                val distance =
                    sRight - nLeft // total distance that is needed to distance to next tab
                val dx = distance * positionOffset
                scrollOffset = (sLeft - dx).toInt()

                if (position == 0) {
                    val indicatorGap =
                        ((nextView.measuredWidth - selectedView.measuredWidth) / 2).toFloat()
                    mIndicatorGap = (indicatorGap * positionOffset).toInt()
                    mIndicatorScroll =
                        ((selectedView.measuredWidth + indicatorGap) * positionOffset).toInt()

                } else {
                    val indicatorGap =
                        ((nextView.measuredWidth - selectedView.measuredWidth) / 2).toFloat()
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
            if (measuredWidth > 0 && mRecyclerIndicatorStyle.mTabMaxWidth > 0 && mRecyclerIndicatorStyle.mTabMinWidth == mRecyclerIndicatorStyle.mTabMaxWidth) { //fixed size
                val width = mRecyclerIndicatorStyle.mTabMinWidth
                val offset = (positionOffset * -width).toInt()
                val leftOffset = ((measuredWidth - width) / 2f).toInt()
                scrollOffset = offset + leftOffset
            }
            mRequestScrollToTab = true
        }

        updateCurrentIndicatorPosition(
            position,
            positionOffset - mOldPositionOffset,
            positionOffset
        )
        mIndicatorPosition = position

        stopScroll()

        if (position != mOldPosition || scrollOffset != mOldScrollOffset) {
            mLinearLayoutManager.scrollToPositionWithOffset(position, scrollOffset)
        }

        mOldPosition = position
        mOldScrollOffset = scrollOffset
        mOldPositionOffset = positionOffset
        if (mRecyclerIndicatorStyle.mIndicatorHeight > 0) {
            postInvalidate()
        }
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
        onDrawIndicator(canvas, mIndicatorPosition)
    }

    private fun onDrawIndicator(canvas: Canvas, position: Int) {
        val view = mLinearLayoutManager.findViewByPosition(position)
        if (view == null) {
            if (mRequestScrollToTab) {
                mRequestScrollToTab = false
                scrollToTab(mViewPager?.currentItem ?: 0)
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

        val top = (height - mRecyclerIndicatorStyle.mIndicatorHeight) / 2
        val bottom = top + mRecyclerIndicatorStyle.mIndicatorHeight

        val rect = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
        canvas.drawRoundRect(
            rect,
            mRecyclerIndicatorStyle.mIndicatorCorner.toFloat(),
            mRecyclerIndicatorStyle.mIndicatorCorner.toFloat(),
            mRecyclerIndicatorStyle.mIndicatorPaint
        )
    }

    fun setCenterPositionItem(realPosition: Int) {
        resetScrollConfig()
        val centerPosition = getItemCenterPosition(realPosition)
        setCurrentItem(centerPosition, false)
        startAnimation(centerPosition)
    }

    private var viewPagerListener: ViewPagerOnPageChangeListener? = null

    open class ViewPagerOnPageChangeListener(private val mRecyclerTabLayout: CycleRecyclerTabLayout) :
        ViewPager.OnPageChangeListener {
        private var mScrollState: Int = 0

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
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
