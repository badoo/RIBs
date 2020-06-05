package com.badoo.ribs.plugin.rename

import com.badoo.ribs.plugin.generator.Replacements
import com.badoo.ribs.plugin.generator.SourceSet
import com.badoo.ribs.plugin.generator.SourceSetDirectoriesProvider
import com.badoo.ribs.plugin.util.addStringReplacement
import com.badoo.ribs.plugin.util.applyReplacements
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaRecursiveElementVisitor
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.PackageWrapper
import com.intellij.refactoring.rename.RenameDialog
import com.intellij.refactoring.rename.RenameProcessor
import com.intellij.ui.EditorTextField
import org.jetbrains.kotlin.idea.core.getPackage
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid
import java.awt.GridBagConstraints
import javax.swing.JPanel

class RenameRibDialog(
    project: Project,
    private val directory: PsiDirectory,
    private val sourceSetDirectoriesProvider: SourceSetDirectoriesProvider,
    private val packageWrapper: PackageWrapper,
    private val ribName: String
): RenameDialog(
    project,
    directory,
    null,
    null
) {
    init {
        (nameSuggestionsField.focusableComponent as EditorTextField).text = ribName
    }

    @Suppress("LongMethod")
    override fun createRenameProcessor(newName: String): RenameProcessor? {
        val replacements = Replacements().apply {
            addStringReplacement(ribName, newName)
        }

        val pkg = directory.getPackage() ?: return null

        val renameProcessor = RenameProcessor(
            project,
            pkg,
            pkg.name.orEmpty().applyReplacements(replacements),
            false,
            false
        )

        SourceSet.values().forEach {
            renameProcessor.addSourceSet(it, replacements)
        }
        packageWrapper.directories.subtract(renameProcessor.elements)
            .forEach{
                renameProcessor.checkForReplacements(it as PsiDirectory, replacements)
            }

        return renameProcessor
    }

    private fun RenameProcessor.addSourceSet(sourceSet: SourceSet, replacements: Replacements) {
        sourceSetDirectoriesProvider.getDirectory(sourceSet, createIfNotFound = false)
            ?.let {
                checkForReplacements(it, replacements)
            }
    }

    private fun RenameProcessor.checkForReplacements(directory: PsiDirectory, replacements: Replacements) {
        fun PsiElement.registerForRename(name: String) {
            if (replacements.fromArray.any { name.contains(it) }) {
                addElement(
                    this,
                    name.applyReplacements(replacements)
                )
            }
        }

        directory.registerForRename(directory.name)
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
        directory.acceptChildren(object : JavaRecursiveElementVisitor() {
            override fun visitClass(aClass: PsiClass) {
                super.visitClass(aClass)

                val name = aClass.name ?: return
                aClass.registerForRename(name)
            }
        })
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
