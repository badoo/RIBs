package com.badoo.ribs.example

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.test.platform.app.InstrumentationRegistry
import com.badoo.ribs.example.image.ImageDownloader
import com.badoo.ribs.example.image.ImageTarget
import com.badoo.ribs.example.test.R
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.SerialDisposable
import io.reactivex.schedulers.Schedulers

class FakeImageDownloader : ImageDownloader {

    private val instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val completionSignal: Relay<Boolean> = PublishRelay.create<Boolean>()

    private val disposable: SerialDisposable = SerialDisposable()

    override fun download(url: String, placeholder: Int, target: ImageTarget) {
        target.onPrepareLoad(targetContext.getDrawable(placeholder))
        disposable.set(
            completionSignal
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    if (result) {
                        target.onBitmapLoaded(getSampleBitmap(), null)
                    } else {
                        target.onBitmapFailed(null, null)
                    }
                }
        )
    }

    private fun getSampleBitmap(): Bitmap? = (getSampleDrawable() as? BitmapDrawable)?.bitmap

    private fun getSampleDrawable(): Drawable? =
            instrumentationContext.getDrawable(R.drawable.ic_sample)

    fun succeed() {
        completionSignal.accept(true)
    }

    fun fail() {
        completionSignal.accept(false)
    }
}
