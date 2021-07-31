package com.badoo.ribs.example.network.model

import com.squareup.moshi.Json

data class Links(
    val self: String,
    val html: String,
    val photos: String?,
    val likes: String?,
    val portfolio: String?,
    val download: String?,
    @Json(name = "download_location")
    val downloadLocation: String?
)
