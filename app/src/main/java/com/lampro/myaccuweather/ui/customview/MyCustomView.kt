package com.lampro.myaccuweather.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.lampro.myaccuweather.R

/**
 * TODO: document your custom view class.
 */
class MyCustomView : View {
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paint2: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val defaultColor = Color.YELLOW
    private var sweepAngle = 55f

    init {
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = 30f



        paint2.color = R.color.light_blue_400.dec()
        paint2.style = Paint.Style.STROKE
        paint2.strokeCap = Paint.Cap.ROUND
        paint2.strokeWidth = 30f
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val typeArray =
            context?.theme?.obtainStyledAttributes(attrs, R.styleable.MyCustomView, 0, 0)
        val color = typeArray?.getColor(R.styleable.MyCustomView_myViewColor,defaultColor)
        if (color != null) {
            paint.color = color
        }
        typeArray?.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec))
    }

    private fun measureSize(measureSpec: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val sizeInPixel = MeasureSpec.getSize(measureSpec)
        return when (mode) {
            MeasureSpec.EXACTLY -> {
                sizeInPixel
            }

            MeasureSpec.AT_MOST -> {
                sizeInPixel.coerceAtMost(40)
            }

            MeasureSpec.UNSPECIFIED -> {
                sizeInPixel.coerceAtMost(40)
            }

            else -> {
                0
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val radius = width / 2f
//        canvas?.drawCircle(width / 2f, width / 2f, radius, paint)
        canvas?.drawArc(20f,20f,measuredWidth.toFloat()-20f,measuredHeight.toFloat()-20f,45f,270f ,false,paint)
        canvas?.drawArc(20f,20f,measuredWidth.toFloat()-20f,measuredHeight.toFloat()-20f,45f,sweepAngle,false,paint2)
    }
    fun changeAngle(i: Float) {
        sweepAngle = i
        postInvalidate()
    }
}
