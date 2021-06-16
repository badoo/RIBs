package com.badoo.ribs.test.router

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.test.builder.RibBuilderStub
import com.badoo.ribs.test.node.RibNodeStub
import java.io.Closeable

class RibRouterTestHelper<C : Parcelable, out R : Router<C>>(
    buildParams: BuildParams<Nothing?>,
    val router: R
) : Closeable {
    private val node: Node<*> =
        RibBuilderStub<Nothing?, Rib> {
            RibNodeStub<RibView>(
                buildParams = it,
                plugins = listOf(router),
            )
        }.build(
            payload = null,
            buildContext = buildParams.buildContext,
        ).node

    init {
        node.onCreate()
    }

    override fun close() {
        node.onDestroy(false)
    }
}
