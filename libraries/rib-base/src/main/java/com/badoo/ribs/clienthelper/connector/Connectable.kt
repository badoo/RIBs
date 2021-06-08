package com.badoo.ribs.clienthelper.connector

import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.badoo.ribs.minimal.reactive.Relay

interface Connectable<Input, Output> : NodeLifecycleAware {
    val input: Relay<Input>
    val output: Relay<Output>
}

