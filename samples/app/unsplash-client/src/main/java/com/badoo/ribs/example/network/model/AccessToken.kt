package com.badoo.ribs.example.network.model

import com.squareup.moshi.Json

data class AccessToken(
    @Json(name = "access_token") val access_token: String,
    @Json(name = "token_type") val tokenType: String,
    val scope: String,
    @Json(name = "created_at") val createdAt: String
)
