package com.badoo.ribs.core

import java.util.UUID

interface Rib {

    data class Identifier(
        val uuid: UUID
    ) : Identifiable {

        override val id: String
            get() = uuid.toString()

        companion object {
            internal const val KEY_UUID = "rib.uuid"
        }
    }
}
