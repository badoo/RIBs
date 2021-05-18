package com.badoo.ribs.test.builder

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.test.assertEquals
import com.badoo.ribs.test.assertTrue

open class RibBuilderStub<Param, R : Rib>(
    val delegate: (BuildParams<Param>) -> R
) : Builder<Param, R>() {

    private val _createdNodes = ArrayList<R>()

    val last: R?
        get() = _createdNodes.lastOrNull()

    val lastNode: Node<*>?
        get() = _createdNodes.lastOrNull()?.node

    var lastParam: Param? = null
        private set

    override fun build(buildParams: BuildParams<Param>): R =
        delegate(buildParams).also {
            _createdNodes += it
            lastParam = buildParams.payload
        }

    fun assertLastParam(param: Param) {
        assertEquals(param, lastParam)
    }

    fun assertLastParam(assert: Param.() -> Boolean) {
        val lastParam = lastParam
        assertTrue(lastParam != null && assert(lastParam)) { "$lastParam does not satisfy requirements" }
    }

    fun assertCreatedNode() {
        assertTrue(lastNode != null) { "Has not created any node" }
    }

    fun assertLastNodeState(state: Lifecycle.State) {
        assertEquals(state, lastNode?.lifecycle?.currentState)
    }

}
