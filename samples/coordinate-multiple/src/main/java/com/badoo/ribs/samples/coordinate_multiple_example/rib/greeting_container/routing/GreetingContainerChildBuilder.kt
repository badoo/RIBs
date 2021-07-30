package com.badoo.ribs.samples.coordinate_multiple_example.rib.greeting_container.routing

import com.badoo.ribs.samples.coordinate_multiple_example.rib.greeting.Greeting
import com.badoo.ribs.samples.coordinate_multiple_example.rib.greeting.GreetingBuilder
import com.badoo.ribs.samples.coordinate_multiple_example.rib.greeting_container.GreetingContainer
import com.badoo.ribs.samples.coordinate_multiple_example.rib.language_selector.LanguageSelector
import com.badoo.ribs.samples.coordinate_multiple_example.rib.language_selector.LanguageSelectorBuilder

internal class GreetingContainerChildBuilder(
    dependency: GreetingContainer.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val greetingBuilder: GreetingBuilder = GreetingBuilder(subtreeDeps)
    val languageSelectorBuilder: LanguageSelectorBuilder = LanguageSelectorBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: GreetingContainer.Dependency
    ) : GreetingContainer.Dependency by dependency,
        Greeting.Dependency,
        LanguageSelector.Dependency
}
