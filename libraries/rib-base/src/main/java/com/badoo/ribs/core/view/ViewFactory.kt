package com.badoo.ribs.core.view

interface ViewFactory<Dependency, View : RibView> : (Dependency) -> NodeViewFactory<View>

