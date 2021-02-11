package com.badoo.ribs.rx.clienthelper.connector

import com.jakewharton.rxrelay2.Relay

interface Connectable<Input, Output> {
    val input: Relay<Input>
    val output: Relay<Output>
}

