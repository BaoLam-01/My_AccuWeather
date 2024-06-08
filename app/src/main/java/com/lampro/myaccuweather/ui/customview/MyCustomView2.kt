package com.lampro.myaccuweather.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.lampro.myaccuweather.R

class MyCustomView2: View {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val color =  Color.YELLOW
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        val typeArray =
            context?.theme?.obtainStyledAttributes(attrs, R.styleable.MyCustomView, 0, 0)
        val color = typeArray?.getColor(R.styleable.MyCustomView_myViewColor,color)
        if (color != null) {
            paint.color = color
        }
        typeArray?.recycle()
    }
    init {
        paint.strokeWidth = 30f
        paint.color = Color.RED
        paint.strokeCap = Paint.Cap.ROUND
        paint.style = Paint.Style.STROKE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
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
        canvas?.drawArc(20f,20f,measuredWidth.toFloat()-20f,measuredHeight.toFloat()-20f,45f,55f,false,paint)
    }

}