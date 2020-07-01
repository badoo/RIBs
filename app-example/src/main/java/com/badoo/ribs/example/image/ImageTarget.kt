package com.badoo.ribs.example.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception

interface ImageTarget : Target {

    fun setDrawable(drawable: Drawable?)

    fun setBitmap(bitmap: Bitmap?)

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        setDrawable(placeHolderDrawable)
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        setBitmap(bitmap)
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        setDrawable(errorDrawable)
    }
}
