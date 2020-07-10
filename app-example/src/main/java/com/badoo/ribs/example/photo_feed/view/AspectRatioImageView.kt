package com.badoo.ribs.example.photo_feed.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.badoo.ribs.example.photo_feed.view.AspectRatioDimensionsCalculator.Companion.INVALID_RATIO

internal class AspectRatioImageView : AppCompatImageView {

    private val dimensionsCalculator = AspectRatioDimensionsCalculator()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    var ratio: Float = INVALID_RATIO
        set(value) {
            dimensionsCalculator.checkAspectRatio(value)
            field = value
            requestLayout()
        }

    fun setAspectRatio(width: Int, height: Int) {
        dimensionsCalculator.checkAspectRatio(width, height)
        ratio = dimensionsCalculator.getAspectRatio(width, height)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (ratio != INVALID_RATIO) {
            val (width, height) = dimensionsCalculator.measureDimensions(
                widthMeasureSpec,
                heightMeasureSpec,
                ratio
            )
            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}
