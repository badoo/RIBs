package com.badoo.ribs.clienthelper

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay

class NodeConnector<Input, Output>(
    override val input: Relay<Input> = PublishRelay.create(),
    override val output: Relay<Output> = PublishRelay.create()
): Connectable<Input, Output>
