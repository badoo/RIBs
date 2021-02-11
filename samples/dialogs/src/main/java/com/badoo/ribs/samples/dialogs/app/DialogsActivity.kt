package com.badoo.ribs.samples.dialogs.app

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.android.dialog.AlertDialogLauncher
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.samples.dialogs.R
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsExample
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsBuilder

class DialogsActivity : RibActivity() {

    private val deps = object : DialogsExample.Dependency {
        override val dialogLauncher: DialogLauncher = AlertDialogLauncher(
            this@DialogsActivity,
            this@DialogsActivity.lifecycle
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_dialogs)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.dialogs_activity_root)

    override fun createRib(savedInstanceState: Bundle?): Rib =
        DialogsBuilder(deps).build(BuildContext.root(savedInstanceState))
}

