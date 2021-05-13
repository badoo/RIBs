package com.badoo.ribs.core.helper

import android.view.ViewGroup
import com.badoo.ribs.core.view.AndroidRibView
import com.nhaarman.mockitokotlin2.mock

class TestView : AndroidRibView() {

    override val androidView: ViewGroup = mock()
}
