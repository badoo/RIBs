package com.badoo.ribs.example.photo_details

import com.badoo.ribs.example.network.UnsplashApi
import com.badoo.ribs.example.network.model.Photo
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

interface PhotoDetailsDataSource {
    fun getPhoto(id: String): Single<Photo>

    fun likePhoto(id: String):  Single<Unit>
    fun unlikePhoto(id: String):  Single<Unit>
}


class PhotoDetailsDataSourceImpl(private val api: UnsplashApi) : PhotoDetailsDataSource {
    override fun getPhoto(id: String): Single<Photo> =
        api.getPhoto(id)
            .observeOn(AndroidSchedulers.mainThread())

    override fun likePhoto(id: String): Single<Unit> =
        api.likePhoto(id)
            .observeOn(AndroidSchedulers.mainThread())

    override fun unlikePhoto(id: String):  Single<Unit> =
        api.unlikePhoto(id)
            .observeOn(AndroidSchedulers.mainThread())
}
