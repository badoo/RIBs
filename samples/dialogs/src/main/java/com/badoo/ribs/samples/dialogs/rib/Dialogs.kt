package com.badoo.ribs.samples.dialogs.rib

import com.badoo.ribs.android.dialog.CanProvideDialogLauncher
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation

interface Dialogs : Rib {

    interface Dependency : CanProvideDialogLauncher

    class Customisation(
            val viewFactory: DialogsView.Factory = DialogsViewImpl.Factory()
    ) : RibCustomisation
}
