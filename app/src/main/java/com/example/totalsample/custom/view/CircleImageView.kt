package com.example.totalsample.custom.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
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
    val srcRect = Rect()
    val destRect = RectF()

    private var drawableChanged = true
    private var cachedBitmap: Bitmap? = null

    val srcInMode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

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


    override fun onDraw(canvas: Canvas) {
        if (drawable == null) {
            super.onDraw(canvas)
            return
        }

        //让圆形的直径等于高度
        val diameter = (width - paddingLeft - paddingRight)
            .coerceAtMost(height - paddingTop - paddingBottom)
        val radius = diameter / 2f
        //中心点坐标
        val centerX = width / 2f
        val centerY = height / 2f

        if (shadowRadius > 0) {
            setLayerType(LAYER_TYPE_SOFTWARE, null)
            shadowPaint.setShadowLayer(shadowRadius, 0f, 0f, shadowColor)
            canvas.drawCircle(centerX, centerY, radius, shadowPaint)
        }
        //离屏缓存
        val saveCount = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), layerPaint)

        paint.reset()
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        canvas.drawCircle(centerX, centerY, radius, paint)

        paint.xfermode = srcInMode

        val bmp = getBitmapFromDrawable(drawable, diameter, diameter)

        srcRect.set(
            0,
            0,
            bmp.width,
            bmp.height
        )

        destRect.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        canvas.drawBitmap(
            bmp,
            srcRect,
            destRect,
            paint
        )

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

    private fun getBitmapFromDrawable(drawable: Drawable, w: Int, h: Int): Bitmap {
        if (!drawableChanged && cachedBitmap != null
            && cachedBitmap?.width == w && cachedBitmap?.height == h
        ) {
            return cachedBitmap!!
        }

        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        drawable.setBounds(0, 0, w, h)
        drawable.draw(canvas)
        cachedBitmap = bmp
        drawableChanged = false
        return bmp
    }

    override fun invalidateDrawable(dr: Drawable) {
        super.invalidateDrawable(dr)
        drawableChanged = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawableChanged = true
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