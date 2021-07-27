package com.badoo.ribs.samples.gallery

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.gallery.rib.root_container.RootContainer
import com.badoo.ribs.samples.gallery.rib.root_container.RootContainerBuilder

class RootActivity : RibActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Rib =
        RootContainerBuilder(
            object : RootContainer.Dependency {

            }
        ).build(root(savedInstanceState))
}
