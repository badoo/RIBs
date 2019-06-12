package com.badoo.ribs.example.app

import com.badoo.ribs.customisation.RibCustomisationDirectory
import com.badoo.ribs.customisation.RibCustomisationDirectoryImpl
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.dialog_example.DialogExample
import com.badoo.ribs.example.rib.foo_bar.FooBar
import com.badoo.ribs.example.rib.foo_bar.FooBarViewImpl
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsum
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumViewImpl
import com.badoo.ribs.example.rib.switcher.Switcher

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

fun customisations(block: RibCustomisationDirectoryImpl.() -> Unit): RibCustomisationDirectoryImpl =
    RibCustomisationDirectoryImpl().apply(block)
