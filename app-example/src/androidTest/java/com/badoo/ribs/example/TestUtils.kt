package com.badoo.ribs.example

import com.badoo.ribs.example.network.model.Links
import com.badoo.ribs.example.network.model.Urls
import com.badoo.ribs.example.network.model.User

const val MY_ID = "MyID"
const val MY_USERNAME = "MyUsername"
const val MY_NAME = "MyName"
const val MY_BIO = "My name is MyName. I am a test user."
const val MY_LOCATION = "London, United Kingdom"

const val TEST_IMAGE_URL = "https://example.com/test.jpg"
const val TEST_EXTERNAL_URL = "https://example.com/test.html"

val MY_PROFILE_IMAGE_URLS: Urls =
    Urls(
        thumb = TEST_IMAGE_URL,
        small = TEST_IMAGE_URL,
        medium = TEST_IMAGE_URL,
        regular = TEST_IMAGE_URL,
        large = TEST_IMAGE_URL,
        full = TEST_IMAGE_URL,
        raw = TEST_IMAGE_URL
    )

val MY_LINKS: Links =
    Links(
        self = TEST_EXTERNAL_URL,
        html = TEST_EXTERNAL_URL,
        photos = TEST_EXTERNAL_URL,
        likes = TEST_EXTERNAL_URL,
        portfolio = TEST_EXTERNAL_URL,
        download = TEST_EXTERNAL_URL,
        downloadLocation = TEST_EXTERNAL_URL
    )

val MY_USER: User =
    User(
        id = MY_ID,
        username = MY_USERNAME,
        name = MY_NAME,
        bio = MY_BIO,
        location = MY_LOCATION,
        totalPhotos = 5,
        totalLikes = 100,
        totalCollections = 4,
        profileImage = MY_PROFILE_IMAGE_URLS,
        links = MY_LINKS
    )
