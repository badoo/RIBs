package com.badoo.ribs.example.network.model

import com.squareup.moshi.Json

data class Photo(
    val id: String,
    @Json(name = "created_at") val createdAt: String?,
    val width: Int,
    val height: Int,
    val color: String?,
    val likes: Int,
    @Json(name = "liked_by_user")
    val likedByUser: Boolean,
    val description: String?,
    val urls: Urls,
    val links: Links,
    val user: User
)
