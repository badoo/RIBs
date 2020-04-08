package com.badoo.ribs.tutorials.tutorial2.rib.hello_world

import android.os.Bundle
import com.badoo.ribs.core.Interactor

class HelloWorldInteractor(
    savedInstanceState: Bundle?
) : Interactor<HelloWorldView>(
    savedInstanceState = savedInstanceState,
    disposables = null
)
