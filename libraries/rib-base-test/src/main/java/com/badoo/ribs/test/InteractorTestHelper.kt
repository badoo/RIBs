package com.badoo.ribs.test

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import org.mockito.Mockito.mock

class InteractorTestHelper<View : RibView>(
    val interactor: Interactor<*, View>,
    val viewFactory: ViewFactory<View>? = null
) {
    var nodeCreator: () -> Node<View> = {
        Node(
            buildParams = emptyBuildParams(),
            viewFactory = viewFactory,
            plugins = listOf(interactor)
        )
    }

    fun moveToStateAndCheck(state: Lifecycle.State, block: (Node<View>) -> Unit) {
        when (state) {
            Lifecycle.State.DESTROYED,
            Lifecycle.State.INITIALIZED -> throw IllegalArgumentException("Unsupported state: $state")
            Lifecycle.State.CREATED -> toAttachViewState(block)
            Lifecycle.State.STARTED -> toStartState(block)
            Lifecycle.State.RESUMED -> toResumeState(block)
        }
    }

    private fun toResumeState(block: (Node<View>) -> Unit) {
        toStartState {
            it.onResume()
            block(it)
            it.onPause()
        }
    }

    private fun toStartState(block: (Node<View>) -> Unit) {
        toAttachViewState {
            it.onStart()
            block(it)
            it.onStop()
        }
    }

    private fun toAttachViewState(block: (Node<View>) -> Unit) {
        val node = nodeCreator()
        node.onCreate()
        node.onCreateView(mock(RibView::class.java))
        node.onAttachToView()
        block(node)
        node.onDetachFromView()
        node.onDestroy(isRecreating = false)
    }
}
