package com.badoo.ribs.rx2.clienthelper.connector

import com.badoo.ribs.core.communication.Unlockable
import com.jakewharton.rxrelay2.Relay

interface Connectable<Input, Output> : Unlockable {
    val input: Relay<Input>
    val output: Relay<Output>
}

