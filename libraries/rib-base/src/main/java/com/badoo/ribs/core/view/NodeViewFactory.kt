package com.badoo.ribs.core.view

import androidx.lifecycle.Lifecycle

// TODO Make it "fun interface" in kotlin 1.4
interface NodeViewFactory<V : RibView> : (RibView, Lifecycle) -> V
