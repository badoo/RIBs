package com.badoo.ribs.example.rib.dialog_example.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.example.rib.dialog_example.DialogExample

class DialogExampleBuilder(
    private val dependency: DialogExample.Dependency
) : SimpleBuilder<DialogExample>() {

    override fun build(buildParams: BuildParams<Nothing?>): DialogExample =
        DaggerDialogExampleComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = buildParams.getOrDefault(DialogExample.Customisation()),
                buildParams = buildParams
            )
            .node()
}
