package com.soul.listener.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.View

/**
 * @description
 * @author kuan
 * Created on 2018/3/22.
 */
class CircleView(context: Context) : View(context) {


    private var radius = 30f
    private var mCircleState = false //默认为小圆
    private val mPaint = Paint()
    private var mCanvas = Canvas()

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                1 -> {
                    if (mCircleState) {
                        setBigCircle()
                        mCircleState = true
                        invalidate()

                    } else {
                        setSmallCircle()
                        mCircleState = false
                        invalidate()
                    }
                }
                0 -> {

                }
            }

        }
    }

    @SuppressLint("ResourceAsColor")
    constructor(context: Context, attributeSet: AttributeSet) : this(context) {

        val typeArray = context.obtainStyledAttributes(attributeSet, R.styleable.CircleView)
        val backgroundColor = typeArray.getColor(R.styleable.CircleView_circleColor, R.color.error_color_material)
        typeArray.recycle()

        mPaint.color = backgroundColor    //设置画笔颜色
        setSmallCircle()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val cx = (measuredWidth / 2).toFloat()
        val cy = measuredHeight - radius - 50f

        canvas!!.drawCircle(cx, cy, radius, mPaint)
        mCanvas = canvas
    }

//    override fun onLongClick(v: View?): Boolean {
//        val message = Message()
//        message.what = 0
//        handler.sendMessageDelayed(message,500)
//        return true
//    }
//
//    override fun onClick(v: View?) {
//        val message = Message()
//        message.what = 1
//        handler.sendMessageDelayed(message,500)
//    }

    /**
     * 绘制小圆
     */
    fun setSmallCircle() {
        radius = 60f
        mPaint.strokeWidth = 30.0.toFloat()              //线宽
        mPaint.style = Paint.Style.STROKE                   //空心效果
        mPaint.isAntiAlias = false          //设置画笔为无锯齿
    }

    /**
     * 绘制大圆
     */
    fun setBigCircle() {
        radius = 100f
        mPaint.strokeWidth = 50.0.toFloat()              //线宽
        mPaint.style = Paint.Style.STROKE                   //空心效果
        mPaint.isAntiAlias = false          //设置画笔为无锯齿
    }
}