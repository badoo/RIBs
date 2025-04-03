package com.badoo.ribs.samples.android.dialogs.rib.dummy

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.samples.android.dialogs.rib.dummy.Dummy.Input
import com.badoo.ribs.samples.android.dialogs.rib.dummy.Dummy.Output

interface Dummy : Rib, Connectable<Input, Output> {

    sealed class Input

    sealed class Output {
        data object SomeEvent : Output()
    }
}
