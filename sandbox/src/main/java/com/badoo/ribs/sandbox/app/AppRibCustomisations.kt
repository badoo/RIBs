package com.badoo.ribs.sandbox.app

import com.badoo.ribs.core.customisation.RibCustomisationDirectory
import com.badoo.ribs.core.customisation.customisations
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExample
import com.badoo.ribs.sandbox.rib.foo_bar.FooBar
import com.badoo.ribs.sandbox.rib.foo_bar.FooBarViewImpl
import com.badoo.ribs.sandbox.rib.lorem_ipsum.LoremIpsum
import com.badoo.ribs.sandbox.rib.lorem_ipsum.LoremIpsumViewImpl
import com.badoo.ribs.sandbox.rib.switcher.Switcher

object AppRibCustomisations : RibCustomisationDirectory by customisations({
    Switcher::class {
        DialogExample::class {
            + LoremIpsum.Customisation(
                viewFactory = LoremIpsumViewImpl.Factory(R.layout.rib_lorem_ipsum_override)
            )
        }
    }

    + FooBar.Customisation(
        viewFactory = FooBarViewImpl.Factory(R.layout.rib_foobar_override)
    )
})
