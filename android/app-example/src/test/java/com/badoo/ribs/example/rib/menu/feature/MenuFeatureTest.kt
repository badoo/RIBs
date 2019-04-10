package com.badoo.ribs.example.rib.menu.feature

import com.badoo.ribs.example.rib.menu.Menu
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

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
