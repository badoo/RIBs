package com.badoo.ribs.routing.source.backstack

import android.os.Parcelable
import com.badoo.ribs.minimal.reactive.Source
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.test.emptyBuildParams
import kotlinx.parcelize.Parcelize
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BackStackTest {

    private val backStack = BackStack<Configuration>(
        initialConfiguration = Configuration.Initial,
        buildParams = emptyBuildParams()
    )

    @Test
    fun `activeConfiguration returns initial configuration`() {
        val configurations = backStack.activeConfigurations.collect()

        assertEquals(listOf(Configuration.Initial), configurations)
    }

    @Test
    fun `activeConfiguration returns last configuration from back stack`() {
        backStack.push(Configuration.Additional)

        val configurations = backStack.activeConfigurations.collect()

        assertEquals(listOf(Configuration.Additional), configurations)
    }

    @Test
    fun `activeConfiguration returns initial configuration after popBackStack`() {
        backStack.push(Configuration.Additional)
        backStack.popBackStack()

        val configurations = backStack.activeConfigurations.collect()

        assertEquals(listOf(Configuration.Initial), configurations)
    }

    @Test
    fun `activeConfiguration correctly notifies about operations`() {
        val configurations = backStack.activeConfigurations.collect()

        backStack.push(Configuration.Additional)
        backStack.popBackStack()

        assertEquals(
            listOf(Configuration.Initial, Configuration.Additional, Configuration.Initial),
            configurations
        )
    }

    private fun <T> Source<T>.collect(): List<T> {
        val list = ArrayList<T>()
        observe { list += it }
        return list
    }

    sealed class Configuration : Parcelable {
        @Parcelize
        object Initial : Configuration()

        @Parcelize
        object Additional : Configuration()

        override fun toString(): String = javaClass.simpleName
    }

}
