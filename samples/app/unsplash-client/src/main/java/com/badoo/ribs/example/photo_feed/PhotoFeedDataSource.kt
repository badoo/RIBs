package com.badoo.ribs.example.photo_feed

import com.badoo.ribs.example.network.UnsplashApi
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

interface PhotoFeedDataSource {
    fun loadPhotos(page: Int = 0): Single<List<Photo>>
}

data class Photo(val id: String, val url: String, val width: Int, val height: Int) {
    constructor(model: com.badoo.ribs.example.network.model.Photo) : this(
        model.id,
        model.urls.small,
        model.width,
        model.height
    )
}

data class PhotoFeedDataSourceImpl(
    private val api: UnsplashApi
) : PhotoFeedDataSource {
    override fun loadPhotos(page: Int): Single<List<Photo>> =
        api.getPhotos(page, pageSize)
            .map { it.map(::Photo) }
            .observeOn(AndroidSchedulers.mainThread())

    companion object {
        private const val pageSize = 10
    }
}
