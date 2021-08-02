package com.badoo.ribs.example.network.model

import com.squareup.moshi.Json

data class User(
    val id: String,
    val username: String,
    val name: String,
    val bio: String?,
    val location: String?,
    @Json(name = "total_likes") val totalLikes: Int,
    @Json(name = "total_photos") val totalPhotos: Int,
    @Json(name = "total_collections") val totalCollections: Int,
    @Json(name = "profile_image") val profileImage: Urls,
    val links: Links
)
