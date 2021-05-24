package com.badoo.ribs.rx2.clienthelper.conector.helpers

import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Input
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Output
import com.badoo.ribs.rx2.clienthelper.connector.Connectable

interface Rx2ConnectableTestRib : Connectable<Input, Output> {

    sealed class Input

    sealed class Output {
        object Output1 : Output()
        object Output2 : Output()
        object Output3 : Output()
    }
}
