package com.badoo.ribs.example.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.badoo.ribs.example.image.ImageTarget

open class TargettableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), ImageTarget {

    override fun setDrawable(drawable: Drawable?) {
        setImageDrawable(drawable)
    }

    override fun setBitmap(bitmap: Bitmap?) {
        setImageBitmap(bitmap)
    }

    override fun setContentDescriptionForTest(contentDescription: String?) {
        this.contentDescription = contentDescription
    }
}
