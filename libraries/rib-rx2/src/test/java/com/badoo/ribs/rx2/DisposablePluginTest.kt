package com.badoo.ribs.rx2

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildParams
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import io.reactivex.disposables.Disposable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DisposablePluginTest {

    private val disposables = mock<Disposable>()
    private lateinit var node: Node<*>

    @BeforeEach
    fun setUp() {
        node = Node<Nothing>(
            buildParams = BuildParams(null, BuildContext.root(null)),
            plugins = listOf(
                disposables(disposables)
            ),
            viewFactory = null
        )
    }

    @Test
    fun `onDestroy() disposes disposables`() {
        node.onDestroy(isRecreating = false)
        verify(disposables).dispose()
    }
}
