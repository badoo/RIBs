package com.badoo.ribs.core.helper

import com.badoo.ribs.core.view.ViewFactory

class TestViewFactory : ViewFactory<TestView> {

    override fun invoke(p1: ViewFactory.Context): TestView =
        TestView()
}
