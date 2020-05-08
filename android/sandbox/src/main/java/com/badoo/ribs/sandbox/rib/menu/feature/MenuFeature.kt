package com.badoo.ribs.sandbox.rib.menu.feature

import com.badoo.mvicore.feature.MemoFeature
import com.badoo.ribs.sandbox.rib.menu.Menu
import com.badoo.ribs.sandbox.rib.menu.feature.MenuFeature.State

class MenuFeature : MemoFeature<State>(
    initialState = State()
) {
    data class State(
        val selected: Menu.MenuItem? = null
    )
}
