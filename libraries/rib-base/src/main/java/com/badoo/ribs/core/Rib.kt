package com.badoo.ribs.core

import java.util.UUID

interface Rib {

    val node: Node<*>

    data class Identifier(
        val uuid: UUID
    ) {

        companion object {
            internal const val KEY_UUID = "rib.uuid"
        }
    }
}
