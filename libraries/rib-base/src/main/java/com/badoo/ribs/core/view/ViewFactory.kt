package com.badoo.ribs.core.view

interface ViewFactory<Dependency, View>: (Dependency) -> (RibView) -> View

