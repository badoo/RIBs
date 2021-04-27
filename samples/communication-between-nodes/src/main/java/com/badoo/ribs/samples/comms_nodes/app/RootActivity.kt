package com.badoo.ribs.samples.comms_nodes.app

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.samples.comms_nodes.R
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.Language.English
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.Language.French
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.Language.German
import com.badoo.ribs.samples.comms_nodes.rib.greeting_container.GreetingContainer.Dependency
import com.badoo.ribs.samples.comms_nodes.rib.greeting_container.GreetingContainerBuilder
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.Language

class RootActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Rib =
        GreetingContainerBuilder(
            dependencies = object : Dependency {
                override val languages: List<Language> = getLanguages()
            }
        ).build(BuildContext.root(savedInstanceState))

    private fun getLanguages() = listOf(English, German, French)
}
