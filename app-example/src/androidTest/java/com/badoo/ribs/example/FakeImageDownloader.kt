package com.badoo.ribs.example

import androidx.test.platform.app.InstrumentationRegistry
import com.badoo.ribs.example.image.ImageDownloader
import com.badoo.ribs.example.image.ImageTarget
import com.badoo.ribs.example.test.R

class FakeImageDownloader : ImageDownloader {

    private val context = InstrumentationRegistry.getInstrumentation().context

    override fun download(url: String, placeholder: Int, target: ImageTarget) {
        target.setDrawable(context.getDrawable(R.drawable.ic_sample))
    }

}
