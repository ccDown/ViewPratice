package com.soul.listener.myapplication

import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

class TanTanLayoutManager(var mRecyclerView: RecyclerView, var listener: OnSwipeListener<String>) : RecyclerView.LayoutManager() {

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        val itemCount = itemCount
        removeAllViews()
        if (itemCount > 0) {
            //倒序是因为要让第一个放在最上面,最后放
            for (i in Math.min(showCount, itemCount) - 1 downTo 0) {
                val view = recycler!!.getViewForPosition(i)
                addView(view)
                measureChildWithMargins(view, 0, 0)
                val widthSpace = width - getDecoratedMeasuredWidth(view) //在宽度上的两边空白
                val heightSpace = height - getDecoratedMeasuredHeight(view)  //高度上的两边空白
                layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2, widthSpace / 2 + getDecoratedMeasuredWidth(view), heightSpace / 2 + getDecoratedMeasuredHeight(view))
                //设置后面缩小和下移
                view.scaleY = 1 - i * scaleRate
                view.scaleX = 1 - i * scaleRate
                view.translationY = view.measuredHeight.toFloat() * translateRate * i.toFloat()
                if (i == 0) {
                    view.setOnTouchListener(object : View.OnTouchListener {
                        private var downX: Float = 0.toFloat()
                        private var layoutParams: ViewGroup.LayoutParams? = null
                        override fun onTouch(v: View, event: MotionEvent): Boolean {
                            if (event.action == MotionEvent.ACTION_DOWN) {
                                downX = event.rawX
                                layoutParams = v.layoutParams
                                return true
                            } else if (event.action == MotionEvent.ACTION_UP) {

                                if (Math.abs(event.rawX - downX) > mRecyclerView.measuredWidth / 3) {
                                    listener.isSwipedGone(true)
                                } else {
                                    listener.isSwipedGone(false)
                                    v.layoutParams = layoutParams
                                    return false
                                }
                            }
                            v.x = event.rawX - v.measuredWidth / 2
                            v.rotation = (event.rawX - downX) / mRecyclerView.measuredWidth * 60
                            v.alpha = 1 - Math.abs((event.rawX - downX) / mRecyclerView.measuredWidth)
                            return false
                        }
                    })
                }
            }
        }
    }

    companion object {

        private val TAG = "666666666666666666666"
        private val scaleRate = 0.1f
        private val translateRate = 0.1f
        private val showCount = 3
    }
    public interface OnSwipeListener<String>{
        fun isSwipedGone(boolean: Boolean)
    }
}
