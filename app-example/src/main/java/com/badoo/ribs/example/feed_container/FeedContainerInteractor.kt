package com.badoo.ribs.example.feed_container

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams

internal class FeedContainerInteractor(
    buildParams: BuildParams<*>
) : Interactor<FeedContainer, FeedContainerView>(
    buildParams = buildParams
) {

    override fun onAttach(nodeLifecycle: Lifecycle) {
        nodeLifecycle.createDestroy {
        }
    }

    override fun onViewCreated(view: FeedContainerView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
        }
    }

    override fun onChildCreated(child: Node<*>) {
        /**
         * TODO bind children here and delete this comment block.
         *
         *  At this point children haven't set their own bindings yet,
         *  so it's safe to setup listening to their output before they start emitting.
         *
         *  On the other hand, they're not ready to receive inputs yet. Usually this is alright.
         *  If it's a requirement though, create those bindings in [onAttachChild]
         */
        // child.lifecycle.createDestroy {
        // when (child) {
        // is Child1 -> bind(child.output to someConsumer)
        // }
        // }
    }
}
