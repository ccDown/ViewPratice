package com.soul.listener.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View

/**
 * @author kuan
 * Created on 2018/1/12.
 * @description
 */

class TextViewTest @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val mTextSize: Int
    private val mPaint: TextPaint
    private val mText: String?

    init {

        val ta = context.obtainStyledAttributes(attrs, R.styleable.TextViewTest)
        mText = ta.getString(R.styleable.TextViewTest_android_text)
        mTextSize = ta.getDimensionPixelSize(R.styleable.TextViewTest_android_textSize, 24)
        ta.recycle()

        mPaint = TextPaint()
        mPaint.color = Color.BLACK
        mPaint.textSize = mTextSize.toFloat()
        mPaint.textAlign = Paint.Align.CENTER

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        /*resultW代表最终设置的宽   resultH代表最终设置的高*/
        var resultW = widthSize
        var resultH = heightSize

        var contentW = 0
        var contentH = 0

        /*
        *重点处理AT_MOST模式
        *TestView自主决定数值大小  但不能超过ViewGroup给出的数值
        */
        if (widthMode == View.MeasureSpec.AT_MOST) {
            if (!TextUtils.isEmpty(mText)) {
                contentW = mPaint.measureText(mText).toInt()
                contentW += paddingLeft + paddingRight

                resultW = if (contentW < widthSize) contentW else widthSize
            }
        }

        if (heightMode == View.MeasureSpec.AT_MOST) {
            if (!TextUtils.isEmpty(mText)) {
                contentH = mTextSize
                contentH += paddingTop + paddingBottom

                resultH = if (contentH < widthSize) contentH else heightSize
            }
        }

        //一定设置这个函数
        setMeasuredDimension(resultW, resultH)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cx = (width - paddingLeft - paddingRight) / 2
        val cy = (height - paddingTop - paddingBottom) / 2

        canvas.drawColor(Color.RED)
        if (TextUtils.isEmpty(mText)) {
            return
        }
        canvas.drawText(mText!!, cx.toFloat(), cy.toFloat(), mPaint)
    }
}
