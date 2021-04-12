package com.badoo.ribs.routing.state.feature

internal typealias InternalTransactionConsumer<C> = (Transaction.InternalTransaction<C>) -> Unit
