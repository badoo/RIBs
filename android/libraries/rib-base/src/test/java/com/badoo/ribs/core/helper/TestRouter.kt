package com.badoo.ribs.core.helper

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature
import com.badoo.ribs.core.routing.history.Routing
import com.nhaarman.mockitokotlin2.mock
import kotlinx.android.parcel.Parcelize

class TestRouter(
    buildParams: BuildParams<Nothing?> = testBuildParams(),
    initialConfiguration: Configuration = Configuration.C1,
    private val routingActionForC1: RoutingAction = mock(),
    private val routingActionForC2: RoutingAction = mock(),
    private val routingActionForC3: RoutingAction = mock(),
    private val routingActionForC4: RoutingAction = mock(),
    private val routingActionForC5: RoutingAction = mock(),
    private val routingActionForC6: RoutingAction = mock(),
    private val routingActionForO1: RoutingAction = mock(),
    private val routingActionForO2: RoutingAction = mock(),
    private val routingActionForO3: RoutingAction = mock()
) : Router<TestRouter.Configuration>(
    buildParams = buildParams,
    routingSource = BackStackFeature(
        buildParams = buildParams,
        initialConfiguration = initialConfiguration
    )
) {

    sealed class Configuration : Parcelable {
        // Content
        @Parcelize object C1 : Configuration() { override fun toString(): String = "C1" }
        @Parcelize object C2 : Configuration() { override fun toString(): String = "C2" }
        @Parcelize object C3 : Configuration() { override fun toString(): String = "C3" }
        @Parcelize object C4 : Configuration() { override fun toString(): String = "C4" }
        @Parcelize object C5 : Configuration() { override fun toString(): String = "C5" }
        @Parcelize data class C6(val i: Int) : Configuration() { override fun toString(): String = "C6" }

        // Overlay
        @Parcelize object O1 : Configuration() { override fun toString(): String = "O1" }
        @Parcelize object O2 : Configuration() { override fun toString(): String = "O2" }
        @Parcelize object O3 : Configuration() { override fun toString(): String = "O3" }
    }

    override fun resolve(routing: Routing<Configuration>): RoutingAction =
        when (routing.configuration) {
            is Configuration.C1 -> routingActionForC1
            is Configuration.C2 -> routingActionForC2
            is Configuration.C3 -> routingActionForC3
            is Configuration.C4 -> routingActionForC4
            is Configuration.C5 -> routingActionForC5
            is Configuration.C6 -> routingActionForC6
            is Configuration.O1 -> routingActionForO1
            is Configuration.O2 -> routingActionForO2
            is Configuration.O3 -> routingActionForO3
        }
}
