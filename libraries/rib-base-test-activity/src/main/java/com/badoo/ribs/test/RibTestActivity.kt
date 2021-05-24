package com.badoo.ribs.test

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.StyleRes
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Rib

class RibTestActivity : RibActivity() {

    lateinit var rib: Rib
        private set

    override val rootViewGroup: ViewGroup
        get() = findViewById(android.R.id.content)

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME?.also { setTheme(it) }
        super.onCreate(savedInstanceState)
    }

    override fun createRib(savedInstanceState: Bundle?): Rib =
        requireNotNull(RIB_FACTORY).invoke(this, savedInstanceState).also { rib = it }

    companion object {
        @StyleRes
        var THEME: Int? = null
        var RIB_FACTORY: ((RibTestActivity, Bundle?) -> Rib)? = null
    }
}
