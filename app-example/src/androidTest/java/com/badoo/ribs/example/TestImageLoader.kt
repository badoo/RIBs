package com.badoo.ribs.example

import android.content.Context
import android.graphics.Bitmap
import androidx.test.platform.app.InstrumentationRegistry
import coil.ImageLoader
import coil.bitmap.BitmapPool
import coil.decode.DataSource
import coil.memory.MemoryCache
import coil.request.DefaultRequestOptions
import coil.request.Disposable
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.request.SuccessResult
import com.badoo.ribs.example.test.R

class TestImageLoader : ImageLoader {

    override val bitmapPool: BitmapPool = BitmapPool.invoke(0)

    override val memoryCache: MemoryCache = object : MemoryCache {
        override val maxSize: Int = 0

        override val size: Int = 0

        override fun clear() {}

        override fun get(key: MemoryCache.Key): Bitmap? = null

        override fun remove(key: MemoryCache.Key): Boolean = false

        override fun set(key: MemoryCache.Key, bitmap: Bitmap) {}
    }

    private val instrumentationContext: Context =
        InstrumentationRegistry.getInstrumentation().context

    private val drawable = instrumentationContext.getDrawable(R.drawable.ic_sample)!!

    private val disposable = object : Disposable {
        override val isDisposed = true
        override fun dispose() {}
        override suspend fun await() {}
    }

    override val defaults = DefaultRequestOptions()

    override fun enqueue(request: ImageRequest): Disposable {
        request.target?.onStart(drawable)
        request.target?.onSuccess(drawable)
        return disposable
    }

    override suspend fun execute(request: ImageRequest): ImageResult =
        SuccessResult(
            drawable,
            request,
            ImageResult.Metadata(
                memoryCacheKey = null,
                isSampled = false,
                dataSource = DataSource.MEMORY,
                isPlaceholderMemoryCacheKeyPresent = false
            )
        )

    override fun newBuilder(): ImageLoader.Builder =
        ImageLoader.Builder(instrumentationContext)

    override fun shutdown() {}
}
