package com.badoo.ribs.android.lifecycle

import android.support.test.InstrumentationRegistry
import android.support.test.annotation.UiThreadTest
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.test.util.ribs.root.TestRoot
import com.badoo.ribs.test.util.ribs.root.TestRootRouter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test


class ChildNodeLifecycleTest {

    val rootProvider = TestRoot.Provider()
    val node = rootProvider()

    @get:Rule
    val ribsRule = RibsRule { node }

    @Test
    fun whenRootIsCreated_noChildrenAreAttached() {
        assertThat(rootProvider.childNode1.isAttached).isFalse()
        assertThat(rootProvider.childNode2.isAttached).isFalse()
    }

    @UiThreadTest
    @Test
    fun whenConfigurationIsPushed_child1IsAttached() {
        rootProvider.router.push(TestRootRouter.Configuration.Node1)

        assertThat(rootProvider.childNode1.isAttached).isTrue()
    }

    @UiThreadTest
    @Test
    fun whenConfigurationIsPushed_child2IsAttached() {
        rootProvider.router.push(TestRootRouter.Configuration.Node2)

        assertThat(rootProvider.childNode2.isAttached).isTrue()
    }

    @UiThreadTest
    @Test
    fun whenTwoConfigurationsArePushed_child1HasOnlyViewDetached() {
        rootProvider.router.push(TestRootRouter.Configuration.Node1)
        rootProvider.router.push(TestRootRouter.Configuration.Node2)

        assertThat(rootProvider.childNode1.isAttached).isTrue()
        assertThat(rootProvider.childNode1.isViewAttached).isFalse()

        assertThat(rootProvider.childNode2.isAttached).isTrue()
        assertThat(rootProvider.childNode2.isViewAttached).isTrue()
    }

    @UiThreadTest
    @Test
    fun whenConfigurationIsPopped_child1ViewIsRestored() {
        rootProvider.router.push(TestRootRouter.Configuration.Node1)
        rootProvider.router.push(TestRootRouter.Configuration.Node2)
        rootProvider.router.popBackStack()

        assertThat(rootProvider.childNode1.isAttached).isTrue()
        assertThat(rootProvider.childNode1.isViewAttached).isTrue()
    }

    @UiThreadTest
    @Test
    fun whenConfigurationIsPopped_child2isDetached() {
        rootProvider.router.push(TestRootRouter.Configuration.Node1)
        rootProvider.router.push(TestRootRouter.Configuration.Node2)
        rootProvider.router.popBackStack()

        assertThat(rootProvider.childNode2.isAttached).isFalse()
        assertThat(rootProvider.childNode2.isViewAttached).isFalse()
    }

    @Ignore("The test is not waiting")
    @Test
    fun when2ChildBackStackIsRestored_bothAreAttached() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            rootProvider.router.push(TestRootRouter.Configuration.Node1)
            rootProvider.router.push(TestRootRouter.Configuration.Node2)
        }

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            ribsRule.activity.recreate()
        }


        assertThat(rootProvider.childNode1.isAttached).isFalse()
        assertThat(rootProvider.childNode2.isAttached).isTrue()
    }
}
