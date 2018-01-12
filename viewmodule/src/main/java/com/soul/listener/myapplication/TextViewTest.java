package com.soul.listener.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author kuan
 *         Created on 2018/1/12.
 * @description
 */

public class TextViewTest extends View {

    private int mTextSize;
    private TextPaint mPaint;
    private String mText;

    public TextViewTest(Context context) {
        this(context,null);
    }

    public TextViewTest(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TextViewTest(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.TextViewTest);
        mText = ta.getString(R.styleable.TextViewTest_android_text);
        mTextSize = ta.getDimensionPixelSize(R.styleable.TextViewTest_android_textSize,24);
        ta.recycle();

        mPaint = new TextPaint();
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        /*resultW代表最终设置的宽   resultH代表最终设置的高*/
        int resultW = widthSize;
        int resultH = heightSize;

        int contentW = 0;
        int contentH = 0;

        /*
        *重点处理AT_MOST模式
        *TestView自主决定数值大小  但不能超过ViewGroup给出的数值
        */
        if (widthMode == MeasureSpec.AT_MOST){
            if (!TextUtils.isEmpty(mText)){
                contentW = (int) mPaint.measureText(mText);
                contentW += getPaddingLeft() + getPaddingRight();

                resultW = contentW<widthSize?contentW:widthSize;
            }
        }

        if (heightMode == MeasureSpec.AT_MOST){
            if (!TextUtils.isEmpty(mText)){
                contentH = mTextSize;
                contentH += getPaddingTop() + getPaddingBottom();

                resultH = contentH<widthSize?contentH:heightSize;
            }
        }

        //一定设置这个函数
        setMeasuredDimension(resultW,resultH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int cx = (getWidth() - getPaddingLeft() - getPaddingRight())/2;
        int cy = (getHeight() - getPaddingTop() - getPaddingBottom()) / 2;

        canvas.drawColor(Color.RED);
        if (TextUtils.isEmpty(mText)){
            return;
        }
        canvas.drawText(mText,cx,cy,mPaint);
    }
}
