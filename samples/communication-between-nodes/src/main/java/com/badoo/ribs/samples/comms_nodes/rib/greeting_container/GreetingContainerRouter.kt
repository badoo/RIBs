package com.badoo.ribs.samples.comms_nodes.rib.greeting_container

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.samples.comms_nodes.app.Language
import com.badoo.ribs.samples.comms_nodes.rib.greeting.builder.GreetingBuilder
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.builder.LanguageSelectorBuilder
import kotlinx.android.parcel.Parcelize

class GreetingContainerRouter(
    buildParams: BuildParams<Nothing?>,
    routingSource: RoutingSource<Configuration>,
    private val greetingBuilder: GreetingBuilder,
    private val languages: List<Language>
) : Router<GreetingContainerRouter.Configuration>(
    buildParams = buildParams,
    routingSource = routingSource
) {
    sealed class Configuration : Parcelable {
        @Parcelize
        object Greeting : Configuration()

        @Parcelize
        data class LanguageSelector(val currentLanguage: Language) : Configuration()
    }

    override fun resolve(routing: Routing<Configuration>): Resolution {
        return when (val configuration = routing.configuration) {
            is Configuration.Greeting -> child { greetingBuilder.build(it) }
            is Configuration.LanguageSelector -> child {
                LanguageSelectorBuilder(languages = languages, currentLanguage = configuration.currentLanguage).build(it)
            }
        }
    }
}