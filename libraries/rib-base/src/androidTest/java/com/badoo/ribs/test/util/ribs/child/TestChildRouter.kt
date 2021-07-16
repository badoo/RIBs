package com.badoo.ribs.test.util.ribs.child

import android.os.Parcelable
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.source.impl.Empty
import com.badoo.ribs.test.util.ribs.child.TestChildRouter.Configuration
import kotlinx.parcelize.Parcelize

class TestChildRouter(
    buildParams: BuildParams<Nothing?>
): Router<Configuration>(
    buildParams = buildParams,
    routingSource = Empty()
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        Resolution.noop()
}
