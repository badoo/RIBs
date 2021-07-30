package com.badoo.ribs.samples.menu_example.rib.menu_example.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.RoutingSource.Companion.permanent
import com.badoo.ribs.samples.menu_example.rib.menu_example.routing.MenuExampleRouter.Configuration
import com.badoo.ribs.samples.menu_example.rib.menu_example.routing.MenuExampleRouter.Configuration.Content.Child1
import com.badoo.ribs.samples.menu_example.rib.menu_example.routing.MenuExampleRouter.Configuration.Content.Child2
import com.badoo.ribs.samples.menu_example.rib.menu_example.routing.MenuExampleRouter.Configuration.Content.Child3
import com.badoo.ribs.samples.menu_example.rib.menu_example.routing.MenuExampleRouter.Configuration.Permanent.Menu
import kotlinx.parcelize.Parcelize

class MenuExampleRouter internal constructor(
    buildParams: BuildParams<Nothing?>,
    routingSource: RoutingSource<Configuration>,
    private val builders: MenuExampleChildBuilders
) : Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource + permanent(Menu)
) {
    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration() {
            @Parcelize object Menu : Permanent()
        }

        sealed class Content : Configuration() {
            @Parcelize object Child1 : Content()
            @Parcelize object Child2 : Content()
            @Parcelize object Child3 : Content()
        }
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        with(builders) {
            when (routing.configuration) {
                is Menu -> child { menu.build(it) }
                is Child1 -> child { child1.build(it) }
                is Child2 -> child { child2.build(it) }
                is Child3 -> child { child3.build(it) }
            }
        }
}
