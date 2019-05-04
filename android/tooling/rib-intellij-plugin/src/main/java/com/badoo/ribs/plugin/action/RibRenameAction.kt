package com.badoo.ribs.plugin.action

import com.badoo.ribs.plugin.generator.dialog.RenameRibDialog
import com.badoo.ribs.plugin.icons.RIBIconProvider
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

class RibRenameAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val element = e.getData(CommonDataKeys.PSI_ELEMENT)
        val ribName = element?.getUserData(RIBIconProvider.RIB_NAME_KEY) ?: return

        RenameRibDialog(
            e.project!!,
            element,
            ribName
        ).show()
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        val element = e.getData(CommonDataKeys.PSI_ELEMENT)

        e.presentation.isEnabledAndVisible = element?.getUserData(RIBIconProvider.IS_RIB_KEY) == true
    }
}