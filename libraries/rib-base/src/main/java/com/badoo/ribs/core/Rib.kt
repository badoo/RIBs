package com.badoo.ribs.core

import java.util.UUID

/**
 * The conceptual main element of the system.
 */
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
