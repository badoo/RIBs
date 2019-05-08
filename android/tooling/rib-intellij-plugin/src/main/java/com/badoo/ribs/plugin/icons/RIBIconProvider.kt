package com.badoo.ribs.plugin.icons

import com.badoo.ribs.plugin.util.getRibName
import com.intellij.ide.IconProvider
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import javax.swing.Icon

class RIBIconProvider: IconProvider() {

    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        if (element !is PsiDirectory) return null

        if (element.hasRib()) {
            return IconLoader.getIcon("icons/generate_rib.png")
        }

        return null
    }

    private fun PsiDirectory.hasRib(): Boolean {
        if (DumbService.isDumb(project)) return false

        return getRibName() != null
    }
}
