package com.badoo.ribs.core.view

import android.view.ViewGroup

interface ViewFactory<D, T>: (D) -> (ViewGroup) -> T

