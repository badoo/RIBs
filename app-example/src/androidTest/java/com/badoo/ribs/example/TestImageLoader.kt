package com.badoo.ribs.example

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import coil.DefaultRequestOptions
import coil.ImageLoader
import coil.decode.DataSource
import coil.request.GetRequest
import coil.request.LoadRequest
import coil.request.RequestDisposable
import coil.request.RequestResult
import coil.request.SuccessResult
import com.badoo.ribs.example.test.R

class TestImageLoader : ImageLoader {

    private val instrumentationContext: Context =
            InstrumentationRegistry.getInstrumentation().context

    private val drawable = instrumentationContext.getDrawable(R.drawable.ic_sample)!!

    private val disposable = object : RequestDisposable {
        override val isDisposed = true
        override fun dispose() {}
        override suspend fun await() {}
    }

    override val defaults = DefaultRequestOptions()

    override fun execute(request: LoadRequest): RequestDisposable {
        request.target?.onStart(drawable)
        request.target?.onSuccess(drawable)
        return disposable
    }

    override suspend fun execute(request: GetRequest): RequestResult =
        SuccessResult(drawable, DataSource.MEMORY)

    override fun invalidate(key: String) {}

    override fun clearMemory() {}

    override fun shutdown() {}

}
