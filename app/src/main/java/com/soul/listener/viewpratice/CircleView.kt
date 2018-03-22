package com.soul.listener.viewpratice

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.widget.Button

/**
 * @description
 * @author kuan
 * Created on 2018/3/9.
 */
class CircleView(context: Context) : Button(context) {

    var mBackgroundDrawable :Drawable? = null

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    constructor(context: Context, attributeSet: AttributeSet) : this(context) {

        val ta: TypedArray = context.obtainStyledAttributes(attributeSet,R.styleable.CircleView)
        mBackgroundDrawable = ta.getDrawable(R.styleable.CircleView_android_background)
        ta.recycle()

        background = mBackgroundDrawable

    }

}