package com.badoo.ribs.example.app

import com.badoo.ribs.core.directory.Directory
import com.badoo.ribs.core.directory.ViewCustomisationDirectory
import com.badoo.ribs.core.directory.inflateOnDemand
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.dialog_example.DialogExample
import com.badoo.ribs.example.rib.foo_bar.FooBar
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsum
import com.badoo.ribs.example.rib.switcher.Switcher

object AppRibCustomisations : Directory by customisations({
    Switcher::class {
        DialogExample::class {
            + LoremIpsum.Customisation(
                viewFactory = inflateOnDemand(R.layout.rib_lorem_ipsum_override)
            )
        }
    }

    + FooBar.Customisation(
        viewFactory = inflateOnDemand(R.layout.rib_foobar_override)
    )
})

fun customisations(block: ViewCustomisationDirectory.() -> Unit): ViewCustomisationDirectory =
    ViewCustomisationDirectory().apply(block)
