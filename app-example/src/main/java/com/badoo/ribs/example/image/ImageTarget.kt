package com.badoo.ribs.example.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.badoo.ribs.example.BuildConfig
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception

interface ImageTarget : Target {

    fun setDrawable(drawable: Drawable?)

    fun setBitmap(bitmap: Bitmap?)

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        if (BuildConfig.DEBUG) {
            setContentDescriptionForTest(CONTENT_DESCRIPTION_LOADING)
        }
        setDrawable(placeHolderDrawable)
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        if (BuildConfig.DEBUG) {
            setContentDescriptionForTest(CONTENT_DESCRIPTION_LOAD_SUCCESS)
        }
        if (bitmap != null) {
            setBitmap(bitmap)
        }
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        if (BuildConfig.DEBUG) {
            setContentDescriptionForTest(CONTENT_DESCRIPTION_LOAD_FAILURE)
        }
        if (errorDrawable != null) {
            setDrawable(errorDrawable)
        }
    }

    fun setContentDescriptionForTest(contentDescription: String?)

    companion object {
        const val CONTENT_DESCRIPTION_LOADING = "Loading"
        const val CONTENT_DESCRIPTION_LOAD_SUCCESS = "LoadSuccess"
        const val CONTENT_DESCRIPTION_LOAD_FAILURE = "LoadFailure"
    }
}
