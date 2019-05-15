package com.badoo.ribs.core.helper

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import kotlinx.android.parcel.Parcelize

class TestRouter(
    initialConfiguration: Configuration,
    override val permanentParts: List<Nothing>,
    private val routingActionForC1: RoutingAction<TestView>,
    private val routingActionForC2: RoutingAction<TestView>,
    private val routingActionForC3: RoutingAction<TestView>,
    private val routingActionForC4: RoutingAction<TestView>,
    private val routingActionForC5: RoutingAction<TestView>
) : Router<TestRouter.Configuration, Nothing, TestRouter.Configuration, Nothing, TestView>(
    initialConfiguration = initialConfiguration
) {

    sealed class Configuration : Parcelable {
        @Parcelize object C1 : Configuration() { override fun toString(): String = "C1" }
        @Parcelize object C2 : Configuration() { override fun toString(): String = "C2" }
        @Parcelize object C3 : Configuration() { override fun toString(): String = "C3" }
        @Parcelize object C4 : Configuration() { override fun toString(): String = "C4" }
        @Parcelize object C5 : Configuration() { override fun toString(): String = "C5" }
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<TestView> =
        when (configuration) {
            is Configuration.C1 -> routingActionForC1
            is Configuration.C2 -> routingActionForC2
            is Configuration.C3 -> routingActionForC3
            is Configuration.C4 -> routingActionForC4
            is Configuration.C5 -> routingActionForC5
        }
}
