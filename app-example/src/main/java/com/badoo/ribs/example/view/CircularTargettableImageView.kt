package com.badoo.ribs.example.view

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory

class CircularTargettableImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : TargettableImageView(context, attrs, defStyleAttr) {

    override fun setBitmap(bitmap: Bitmap?) {
        val roundedDrawable: RoundedBitmapDrawable =
                RoundedBitmapDrawableFactory.create(resources, bitmap).apply { isCircular = true }
        drawable = roundedDrawable
    }
}
