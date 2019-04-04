package com.badoo.ribs.example.rib.blocker.mapper

import com.badoo.ribs.example.rib.blocker.Blocker
import com.badoo.ribs.example.rib.blocker.BlockerView

internal object ViewEventToOutput : (BlockerView.Event) -> Blocker.Output? {

    override fun invoke(event: BlockerView.Event): Blocker.Output? = when (event) {
        is BlockerView.Event.ButtonClicked -> Blocker.Output.SomeEvent
    }

}
