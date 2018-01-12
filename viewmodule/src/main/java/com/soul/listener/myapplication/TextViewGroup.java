package com.soul.listener.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author kuan
 *         Created on 2018/1/12.
 * @description
 */

public class TextViewGroup extends ViewGroup {
    public TextViewGroup(Context context) {
        this(context,null);
    }

    public TextViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TextViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int topStart = getPaddingTop();
        int leftStart = getPaddingLeft();
        int childW = 0;
        int childH = 0;
        MarginLayoutParams layoutParams = null;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            layoutParams = (MarginLayoutParams) child.getLayoutParams();

            //子元素不可见时  不参与布局  因此不需要将其尺寸计算在内
            if (child.getVisibility() == View.GONE){
                continue;
            }

            childW = child.getMeasuredWidth();
            childH = child.getMeasuredHeight();

            leftStart += layoutParams.leftMargin;
            topStart += layoutParams.topMargin;

            child.layout(leftStart,topStart,leftStart+childW,topStart+childH);

            leftStart+=childW+layoutParams.rightMargin;
            topStart += childH+layoutParams.bottomMargin;
        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        //只关心子元素的 margin 信息，所以这里用 MarginLayoutParams
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        /*resultW代表最终设置的宽  resultH代表最终设置的高*/
        int resultW = widthSize;
        int resultH = heightSize;

        /*计算尺寸的时候将自身的padding考虑进去*/
        int contentW = getPaddingLeft() + getPaddingRight();
        int contentH = getPaddingTop() + getPaddingBottom();

        /*对子元素进行尺寸的测量  */
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        MarginLayoutParams layoutParams = null;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            layoutParams = (MarginLayoutParams) child.getLayoutParams();

            //子元素不可见时，不参与布局，因此不需要将其尺寸计算在内
            if (child.getVisibility() == View.GONE){
                continue;
            }

            contentW += child.getMeasuredWidth()+layoutParams.leftMargin+layoutParams.rightMargin;
            contentH += child.getMeasuredHeight()+layoutParams.topMargin+layoutParams.bottomMargin;
        }

        /**
         * 重点处理AT_MOST模式
         * TextViewGroup通过子元素的尺寸自主决定数值大小，
         * 但不能超过ViewGroup给出的建议数值
         */

        if (widthMode == MeasureSpec.AT_MOST){
            resultW = contentW < widthSize?contentW:widthSize;
        }

        if (heightMode == MeasureSpec.AT_MOST){
            resultH = contentH < heightSize ? contentH:heightSize;
        }

        //一定要设置这个函数  不然会报错
        setMeasuredDimension(resultW,resultH);
    }
}
