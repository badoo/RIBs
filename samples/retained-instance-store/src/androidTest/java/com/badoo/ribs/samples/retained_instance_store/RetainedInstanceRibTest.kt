package com.badoo.ribs.samples.retained_instance_store

import android.app.Activity
import android.os.Bundle
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.samples.retained_instance_store.rib.RetainedInstanceRib
import com.badoo.ribs.samples.retained_instance_store.rib.builder.RetainedInstanceBuilder

import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RetainedInstanceRibTest {
  /*  @get:Rule
    val ribsRule = RibsRule { _, savedInstanceState -> buildRib(savedInstanceState) }

    private fun buildRib(savedInstanceState: Bundle?) =
            RetainedInstanceBuilder(object : RetainedInstanceRib.Dependency {
                override val orientationController: Activity
                    get() = TODO("Wrap activity")
            }).build(BuildContext.root(savedInstanceState))*/
}