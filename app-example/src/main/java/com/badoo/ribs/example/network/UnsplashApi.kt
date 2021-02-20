package com.badoo.ribs.example.network

import com.badoo.ribs.example.network.model.AccessToken
import com.badoo.ribs.example.network.model.Collection
import com.badoo.ribs.example.network.model.Photo
import com.badoo.ribs.example.network.model.SearchResult
import com.badoo.ribs.example.network.model.User
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface UnsplashApi {

    /**
     * https://unsplash.com/documentation#authorization-workflow
     */
    @SuppressWarnings("LongParameterList")
    @POST()
    fun requestAccessToken(
        @Url url:String = "https://unsplash.com/oauth/token",
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("redirect_uri") redirectUri: String,
        @Query("code") code: String,
        @Query("grant_type") grantType: String = "authorization_code"
    ): Single<AccessToken>

    /**
     * https://unsplash.com/documentation#get-the-users-profile
     */
    @GET("me")
    fun getMyProfile(): Single<User>

    //region User
    /**
     * https://unsplash.com/documentation#get-a-users-public-profile
     */
    @GET("users/{username}")
    fun getUserProfile(@Path("username") username: String): Single<User>

    /**
     * https://unsplash.com/documentation#list-a-users-photos
     */
    @GET("users/{username}/photos")
    fun getUserPhotos(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<List<Photo>>

    /**
     * https://unsplash.com/documentation#list-a-users-liked-photos
     */
    @GET("users/{username}/likes")
    fun getUserLikedPhotos(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<List<Photo>>

    /**
     * https://unsplash.com/documentation#list-a-users-collections
     */
    @GET("users/{username}/collections")
    fun getUserCollections(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<List<Collection>>
    //endregion

    //region Photos
    /**
     * https://unsplash.com/documentation#list-photos
     */
    @GET("photos")
    fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String? = null
    ): Single<List<Photo>>

    /**
     * https://unsplash.com/documentation#get-a-photo
     */
    @GET("photos/{id}")
    fun getPhoto(@Path("id") id: String): Single<Photo>

    /**
     * https://unsplash.com/documentation#track-a-photo-download
     */
    @GET("photos/{id}/download")
    fun trackDownload(@Path("id") id: String): Completable

    /**
     * https://unsplash.com/documentation#like-a-photo
     */
    @POST("photos/{id}/like")
    fun likePhoto(@Path("id") id: String): Single<Unit>

    /**
     * https://unsplash.com/documentation#unlike-a-photo
     */
    @DELETE("photos/{id}/like")
    fun unlikePhoto(@Path("id") id: String): Single<Unit>
    //endregion

    //region Search
    /**
     * https://unsplash.com/documentation#search-photos
     */
    @GET("search/photos")
    fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String? = null
    ): Single<SearchResult<Photo>>

    /**
     * https://unsplash.com/documentation#search-collections
     */
    @GET("search/collections")
    fun searchCollections(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<SearchResult<Collection>>

    /**
     * https://unsplash.com/documentation#search-users
     */
    @GET("search/collections")
    fun searchUser(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<SearchResult<User>>
    //endregion

    //region Collections
    /**
     * https://unsplash.com/documentation#list-collections
     */
    @GET("collections")
    fun getCollections(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<List<Collection>>

    /**
     * https://unsplash.com/documentation#list-featured-collections
     */
    @GET("collections/featured")
    fun getFeaturedCollections(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<List<Collection>>

    /**
     * https://unsplash.com/documentation#get-a-collection
     */
    @GET("collections/{id}")
    fun getCollection(@Path("id") id: Int): Single<Collection>

    /**
     * https://unsplash.com/documentation#get-a-collections-photos
     */
    @GET("users/{username}/photos")
    fun getCollectionPhotos(
        @Path("id") id: Int,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<List<Photo>>

    /**
     * https://unsplash.com/documentation#create-a-new-collection
     */
    @POST("collections")
    fun createNewCollection(
        @Query("title") title: String,
        @Query("description") description: String?,
        @Query("private") isPrivate: Boolean?
    ): Single<Collection>

    /**
     * https://unsplash.com/documentation#update-an-existing-collection
     */
    @POST("collections/{id}")
    fun updateCollection(
        @Path("id") id: Int,
        @Query("title") title: String?,
        @Query("description") description: String?,
        @Query("private") isPrivate: Boolean?
    ): Single<Collection>

    /**
     * https://unsplash.com/documentation#delete-a-collection
     */
    @DELETE("collections/{id}")
    fun deleteCollection(
        @Path("id") id: Int
    ): Completable

    /**
     * https://unsplash.com/documentation#add-a-photo-to-a-collection
     */
    @POST("collections/{collection_id}/add")
    fun addPhotoToCollection(
        @Path("collection_id") collectionId: Int,
        @Query("photo_id") photoId: String
    ): Completable
    //endregion
}
