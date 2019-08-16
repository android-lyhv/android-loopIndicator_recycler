package com.lyhv.library

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IndicatorRecyclerAdapter(context: Context, var titles: List<String>, var loopCount: Int = IndicatorConfig.LOOP_COUNT) :
        MyRecyclerTabLayout.Adapter<IndicatorRecyclerAdapter.IndicatorViewHolder>(context) {
    var onItemListener: OnIndicatorItemListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndicatorViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.indicator_view, parent, false)
        return IndicatorViewHolder(view)
    }

    override fun onBindViewHolder(holderIndicator: IndicatorViewHolder, position: Int) {
        holderIndicator.tvTitle.isSelected = currentIndicatorPosition == position
        holderIndicator.onBind()
    }


    override fun getItemCount(): Int {
        return titles.size * loopCount
    }


    inner class IndicatorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)

        init {
            itemView.setOnClickListener {
                onItemListener?.onItemPositionClicked(adapterPosition, getRealPosition(adapterPosition))
            }
        }

        @SuppressLint("SetTextI18n")
        fun onBind() {
            val realPosition = getRealPosition(adapterPosition)
            tvTitle.text = titles[realPosition]
        }
    }

    fun getRealPosition(position: Int) = position.rem(titles.size)

    interface OnIndicatorItemListener {
        fun onItemPositionClicked(positionIndex: Int, realPosition: Int)
    }
}