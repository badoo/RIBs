package com.badoo.ribs.example.photo_details.routing

import com.badoo.ribs.example.photo_details.PhotoDetails

internal class PhotoDetailsChildBuilders(
    dependency: PhotoDetails.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    class SubtreeDependency(
        dependency: PhotoDetails.Dependency
    ) : PhotoDetails.Dependency by dependency
}



