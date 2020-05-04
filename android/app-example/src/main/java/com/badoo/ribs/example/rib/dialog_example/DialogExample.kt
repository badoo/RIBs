package com.badoo.ribs.example.rib.dialog_example

import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.RibCustomisation
import com.badoo.ribs.dialog.CanProvideDialogLauncher

interface DialogExample : Rib {

    interface Dependency : CanProvideDialogLauncher

    class Customisation(
        val viewFactory: DialogExampleView.Factory = DialogExampleViewImpl.Factory()
    ) : RibCustomisation
}
