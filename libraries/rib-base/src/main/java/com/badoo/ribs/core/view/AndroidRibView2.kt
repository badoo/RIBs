package com.badoo.ribs.core.view

import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

@Suppress("LeakingThis", "DEPRECATION") // owners approach always leaks "this"
abstract class AndroidRibView2(
    final override val androidView: ViewGroup,
    private val lifecycle: Lifecycle,
) : AndroidRibView(), LifecycleOwner, SavedStateRegistryOwner {

    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    init {
        ViewTreeLifecycleOwner.set(androidView, this)
        androidView.setViewTreeSavedStateRegistryOwner(this)
        savedStateRegistryController.performAttach()
    }

    final override fun getLifecycle(): Lifecycle =
        lifecycle

    override fun saveInstanceState(bundle: Bundle) {
        super.saveInstanceState(bundle)
        savedStateRegistryController.performSave(bundle)
    }

    override fun restoreInstanceState(bundle: Bundle?) {
        super.restoreInstanceState(bundle)
        savedStateRegistryController.performRestore(bundle)
    }

}
