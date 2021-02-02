package com.badoo.ribs.samples.back_stack.app

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.back_stack.R
import com.badoo.ribs.samples.back_stack.rib.parent.Parent
import com.badoo.ribs.samples.back_stack.rib.parent.ParentBuilder

class RootActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Rib {
        val dependency = object : Parent.Dependency {}
        return ParentBuilder(dependency).build(root(savedInstanceState))
    }

}
