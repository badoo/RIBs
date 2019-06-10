package com.badoo.ribs.core.view

import android.view.ViewGroup

// TODO Replace ViewFactory with this
interface ViewFactory2<D, T>: (D) -> (ViewGroup) -> T

@Deprecated("Use DepsViewFactory")
interface ViewFactory<T>: (ViewGroup) -> T
