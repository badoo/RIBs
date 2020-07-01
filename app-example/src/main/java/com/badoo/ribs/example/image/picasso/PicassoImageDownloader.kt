package com.badoo.ribs.example.image.picasso

import com.badoo.ribs.example.image.ImageDownloader
import com.badoo.ribs.example.image.ImageTarget
import com.squareup.picasso.Picasso

class PicassoImageDownloader : ImageDownloader {

    override fun download(url: String, placeholder: Int, target: ImageTarget) {
        Picasso.get().load(url).placeholder(placeholder).into(target)
    }

}
