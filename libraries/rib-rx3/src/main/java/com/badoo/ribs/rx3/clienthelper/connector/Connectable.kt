package com.badoo.ribs.rx3.clienthelper.connector

import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.jakewharton.rxrelay3.Relay

interface Connectable<Input, Output> : NodeLifecycleAware {
    val input: Relay<Input>
    val output: Relay<Output>
}

