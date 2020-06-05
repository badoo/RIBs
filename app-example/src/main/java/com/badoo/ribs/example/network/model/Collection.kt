package com.badoo.ribs.example.network.model

import com.squareup.moshi.Json

data class Collection(
    val id: Int,
    val title: String,
    val description: String?,
    val private: Boolean,
    @Json(name = "cover_photo") val coverPhoto: Photo?,
    val user: User
)
