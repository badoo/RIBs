package com.badoo.ribs.test

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Rib
import com.badoo.ribs.test.activity.R

class RibTestActivity : RibActivity() {

    override val rootViewGroup: ViewGroup
        get() = findViewById(android.R.id.content)

    override fun createRib(savedInstanceState: Bundle?): Rib =
        ribFactory!!(this, savedInstanceState)

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    companion object {
        var ribFactory: ((RibTestActivity, Bundle?) -> Rib)? = null
    }
}
