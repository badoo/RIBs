package com.badoo.ribs.core.helper

import android.view.ViewGroup

class TestviewFactory: (RibView) -> TestView {
    override fun invoke(p1: ViewGroup): TestView =
        TestView()
}
