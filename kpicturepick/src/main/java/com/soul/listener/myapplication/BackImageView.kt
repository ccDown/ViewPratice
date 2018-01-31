package com.soul.listener.myapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView

/**
 * @description
 * @author kuan
 * Created on 2018/picture/31.
 */
class BackImageView(context: Context?, attributes: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attributes, defStyleAttr) {

    private var mPaint: Paint? = null
    private var bitmap: Bitmap? = null

    private var canvas: Canvas? = null

    public fun initView(imageView: ImageView): Unit {
        mPaint!!.isAntiAlias = true
        bitmap = imageView.getDrawingCache()
    }

    public fun drawMatrix(): Unit {
        canvas!!.drawBitmap(bitmap, null, Rect(0, 0, 500, 500 * bitmap!!.height / bitmap!!.width), mPaint)
        canvas!!.translate(510f, 0f)

        val colorMatrix = ColorMatrix(floatArrayOf(
                0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f,
                0f, 0f, 1f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
        ))

        mPaint!!.setColorFilter(ColorMatrixColorFilter(colorMatrix))
        canvas!!.drawBitmap(bitmap, null, Rect(0, 0, 500, 500 * bitmap!!.height / bitmap!!.width), mPaint)

        postInvalidate()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(if (this.canvas!=null)this.canvas else canvas)

    }
}