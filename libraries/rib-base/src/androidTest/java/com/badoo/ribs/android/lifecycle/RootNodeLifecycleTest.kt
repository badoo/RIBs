package com.badoo.ribs.android.lifecycle

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.customisation.RibCustomisationDirectoryImpl
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.test.RibsRule
import com.badoo.ribs.test.util.finishActivitySync
import com.badoo.ribs.test.util.ribs.root.TestRoot
import com.badoo.ribs.test.util.ribs.root.builder.TestRootBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

class RootNodeLifecycleTest {
    private val provider = TestRoot.Provider()
    val node get() = provider.rootNode!!

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState ->
        provider.create(
            buildParams = BuildParams(
                payload = TestRootBuilder.Params(false),
                buildContext = BuildContext.root(
                    savedInstanceState = null,
                    customisations = RibCustomisationDirectoryImpl()
                )
            ),
            dialogLauncher = activity.integrationPoint.dialogLauncher, // TODO reconsider if we need direct dependency at all
            savedInstanceState = savedInstanceState
        )
    }

    @Test
    fun whenActivityResumed_nodeIsAttached() {
        assertThat(node.isAttached).isTrue()
    }

    @Test
    fun whenActivityResumed_viewIsAttached() {
        assertThat(node.isAttachedToView).isTrue()
    }

    @Test
    fun whenActivityResumed_lifecycleEventsAreDispatched() {
        provider.viewLifecycleObserver.assertValues(
            Lifecycle.Event.ON_CREATE,
            Lifecycle.Event.ON_START,
            Lifecycle.Event.ON_RESUME
        )
    }

    @Test
    fun whenActivityDestroyed_nodeIsDetached() {
        ribsRule.finishActivitySync()

        assertThat(node.isAttached).isFalse()
    }

    @Test
    fun whenActivityDestroyed_viewIsDetached() {
        ribsRule.finishActivitySync()

        assertThat(node.isAttachedToView).isFalse()
    }

    @Test
    fun whenActivityDestroyed_lifecycleEventsAreDispatched() {
        val viewLifecycleObserver = provider.viewLifecycleObserver
        viewLifecycleObserver.clear()

        ribsRule.finishActivitySync()

        viewLifecycleObserver.assertValues(
            Lifecycle.Event.ON_PAUSE,
            Lifecycle.Event.ON_STOP,
            Lifecycle.Event.ON_DESTROY
        )
    }
}
