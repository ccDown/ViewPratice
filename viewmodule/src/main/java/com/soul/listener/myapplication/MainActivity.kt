package com.soul.listener.myapplication

import android.animation.*
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.LruCache
import android.view.animation.BounceInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ObjectAnimator
        textview.setOnClickListener {
            val objectAnimator = ObjectAnimator.ofFloat(textview,"alpha",1f,0f,1f)
            objectAnimator.interpolator = LinearInterpolator()
            objectAnimator.setEvaluator(FloatEvaluator())
            objectAnimator.repeatMode = ObjectAnimator.REVERSE
            objectAnimator.repeatCount = ObjectAnimator.INFINITE
            objectAnimator.duration = 1*1000
            objectAnimator.start()
        }

        //ValueAnimator
        textview2.setOnClickListener{
            val valueAnimator  = ValueAnimator.ofInt(0,400,330,900)
            val colorAnimator = ValueAnimator.ofArgb(0xffffff00.toInt(), 0xff0000ff.toInt())
            valueAnimator.duration = 1*1000
            colorAnimator.duration = 1*1000
            colorAnimator.addUpdateListener(ValueAnimator.AnimatorUpdateListener {
                val curValue = it.animatedValue as Int
                textview2.setBackgroundColor(curValue)

            })
            valueAnimator.addUpdateListener(ValueAnimator.AnimatorUpdateListener {
                val curValue = it.animatedValue as Int
                textview2.layout(curValue,curValue,curValue+textview2.width,curValue+textview2.height)
            })
            valueAnimator.repeatMode = ValueAnimator.REVERSE
            valueAnimator.repeatCount = ValueAnimator.INFINITE
            valueAnimator.start()

            colorAnimator.repeatCount = ValueAnimator.INFINITE
            colorAnimator.repeatMode = ValueAnimator.REVERSE
            colorAnimator.start()
        }

        //OfObject
        button.setOnClickListener {
            val ofObjectAnimation  = ValueAnimator.ofObject(CharEvaluator(),"#0000FF", "#FF0000")
            ofObjectAnimation.duration = 10*1000
            ofObjectAnimation.addUpdateListener {
                val string = it.animatedValue
                button.setText("$string")
            }
            ofObjectAnimation.repeatMode = ValueAnimator.REVERSE
            ofObjectAnimation.repeatCount = ValueAnimator.INFINITE
            ofObjectAnimation.start()
        }

        button2.setOnClickListener{
            val anim = ValueAnimator.ofObject(PointEvaluator<String>(), 0,300)
            val anim2 = ObjectAnimator.ofObject(button2, "background", ArgbEvaluator(),
                    0xffff00, 0xFF0000)
            val animatorSet = AnimatorSet()
            animatorSet.duration = 2000
            animatorSet.playTogether(anim,anim2)
            animatorSet.start()
            LruCache
        }
        var isDownOrUp = false
        button3.setOnClickListener{
            when(isDownOrUp){
                false ->{
                    button3.animate().y(500F).setDuration(5000)
                            .setInterpolator (BounceInterpolator()).alpha(0.5f)
                    isDownOrUp = true
                }
                true ->{
                    button3.animate().y(-500F).setDuration(5000)
                            .setInterpolator (BounceInterpolator()).alpha(1f)
                    isDownOrUp = false
                }
            }
        }

        paintview.setOnLongClickListener {
            true
        }
    }

    class PointEvaluator<T> : TypeEvaluator<T> {
        override fun evaluate(fraction: Float, startValue: T, endValue: T): T {
            return startValue
        }
    }

    class MyInterpolator : Interpolator{
        override fun getInterpolation(input: Float): Float {
            return -input
        }
    }

    class CharEvaluator : TypeEvaluator<Int> {
        override fun evaluate(fraction: Float, startValue: Int?, endValue: Int?): Int? {
            val startInt = startValue!!
            val endInt = endValue!!
            val curInt = (startInt + fraction * (endInt - startInt))
            return curInt.toInt()
        }
    }
}
