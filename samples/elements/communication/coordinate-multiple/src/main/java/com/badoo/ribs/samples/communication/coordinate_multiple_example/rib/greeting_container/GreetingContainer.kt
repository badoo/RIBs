package com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting_container

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation

interface GreetingContainer : Rib {

    interface Dependency

    class Customisation(
        val viewFactory: GreetingContainerView.Factory = GreetingContainerViewImpl.Factory(),
    ) : RibCustomisation
}
