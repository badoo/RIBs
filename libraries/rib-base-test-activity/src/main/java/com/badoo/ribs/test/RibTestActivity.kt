package com.badoo.ribs.test

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.StyleRes
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Rib
import com.badoo.ribs.test.activity.R

class RibTestActivity : RibActivity() {

    lateinit var rib: Rib
        private set

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.rib_test_root)

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME?.also { setTheme(it) }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rib_test)
    }

    override fun createRib(savedInstanceState: Bundle?): Rib =
        requireNotNull(RIB_FACTORY).invoke(this, savedInstanceState).also { rib = it }

    companion object {
        @StyleRes
        var THEME: Int? = null
        var RIB_FACTORY: ((RibTestActivity, Bundle?) -> Rib)? = null
    }
}
