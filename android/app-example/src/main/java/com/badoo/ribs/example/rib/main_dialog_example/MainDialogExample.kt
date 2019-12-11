package com.badoo.ribs.example.rib.main_dialog_example

import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.CanProvideRibCustomisation
import com.badoo.ribs.customisation.RibCustomisation
import com.badoo.ribs.dialog.CanProvideDialogLauncher

interface MainDialogExample : Rib {

    interface Dependency : CanProvideDialogLauncher, CanProvideRibCustomisation

    class Customisation(
        val viewFactory: MainDialogExampleView.Factory = MainDialogExampleViewImpl.Factory()
    ) : RibCustomisation
}
