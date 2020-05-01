package com.badoo.ribs.core

import com.badoo.ribs.core.view.RibView
import java.util.UUID

interface Rib<V : RibView> {

    val node: Node<V>

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
