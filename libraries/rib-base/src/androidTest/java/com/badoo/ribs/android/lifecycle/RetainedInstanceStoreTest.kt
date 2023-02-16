package com.badoo.ribs.android.lifecycle

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisationDirectoryImpl
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.store.RetainedInstanceStore
import com.badoo.ribs.test.RibsRule
import com.badoo.ribs.test.util.finishActivitySync
import com.badoo.ribs.test.util.restartActivitySync
import com.badoo.ribs.test.util.ribs.root.TestRoot
import com.badoo.ribs.test.util.ribs.root.builder.TestRootBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.KClass

class RetainedInstanceStoreTest {
    private val provider = TestRoot.Provider()
    private val stubRetainedInstanceStore = StubRetainedInstanceStore()

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
            savedInstanceState = savedInstanceState,
            retainedInstanceStore = stubRetainedInstanceStore
        )
    }

    @Test
    fun WHEN_activity_finished_THEN_retained_instance_store_content_is_removed() {
        ribsRule.finishActivitySync()

        assertThat(stubRetainedInstanceStore.removeAllCalled).isTrue
    }

    @Test
    fun WHEN_activity_recreated_THEN_retained_instance_store_content_is_not_removed() {
        ribsRule.restartActivitySync()

        assertThat(stubRetainedInstanceStore.removeAllCalled).isFalse
    }

    class StubRetainedInstanceStore : RetainedInstanceStore {
        var removeAllCalled: Boolean = false

        override fun <T : Any> get(
            owner: Rib.Identifier,
            clazz: KClass<*>,
            disposer: (T) -> Unit,
            factory: () -> T
        ): T = RetainedInstanceStore.get(owner, clazz, disposer, factory)

        override fun removeAll(owner: Rib.Identifier) {
            removeAllCalled = true
            RetainedInstanceStore.removeAll(owner)
        }

    }
}
