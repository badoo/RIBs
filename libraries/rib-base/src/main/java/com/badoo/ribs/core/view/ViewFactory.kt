package com.badoo.ribs.core.view

import android.view.ViewGroup

interface ViewFactory<Dependency, View>: (Dependency) -> (ViewGroup) -> View

