package com.badoo.ribs.plugin.generator.dialog

import com.badoo.ribs.plugin.generator.Replacements
import com.badoo.ribs.plugin.generator.SourceSet
import com.badoo.ribs.plugin.generator.SourceSetDirectoriesProvider
import com.badoo.ribs.plugin.util.addStringReplacement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.rename.RenameDialog
import com.intellij.refactoring.rename.RenameProcessor
import com.intellij.ui.EditorTextField
import org.apache.commons.lang.StringUtils
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid
import java.awt.GridBagConstraints
import javax.swing.JPanel

class RenameRibDialog(
    project: Project,
    psiElement: PsiElement,
    private val sourceSetDirectoriesProvider: SourceSetDirectoriesProvider,
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
        val replacements = Replacements().apply {
            addStringReplacement(ribName, newName)
        }

        val renameProcessor = RenameProcessor(
            project,
            psiElement,
            StringUtils.replaceEach(newName, replacements.fromArray, replacements.toArray),
            false,
            false
        )

        SourceSet.values().forEach {
            fun PsiElement.registerForRename(name: String) {
                if (replacements.fromArray.any { name.contains(it) }) {
                    renameProcessor.addElement(
                        this,
                        StringUtils.replaceEach(name, replacements.fromArray, replacements.toArray)
                    )
                }
            }

            val directory = sourceSetDirectoriesProvider.getDirectory(it, createIfNotFound = false)
            directory.registerForRename(newName)

            directory.acceptChildren(object : KtTreeVisitorVoid() {
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
            })
        }



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