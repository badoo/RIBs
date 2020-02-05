package com.badoo.ribs.core

import java.util.UUID

interface Rib {

    data class Identifier(
        val rib: Rib,
        val uuid: UUID,
        val tag: Any? = null // can be set by client code to anything
    ) : Identifiable {

        override val id: String
            get() = uuid.toString()

        companion object {
            internal const val KEY_UUID = "rib.uuid"
        }
    }
}
