package com.badoo.ribs.example.network.model

import com.squareup.moshi.Json

class SearchResult<R>(
    val total: Int,
    @Json(name = "total_pages") val totalPages: Int,
    val results: List<R>
)
