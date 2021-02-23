package com.badoo.ribs.samples.transitionanimations.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.transitionanimations.R
import com.badoo.ribs.samples.transitionanimations.rib.parent.Parent
import com.badoo.ribs.samples.transitionanimations.rib.parent.ParentBuilder

class RootActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Rib {
        val dependency = object : Parent.Dependency {}
        return ParentBuilder(dependency = dependency).build(buildContext = root(savedInstanceState))
    }

}