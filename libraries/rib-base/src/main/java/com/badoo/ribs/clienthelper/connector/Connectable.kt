package com.badoo.ribs.clienthelper.connector

import com.badoo.ribs.minimal.reactive.Relay

interface Connectable<Input, Output> {
    val input: Relay<Input>
    val output: Relay<Output>
}

