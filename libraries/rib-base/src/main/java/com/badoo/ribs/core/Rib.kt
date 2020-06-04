package com.badoo.ribs.core

import java.util.UUID

interface Rib {

    val node: Node<*>

    data class Identifier(
        val uuid: UUID
    ) : RequestCodeClient {

        override val id: String
            get() = uuid.toString()

        companion object {
            internal const val KEY_UUID = "rib.uuid"
        }
    }
}
