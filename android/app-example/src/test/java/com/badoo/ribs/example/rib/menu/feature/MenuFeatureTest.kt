package com.badoo.ribs.example.rib.menu.feature

import com.badoo.ribs.example.rib.menu.Menu
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Unit tests for a feature that should check core business logic of a RIB
 * These tests are the fastest by execution time and they show
 * the most specific failure reason if test fails
 */
class MenuFeatureTest {

    @Test
    fun `no wishes - no selected item in the state`() {
        val feature = MenuFeature()

        assertThat(feature.state.selected).isNull()
    }

    @Test
    fun `select menu item wish - updates state with the selected item`() {
        val feature = MenuFeature()

        feature.accept(Menu.Input.SelectMenuItem(Menu.MenuItem.HelloWorld))

        assertThat(feature.state.selected).isEqualTo(Menu.MenuItem.HelloWorld)
    }
}
