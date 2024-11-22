package com.badoo.ribs.core.helper

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.store.RetainedInstanceStore
import org.mockito.kotlin.mock

open class TestNode(
    buildParams: BuildParams<*> = testBuildParams(),
    viewFactory: ViewFactory<TestView>? = TestViewFactory(),
    router: Router<*> = mock(),
    plugins: List<Plugin> = emptyList()
) : Node<TestView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    retainedInstanceStore = RetainedInstanceStore,
    isOnMainThreadFunc = { true },
    plugins = plugins + listOf(router)
), TestRib {

    var handleBackPress: Boolean =
        false

    var handleBackPressInvoked: Boolean =
        false

    fun makeActive(isActive: Boolean) {
        onAttachToView()
        markPendingDetach(!isActive)
        markPendingViewDetach(!isActive)
    }

    override fun handleBackPress(): Boolean =
        handleBackPress.also {
            handleBackPressInvoked = true
        }
}
