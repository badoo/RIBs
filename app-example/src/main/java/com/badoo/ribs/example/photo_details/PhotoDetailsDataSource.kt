package com.badoo.ribs.example.photo_details

import com.badoo.ribs.example.network.UnsplashApi
import com.badoo.ribs.example.network.model.Photo
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

interface PhotoDetailsDataSource {
    fun getPhoto(id: String): Single<Photo>

    fun likePhoto(id: String): Completable
    fun unlikePhoto(id: String): Completable
}


class PhotoDetailsDataSourceImpl(private val api: UnsplashApi) : PhotoDetailsDataSource {
    override fun getPhoto(id: String): Single<Photo> =
        api.getPhoto(id)
            .observeOn(AndroidSchedulers.mainThread())

    override fun likePhoto(id: String): Completable =
        api.likePhoto(id)

    override fun unlikePhoto(id: String): Completable =
        api.unlikePhoto(id)
}
