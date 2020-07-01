package com.badoo.ribs.example.image

interface ImageDownloader {

    fun download(url: String, placeholder: Int, target: ImageTarget)
}
