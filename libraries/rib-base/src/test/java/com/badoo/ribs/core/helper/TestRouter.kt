package com.badoo.ribs.core.helper

import android.os.Parcelable
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.Routing
import org.mockito.kotlin.mock
import kotlinx.parcelize.Parcelize

class TestRouter(
    buildParams: BuildParams<Nothing?> = testBuildParams(),
    initialConfiguration: Configuration = Configuration.C1,
    private val resolutionForC1: Resolution = mock(),
    private val resolutionForC2: Resolution = mock(),
    private val resolutionForC3: Resolution = mock(),
    private val resolutionForC4: Resolution = mock(),
    private val resolutionForC5: Resolution = mock(),
    private val resolutionForC6: Resolution = mock(),
    private val resolutionForO1: Resolution = mock(),
    private val resolutionForO2: Resolution = mock(),
    private val resolutionForO3: Resolution = mock()
) : Router<TestRouter.Configuration>(
    buildParams = buildParams,
    routingSource = BackStack(
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

    override fun resolve(routing: Routing<Configuration>): Resolution =
        when (routing.configuration) {
            is Configuration.C1 -> resolutionForC1
            is Configuration.C2 -> resolutionForC2
            is Configuration.C3 -> resolutionForC3
            is Configuration.C4 -> resolutionForC4
            is Configuration.C5 -> resolutionForC5
            is Configuration.C6 -> resolutionForC6
            is Configuration.O1 -> resolutionForO1
            is Configuration.O2 -> resolutionForO2
            is Configuration.O3 -> resolutionForO3
        }
}
