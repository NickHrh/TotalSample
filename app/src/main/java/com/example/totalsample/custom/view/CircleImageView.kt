package com.example.totalsample.custom.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.example.totalsample.R

class CircleImageView : AppCompatImageView {

    private var borderWidth = 0f
    private var borderColor = Color.WHITE

    private var shadowRadius = 0f
    private var shadowColor = Color.TRANSPARENT


    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 用于离屏缓冲的paint
    private val layerPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    val rect = RectF()

    constructor(ctx: Context) : super(ctx) {
        init(null)
    }

    constructor(ctx: Context, attrs: AttributeSet?) : super(ctx, attrs) {
        init(attrs)
    }

    constructor(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        ctx,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }


    val srcInMode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    override fun onDraw(canvas: Canvas) {
        if (drawable == null) {
            super.onDraw(canvas)
            return
        }

        val diameter =
            (width - paddingLeft - paddingRight)
                .coerceAtMost(height - paddingTop - paddingBottom)
        val radius = diameter / 2f
        val centerX = width / 2f
        val centerY = height / 2f

        if (shadowRadius > 0) {
            shadowPaint.color = shadowColor
            canvas.drawCircle(centerX, centerY, radius, shadowPaint)
        }

        val saveCount = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), layerPaint)

        paint.reset()
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        canvas.drawCircle(centerX, centerY, radius, paint)

        paint.xfermode = srcInMode

        val bmp = drawableToBitmap(drawable, diameter, diameter)
        canvas.drawBitmap(bmp, centerX - radius, centerY - radius, paint)

        paint.xfermode = null
        canvas.restoreToCount(saveCount)

        if (borderWidth > 0) {
            borderPaint.isAntiAlias = true
            borderPaint.style = Paint.Style.STROKE
            borderPaint.strokeWidth = borderWidth
            borderPaint.color = borderColor
            canvas.drawCircle(centerX, centerY, radius - borderWidth / 2, borderPaint)
        }
    }

    private fun drawableToBitmap(drawable: Drawable, w: Int, h: Int): Bitmap {
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        drawable.setBounds(0, 0, w, h)
        drawable.draw(c)
        return bmp
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rect.set(0f, 0f, w.toFloat(), h.toFloat())
    }

    private fun init(attrs: AttributeSet?) {

        scaleType = ScaleType.CENTER_CROP
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
        borderWidth = typeArray.getDimension(R.styleable.CircleImageView_borderWidth, 0f)
        borderColor = typeArray.getColor(R.styleable.CircleImageView_borderColor, Color.WHITE)
        shadowRadius = typeArray.getDimension(R.styleable.CircleImageView_shadowRadius, 0f)
        shadowColor = typeArray.getColor(R.styleable.CircleImageView_shadowColor, Color.TRANSPARENT)
        typeArray.recycle()

        borderPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
            color = borderColor
        }

        shadowPaint.color = shadowColor
        paint.color = Color.WHITE
    }
}