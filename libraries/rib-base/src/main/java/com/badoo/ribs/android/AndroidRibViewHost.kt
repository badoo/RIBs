package com.badoo.ribs.android

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.badoo.ribs.core.view.AndroidRibView

class AndroidRibViewHost(
    host: ViewGroup
) : AndroidRibView() {

    override val androidView: ViewGroup =
        host

    init {
        // Integration host does not need any lifecycle
        onCreate(LifecycleRegistry(this))
    }

    @SuppressLint("MissingSuperCall")
    override fun onCreate(lifecycle: Lifecycle) {
        // no-op
    }

}
