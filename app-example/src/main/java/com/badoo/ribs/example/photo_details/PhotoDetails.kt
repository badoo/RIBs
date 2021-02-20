package com.badoo.ribs.example.photo_details

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.example.photo_details.PhotoDetails.Input
import com.badoo.ribs.example.photo_details.PhotoDetails.Output
import com.badoo.ribs.example.photo_details.routing.PhotoDetailsRouter
import com.badoo.ribs.portal.CanProvidePortal
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.rx2.clienthelper.connector.Connectable

interface PhotoDetails : Rib, Connectable<Input, Output> {

    interface Dependency : CanProvidePortal {
        val photoDetailsDataSource: PhotoDetailsDataSource
    }

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: PhotoDetailsView.Factory = PhotoDetailsViewImpl.Factory(),
        val transitionHandler: TransitionHandler<PhotoDetailsRouter.Configuration>? = null
    ) : RibCustomisation
}
