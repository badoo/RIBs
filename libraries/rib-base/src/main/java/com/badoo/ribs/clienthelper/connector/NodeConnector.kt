package com.badoo.ribs.clienthelper.connector

import com.badoo.ribs.minimal.reactive.Relay

class NodeConnector<Input, Output>(
    override val input: Relay<Input> = Relay(),
    override val output: Relay<Output> = Relay()
): Connectable<Input, Output>
