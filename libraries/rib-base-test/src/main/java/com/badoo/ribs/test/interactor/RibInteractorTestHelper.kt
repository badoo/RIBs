package com.badoo.ribs.test.interactor

import android.os.Parcelable
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.builder.Builder
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.ActivationMode
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.test.RibTestHelper
import com.badoo.ribs.test.integrationpoint.TestIntegrationPoint
import com.badoo.ribs.test.node.RibNodeTestHelper
import kotlinx.android.parcel.Parcelize

class RibInteractorTestHelper<out R : Rib, View : RibView>(
    val interactor: Interactor<*, View>,
    ribFactory: (buildParams: BuildParams<Nothing?>) -> R,
) : RibTestHelper<R> {
    val rib: R =
        ribFactory(
            BuildParams(
                buildContext = BuildContext.root(
                    savedInstanceState = null,
                    defaultPlugins = { listOf(interactor) }
                ),
                payload = null,
            )
        ).also { rib ->
            val interactors = rib.node.plugins.count { it == interactor }
            require(interactors == 1) {
                if (interactors == 0) {
                    "Use BuildParams that are passed from ribFactory or add interactor manually"
                } else {
                    "Interactor should be added only once, remove it from the node"
                }
            }
        }
    private val nodeTestHelper = RibNodeTestHelper(rib, additionalPlugins = listOf(interactor))

    val integrationPoint: TestIntegrationPoint
        get() = nodeTestHelper.integrationPoint

    override fun getLifecycle(): Lifecycle =
        nodeTestHelper.lifecycle

    override fun moveTo(state: Lifecycle.State) {
        nodeTestHelper.moveTo(state)
    }

    override fun moveToStateAndCheck(state: Lifecycle.State, block: (R) -> Unit) {
        nodeTestHelper.moveToStateAndCheck(state, block)
    }

    fun createChildBuildContext(): BuildContext =
        BuildContext(
            activationMode = ActivationMode.ATTACH_TO_PARENT,
            ancestryInfo = AncestryInfo.Child(rib.node, Routing(Configuration)),
            savedInstanceState = null,
            customisations = rib.node.buildParams.buildContext.customisations,
        )

    fun createChildBuildParams(): BuildParams<*> =
        BuildParams(
            payload = null,
            buildContext = createChildBuildContext()
        )

    fun <R : Rib> attachChild(rib: R) {
        object : Builder<Nothing?, R>() {
            override fun build(buildParams: BuildParams<Nothing?>): R {
                require(rib.node.buildParams.buildContext == buildParams.buildContext) {
                    "Pass proper BuildContext to the child node by using createChildBuildParams() or createChildBuildParams()"
                }
                return rib
            }
        }.build(
            buildContext = createChildBuildContext(),
            payload = null,
        )
        nodeTestHelper.rib.node.attachChildNode(rib.node)
    }

    fun <R : Rib> withChild(rib: R, block: (child: R) -> Unit) {
        attachChild(rib)
        block(rib)
        nodeTestHelper.rib.node.detachChildNode(rib.node, isRecreating = false)
    }

    @Parcelize
    private object Configuration : Parcelable

}
