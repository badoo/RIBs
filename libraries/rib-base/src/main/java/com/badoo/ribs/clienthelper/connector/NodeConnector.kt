package com.badoo.ribs.clienthelper.connector

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay

class NodeConnector<Input, Output>(
    override val input: Relay<Input> = BehaviorRelay.create(), // FIXME
    override val output: Relay<Output> = PublishRelay.create()
): Connectable<Input, Output>
