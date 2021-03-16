package com.badoo.ribs.sandbox.rib.compose_parent

import android.view.ViewGroup
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.sandbox.compose.R

interface ComposeParentView : RibView {

    interface Factory : ViewFactoryBuilder<Nothing?, ComposeParentView>
}


class ComposeParentViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    ComposeParentView {

    class Factory : ComposeParentView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<ComposeParentView> = ViewFactory {
            ComposeParentViewImpl(
                it.inflate(R.layout.rib_compose_parent)
            )
        }
    }
}
