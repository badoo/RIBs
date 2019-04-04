package com.badoo.ribs.android.lifecycle

import android.support.test.InstrumentationRegistry
import android.support.test.annotation.UiThreadTest
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.runner.lifecycle.Stage
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.test.util.ribs.root.TestRoot
import com.badoo.ribs.test.util.ribs.root.TestRootRouter
import com.badoo.ribs.test.util.waitFor
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test


class ChildNodeLifecycleTest {

    val rootProvider = TestRoot.Provider()
    val router: TestRootRouter
        get() = rootProvider.rootNode?.getRouter() as TestRootRouter

    @get:Rule
    val ribsRule = RibsRule { rootProvider.invoke() }

    @Test
    fun whenRootIsCreated_noChildrenAreAttached() {
        assertThat(rootProvider.childNode1?.isAttached).isNull()
        assertThat(rootProvider.childNode2?.isAttached).isNull()
    }

    @UiThreadTest
    @Test
    fun whenConfigurationIsPushed_child1IsAttached() {
        router.push(TestRootRouter.Configuration.Node1)

        assertThat(rootProvider.childNode1?.isAttached).isTrue()
    }

    @UiThreadTest
    @Test
    fun whenConfigurationIsPushed_child2IsAttached() {
        router.push(TestRootRouter.Configuration.Node2)

        assertThat(rootProvider.childNode2?.isAttached).isTrue()
    }

    @UiThreadTest
    @Test
    fun whenTwoConfigurationsArePushed_child1HasOnlyViewDetached() {
        router.push(TestRootRouter.Configuration.Node1)
        router.push(TestRootRouter.Configuration.Node2)

        assertThat(rootProvider.childNode1?.isAttached).isTrue()
        assertThat(rootProvider.childNode1?.isViewAttached).isFalse()

        assertThat(rootProvider.childNode2?.isAttached).isTrue()
        assertThat(rootProvider.childNode2?.isViewAttached).isTrue()
    }

    @UiThreadTest
    @Test
    fun whenConfigurationIsPopped_child1ViewIsRestored() {
        router.push(TestRootRouter.Configuration.Node1)
        router.push(TestRootRouter.Configuration.Node2)
        router.popBackStack()

        assertThat(rootProvider.childNode1?.isAttached).isTrue()
        assertThat(rootProvider.childNode1?.isViewAttached).isTrue()
    }

    @UiThreadTest
    @Test
    fun whenConfigurationIsPopped_child2isDetached() {
        router.push(TestRootRouter.Configuration.Node1)
        router.push(TestRootRouter.Configuration.Node2)
        router.popBackStack()

        assertThat(rootProvider.childNode2?.isAttached).isFalse()
        assertThat(rootProvider.childNode2?.isViewAttached).isFalse()
    }

    @Test
    fun whenBackStackWith2ChildrenIsRestored_lastOneIsAttached() {
        val inst = InstrumentationRegistry.getInstrumentation()

        inst.runOnMainSync {
            router.push(TestRootRouter.Configuration.Node1)
            router.push(TestRootRouter.Configuration.Node2)
        }

        inst.runOnMainSync {
            ribsRule.activity.recreate()
        }

        var activityResumed = false
        ActivityLifecycleMonitorRegistry.getInstance()
            .addLifecycleCallback { _, stage ->
                if (stage == Stage.RESUMED) {
                    activityResumed = true
                }
            }
        waitFor { activityResumed }

        assertThat(rootProvider.childNode1?.isAttached).isFalse()
        assertThat(rootProvider.childNode2?.isAttached).isTrue()
    }
}
