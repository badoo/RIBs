package com.badoo.ribs.core.helper.connectable

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.helper.connectable.ConnectableTestRib.Input
import com.badoo.ribs.core.helper.connectable.ConnectableTestRib.Output

interface ConnectableTestRib : Connectable<Input, Output> {

    sealed class Input

    sealed class Output {
        object Output1 : Output()
        object Output2 : Output()
        object Output3 : Output()
    }
}
