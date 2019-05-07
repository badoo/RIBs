package com.badoo.ribs.plugin.action

import com.badoo.ribs.plugin.generator.SourceSetDirectoriesProvider
import com.badoo.ribs.plugin.rename.RenameRibDialog
import com.badoo.ribs.plugin.util.getRibName
import com.intellij.facet.FacetManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.psi.PsiDirectory
import com.intellij.refactoring.PackageWrapper
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.kotlin.idea.core.getPackage

class RibRenameAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        val element = e.getData(CommonDataKeys.PSI_ELEMENT) as PsiDirectory
        val module = ModuleUtilCore.findModuleForPsiElement(element)!!
        val facet = FacetManager.getInstance(module).getFacetByType(AndroidFacet.ID)!!
        val packageWrapper = PackageWrapper(element.getPackage())
        val ribName = element.getRibName()!!

        val sourceSetDirectoryProvider = SourceSetDirectoriesProvider(
            project,
            facet,
            packageWrapper,
            element
        )

        RenameRibDialog(
            project,
            element,
            sourceSetDirectoryProvider,
            packageWrapper,
            ribName
        ).show()
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        val element = e.getData(CommonDataKeys.PSI_ELEMENT)

        e.presentation.isEnabledAndVisible = element is PsiDirectory && element.getRibName() != null
    }
}