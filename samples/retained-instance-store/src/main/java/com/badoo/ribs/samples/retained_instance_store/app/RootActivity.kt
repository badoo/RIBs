package com.badoo.ribs.samples.retained_instance_store.app

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.samples.retained_instance_store.R
import com.badoo.ribs.samples.retained_instance_store.rib.retainedInstance.RetainedInstance
import com.badoo.ribs.samples.retained_instance_store.rib.retainedInstance.RetainedInstanceBuilder

class RootActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Rib {
        val dependency = object : RetainedInstance.Dependency {}
        return RetainedInstanceBuilder(dependency).build(BuildContext.root(savedInstanceState))
    }
}
