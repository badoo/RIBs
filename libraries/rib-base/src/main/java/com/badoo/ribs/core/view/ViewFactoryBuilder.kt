package com.badoo.ribs.core.view

fun interface ViewFactoryBuilder<Dependency, View> : (Dependency) -> ViewFactory<View>
