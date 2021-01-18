package com.badoo.ribs.sandbox.rib.dialog_example.dialog

import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleChildBuilders

class Dialogs internal constructor(
    builders: DialogExampleChildBuilders
) {
    val themeDialog = ThemeDialog()
    val simpleDialog = SimpleDialog()
    val lazyDialog = LazyDialog()
    val ribDialog = RibDialog(
        builders.loremIpsum
    )
}
