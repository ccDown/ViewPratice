package com.soul.listener.viewpratice

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import java.util.jar.Attributes

/**
 * @description
 * @author kuan
 * Created on 2018/1/12.
 */
class SelfView(context: Context?) : ViewGroup(context) {

    private val preX : Float? = null
    private val preY : Float? = null
    val TAG : String = "TagView"
    var mBackgroundDrawable : Drawable? = null

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    /**
     * 第二个构造函数，它委托调用了 this()。这是因为有一条规则：
    如果类有一个主构造函数（无论有无参数），
    每个次构造函数需要直接或间接委托给主构造函数，用this关键字
     */
    constructor(context: Context?,sttributeSet: AttributeSet?) : this(context){
        val ta :TypedArray = context!!.obtainStyledAttributes(sttributeSet,R.styleable.TagView);
        mBackgroundDrawable = ta.getDrawable(R.styleable.TagView_android_background)

        ta.recycle()

        if (mBackgroundDrawable != null){
            setBackgroundDrawable(mBackgroundDrawable)
        }
    }

    /**
     * 子 View 拥有 margin 属性。
     */
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return super.generateLayoutParams(attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

}