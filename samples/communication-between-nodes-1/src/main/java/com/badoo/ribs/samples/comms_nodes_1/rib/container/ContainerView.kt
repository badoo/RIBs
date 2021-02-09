package com.badoo.ribs.samples.comms_nodes_1.rib.container

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.comms_nodes_1.R

interface ContainerView : RibView {

    interface Factory : ViewFactory<Nothing?, ContainerView>
}


class ContainerViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    ContainerView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_container
    ) : ContainerView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> ContainerView = {
            ContainerViewImpl(
                it.inflate(layoutRes)
            )
        }
    }
}
