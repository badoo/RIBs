package com.badoo.ribs.sandbox.rib.dialog_example.builder

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExample

class DialogExampleBuilder(
    private val dependency: DialogExample.Dependency
) : SimpleBuilder<DialogExample>() {

    override fun build(buildParams: BuildParams<Nothing?>): DialogExample = TODO()
//        DaggerDialogExampleComponent
//            .factory()
//            .create(
//                dependency = dependency,
//                customisation = buildParams.getOrDefault(DialogExample.Customisation()),
//                buildParams = buildParams
//            )
//            .node()
}
