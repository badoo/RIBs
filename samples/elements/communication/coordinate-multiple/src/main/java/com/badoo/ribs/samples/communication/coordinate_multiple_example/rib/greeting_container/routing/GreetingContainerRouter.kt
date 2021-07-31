package com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting_container.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting_container.routing.GreetingContainerRouter.Configuration
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting_container.routing.GreetingContainerRouter.Configuration.ChooseLanguage
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting_container.routing.GreetingContainerRouter.Configuration.Greeting
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.language_selector.Language
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.language_selector.LanguageSelectorBuilder.Params
import kotlinx.parcelize.Parcelize

internal class GreetingContainerRouter(
    buildParams: BuildParams<Nothing?>,
    routingSource: RoutingSource<Configuration>,
    private val childBuilder: GreetingContainerChildBuilder
) : Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource
) {
    sealed class Configuration : Parcelable {
        @Parcelize
        object Greeting : Configuration()

        @Parcelize
        data class ChooseLanguage(val currentLanguage: Language) : Configuration()
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        with(childBuilder) {
            when (val configuration = routing.configuration) {
                is Greeting -> child { greetingBuilder.build(it) }
                is ChooseLanguage -> child { languageSelectorBuilder.build(it, Params(configuration.currentLanguage)) }
            }
        }
}
