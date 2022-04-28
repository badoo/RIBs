package com.badoo.ribs.core.helper

import android.view.ViewGroup
import com.badoo.ribs.core.view.AndroidRibView
import org.mockito.kotlin.mock

class TestView : AndroidRibView() {

    override val androidView: ViewGroup = mock()
}
