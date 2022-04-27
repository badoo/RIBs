package com.badoo.ribs.sandbox.rib.dialog_example

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.android.dialog.CanProvideDialogLauncher
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExample.Input
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExample.Output
import com.bumble.appyx.utils.customisations.NodeCustomisation

interface DialogExample : Rib, Connectable<Input, Output> {

    interface Dependency : CanProvideDialogLauncher

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: DialogExampleView.Factory = DialogExampleViewImpl.Factory()
    ) : NodeCustomisation
}
