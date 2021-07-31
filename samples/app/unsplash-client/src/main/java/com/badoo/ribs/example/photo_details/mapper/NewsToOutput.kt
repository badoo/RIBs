package com.badoo.ribs.example.photo_details.mapper

import com.badoo.ribs.example.photo_details.PhotoDetails.Output
import com.badoo.ribs.example.photo_details.feature.PhotoDetailsFeature.News

internal object NewsToOutput : (News) -> Output? {

    override fun invoke(news: News): Output? = null
}
