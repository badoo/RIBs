package com.badoo.ribs.rx

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildParams
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.disposables.Disposable
import org.junit.Before
import org.junit.Test

class DisposablePluginTest {

    private val disposables = mock<Disposable>()
    private lateinit var node: Node<*>

    @Before
    fun setUp() {
        node = Node<Nothing>(
            buildParams = BuildParams(null, BuildContext.root(null)),
            plugins = listOf(
                disposeOnDetach(disposables)
            ),
            viewFactory = null
        )
    }

    @Test
    fun `onDetach() disposes disposables`() {
        node.onDetach()
        verify(disposables).dispose()
    }
}
