package com.soul.listener.myapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * @description
 * @author kuan
 * Created on 2018/picture/31.
 */
class ChangeColorView(context: Context?, attrs: AttributeSet?=null) : View(context, attrs) {
    private var mPaint = Paint()
    private var mBitmap:Bitmap? = null
    init {
        mPaint.isAntiAlias = true
        mBitmap = BitmapFactory.decodeResource(context!!.resources,R.drawable.picture)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        canvas!!.drawBitmap(mBitmap, null, Rect(0, 0, 500, 500 * mBitmap!!.height / mBitmap!!.width), mPaint)
//        canvas!!.translate(0f, 500f)

        val colorMatrix = ColorMatrix(floatArrayOf(
                1f, 0f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 500f,
                0f, 0f, 1f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
        ))

        mPaint!!.setColorFilter(ColorMatrixColorFilter(colorMatrix))
        canvas!!.drawBitmap(mBitmap, null, Rect(0, 0, mBitmap!!.width, 500 * mBitmap!!.height / mBitmap!!.width), mPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        var resultW = widthSize
        var resultH = heightSize

        var contentW = 0
        var contentH = 0

        if (widthMode == View.MeasureSpec.AT_MOST){
            contentW = mBitmap!!.width
            contentW +=paddingRight+paddingLeft

            resultW = if (contentW < widthSize)contentW else widthSize
        }

        if (heightMode == View.MeasureSpec.AT_MOST){
            contentH = mBitmap!!.height*2+500
            contentH += paddingLeft + paddingRight

            resultH = if (contentH < height) contentH else height
        }

        setMeasuredDimension(resultW,resultH)

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }
}