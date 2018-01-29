package com.soul.listener.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * @description
 * @author kuan
 * Created on 2018/1/18.
 */
class PaintView(context: Context?,attributes: AttributeSet? =null,defStyleAttr:Int = 0) : View(context, attributes, defStyleAttr)  {

    private val path = Path()

    private var mPrex: Float? = null
    private var mPrey: Float? = null

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                mPrex = event.x
                mPrey = event.y
                path.moveTo(event.x, event.y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = event.x
                val endY = event.y
                path.quadTo(mPrex!!,mPrey!!,endX,endY)
                mPrex = endX
                mPrey = endY
                invalidate()
            }
        }

        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val paint = Paint()
        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE
        paint.textSize = 20f

        canvas?.drawPath(path, paint)
    }

    public fun reset() {
        path.reset()
        invalidate()
    }
}