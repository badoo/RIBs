package com.badoo.ribs.samples.dialogs.rib.dialogs_example

import com.badoo.ribs.android.dialog.CanProvideDialogLauncher
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation

interface DialogsExample : Rib {

    interface Dependency : CanProvideDialogLauncher

    class Customisation(
        val viewFactory: DialogsView.Factory = DialogsViewImpl.Factory()
    ) : RibCustomisation
}
