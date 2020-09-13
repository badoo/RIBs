package com.badoo.ribs.core.helper

import com.badoo.ribs.core.view.RibView

class TestViewFactory: (RibView) -> TestView {

    override fun invoke(p1: RibView): TestView =
        TestView()
}
