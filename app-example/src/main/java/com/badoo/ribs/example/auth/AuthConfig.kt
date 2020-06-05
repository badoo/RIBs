package com.badoo.ribs.example.auth

object AuthConfig {
    const val redirectUri: String = "ribs-example-app://auth-callback"

    private const val scope: String =
        "public+read_user+read_photos+write_likes+read_collections+write_collections"

    fun authUrl(accessKey: String): String =
        "https://unsplash.com/oauth/authorize?client_id=$accessKey&redirect_uri=$redirectUri&response_type=code&scope=$scope"
}

