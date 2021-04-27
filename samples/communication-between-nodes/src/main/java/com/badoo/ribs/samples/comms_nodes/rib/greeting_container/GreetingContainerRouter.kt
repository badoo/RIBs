package com.badoo.ribs.samples.comms_nodes.rib.greeting_container

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.Language
import com.badoo.ribs.samples.comms_nodes.rib.greeting.GreetingBuilder
import com.badoo.ribs.samples.comms_nodes.rib.greeting_container.GreetingContainerRouter.Configuration
import com.badoo.ribs.samples.comms_nodes.rib.greeting_container.GreetingContainerRouter.Configuration.Greeting
import com.badoo.ribs.samples.comms_nodes.rib.greeting_container.GreetingContainerRouter.Configuration.ChooseLanguage
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.LanguageSelector.Dependency
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.LanguageSelectorBuilder
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.LanguageSelectorBuilder.Params
import kotlinx.android.parcel.Parcelize

class GreetingContainerRouter(
    buildParams: BuildParams<Nothing?>,
    routingSource: RoutingSource<Configuration>,
    private val languages: List<Language>
) : Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource
) {

    private val greetingBuilder = GreetingBuilder()
    private val languageSelectorBuilder = LanguageSelectorBuilder(
        dependencies = object : Dependency {
            override val languages: List<Language> = this@GreetingContainerRouter.languages
        }
    )

    sealed class Configuration : Parcelable {
        @Parcelize
        object Greeting : Configuration()

        @Parcelize
        data class ChooseLanguage(val currentLanguage: Language) : Configuration()
    }

    override fun resolve(routing: Routing<Configuration>): Resolution {
        return when (val configuration = routing.configuration) {
            is Greeting -> child { greetingBuilder.build(it) }
            is ChooseLanguage -> child { languageSelectorBuilder.build(it, Params(configuration.currentLanguage)) }
        }
    }
}
