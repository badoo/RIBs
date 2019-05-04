package com.badoo.ribs.plugin.generator.dialog

import com.badoo.ribs.plugin.util.toSnakeCase
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.rename.RenameDialog
import com.intellij.refactoring.rename.RenameProcessor
import com.intellij.ui.EditorTextField
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid
import java.awt.GridBagConstraints
import javax.swing.JPanel

class RenameRibDialog(
    project: Project,
    psiElement: PsiElement,
    private val ribName: String
): RenameDialog(
    project,
    psiElement,
    null,
    null
) {
    init {
        (nameSuggestionsField.focusableComponent as EditorTextField).text = ribName
    }

    override fun createRenameProcessor(newName: String): RenameProcessor {
        val renameProcessor = RenameProcessor(
            project,
            psiElement,
            newName.toSnakeCase(),
            false,
            false
        )

        psiElement.acceptChildren(object : KtTreeVisitorVoid() {
            override fun visitFile(file: PsiFile) {
                super.visitFile(file)

                val name = file.name
                file.registerForRename(name)
            }

            override fun visitClassOrObject(classOrObject: KtClassOrObject) {
                super.visitClassOrObject(classOrObject)

                val name = classOrObject.name ?: return
                classOrObject.registerForRename(name)
            }

            fun PsiElement.registerForRename(name: String) {
                if (name.startsWith(ribName)) {
                    val suffix = name.substring(ribName.length)
                    renameProcessor.addElement(
                        this, newName + suffix
                    )
                }
            }
        })

        return renameProcessor
    }

    override fun getTitle(): String =
        "Rename RIB..."

    override fun getLabelText(): String =
        "Rename RIB to:"

    override fun createCheckboxes(panel: JPanel?, gbConstraints: GridBagConstraints?) {
        super.createCheckboxes(panel, gbConstraints)
        cbSearchInComments.isVisible = false
    }
}