package com.badoo.ribs.core

interface LifecycleParent {
    fun addParented(child: LifecycleChild)
    fun removeParented(child: LifecycleChild)
}

interface LifecycleChild {
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
}
