package com.badoo.ribs.rx2.clienthelper.connector

import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.jakewharton.rxrelay2.Relay

interface Connectable<Input, Output> : NodeLifecycleAware {
    val input: Relay<Input>
    val output: Relay<Output>
}

