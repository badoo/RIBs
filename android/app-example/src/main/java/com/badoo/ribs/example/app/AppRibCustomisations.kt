package com.badoo.ribs.example.app

import com.badoo.ribs.customisation.RibCustomisationDirectory
import com.badoo.ribs.customisation.RibCustomisationDirectoryImpl
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.dialog_lorem_ipsum.DialogLoremIpsum
import com.badoo.ribs.example.rib.dialog_lorem_ipsum.DialogLoremIpsumViewImpl
import com.badoo.ribs.example.rib.main_dialog_example.MainDialogExample
import com.badoo.ribs.example.rib.main_foo_bar.MainFooBar
import com.badoo.ribs.example.rib.main_foo_bar.MainFooBarViewImpl
import com.badoo.ribs.example.rib.switcher.Switcher

object AppRibCustomisations : RibCustomisationDirectory by customisations({
    Switcher::class {
        MainDialogExample::class {
            + DialogLoremIpsum.Customisation(
                viewFactory = DialogLoremIpsumViewImpl.Factory(R.layout.rib_dialog_lorem_ipsum_override)
            )
        }
    }

    + MainFooBar.Customisation(
        viewFactory = MainFooBarViewImpl.Factory(R.layout.rib_main_foobar_override)
    )
})

fun customisations(block: RibCustomisationDirectoryImpl.() -> Unit): RibCustomisationDirectoryImpl =
    RibCustomisationDirectoryImpl().apply(block)
