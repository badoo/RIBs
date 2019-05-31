package com.badoo.ribs.example.rib.menu.feature

import com.badoo.mvicore.feature.MemoFeature
import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.menu.feature.MenuFeature.State

class MenuFeature : MemoFeature<State>(
    initialState = State()
) {
    data class State(
        val selected: Menu.MenuItem? = null
    )
}
