package com.example.totalsample.custom.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.example.totalsample.R

class CircleImageView : AppCompatImageView {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val rect = RectF()
    val srcRect = RectF()
    val destRect = RectF()

    constructor(ctx: Context) : super(ctx)

    constructor(ctx: Context, attrs: AttributeSet?) : super(ctx, attrs)

    constructor(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        ctx,
        attrs,
        defStyleAttr
    )


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable == null) {
            super.onDraw(canvas)
            return
        }

        val diameter =
            (width - paddingLeft - paddingRight).coerceAtMost(height - paddingTop - paddingBottom)


    }

    private fun init(attrs: AttributeSet) {

        scaleType = ScaleType.CENTER_CROP
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
        val borderWidth = typeArray.getDimension(R.styleable.CircleImageView_borderWidth, 0f)
        val borderColor =
            typeArray.getColor(R.styleable.CircleImageView_borderColor, Color.WHITE)
        val shadowRadius = typeArray.getDimension(R.styleable.CircleImageView_shadowRadius, 0f)
        val shadowColor =
            typeArray.getColor(R.styleable.CircleImageView_shadowColor, Color.TRANSPARENT)
        typeArray.recycle()

        borderPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
            color = borderColor
        }

        shadowPaint.color = shadowColor

    }


}