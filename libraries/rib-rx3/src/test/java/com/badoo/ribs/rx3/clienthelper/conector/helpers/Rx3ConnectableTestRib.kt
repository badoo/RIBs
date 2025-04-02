package com.badoo.ribs.rx3.clienthelper.conector.helpers

import com.badoo.ribs.rx3.clienthelper.conector.helpers.Rx3ConnectableTestRib.Input
import com.badoo.ribs.rx3.clienthelper.conector.helpers.Rx3ConnectableTestRib.Output
import com.badoo.ribs.rx3.clienthelper.connector.Connectable

interface Rx3ConnectableTestRib : Connectable<Input, Output> {

    sealed class Input

    sealed class Output {
        data object Output1 : Output()
        data object Output2 : Output()
        data object Output3 : Output()
    }
}
