package com.badoo.ribs.example.photo_details

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.photo_details.PhotoDetails.Input
import com.badoo.ribs.example.photo_details.PhotoDetails.Output
import com.badoo.ribs.example.photo_details.routing.PhotoDetailsRouter
import com.badoo.ribs.portal.CanProvidePortal
import com.badoo.ribs.routing.transition.handler.TransitionHandler

interface PhotoDetails : Rib, Connectable<Input, Output> {

    interface Dependency : CanProvidePortal {
        val authDataSource: AuthDataSource
        val photoDetailsDataSource: PhotoDetailsDataSource
    }

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: PhotoDetailsView.Factory = PhotoDetailsViewImpl.Factory(),
        val transitionHandler: TransitionHandler<PhotoDetailsRouter.Configuration>? = null
    ) : RibCustomisation
}
