package com.badoo.ribs.android.store

import androidx.lifecycle.ViewModel

internal class RetainedInstanceStoreViewModel : ViewModel() {

    var isCleared: Boolean = false
        private set

    override fun onCleared() {
        isCleared = true
    }

}