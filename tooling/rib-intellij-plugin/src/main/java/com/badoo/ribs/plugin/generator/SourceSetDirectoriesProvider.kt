package com.badoo.ribs.plugin.generator

import com.android.tools.idea.gradle.model.IdeArtifactName
import com.android.tools.idea.gradle.project.model.AndroidModuleModel
import com.badoo.ribs.plugin.generator.SourceSet.*
import com.badoo.ribs.plugin.util.toPsiDirectory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiDirectory
import com.intellij.refactoring.PackageWrapper
import com.intellij.refactoring.util.RefactoringUtil
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.android.facet.ResourceFolderManager

class SourceSetDirectoriesProvider(
    private val project: Project,
    private val androidFacet: AndroidFacet,
    private val targetPackage: PackageWrapper,
    private val mainSourceDirectory: PsiDirectory
) {

    private val androidModel = AndroidModuleModel.get(androidFacet)!!
    private val directoriesCache: MutableMap<SourceSet, PsiDirectory> = hashMapOf()

    fun getDirectory(sourceSet: SourceSet, createIfNotFound: Boolean = true): PsiDirectory? =
        directoriesCache[sourceSet] ?: findDirectory(sourceSet, createIfNotFound)?.also {
            directoriesCache[sourceSet] = it
        }

    private fun findDirectory(sourceSet: SourceSet, createIfNotFound: Boolean): PsiDirectory? = when (sourceSet) {
        MAIN -> mainSourceDirectory
        TEST -> getAndroidArtifactDirectory(IdeArtifactName.UNIT_TEST, createIfNotFound)
        ANDROID_TEST -> getAndroidArtifactDirectory(IdeArtifactName.ANDROID_TEST, createIfNotFound)
        RESOURCES -> ResourceFolderManager.getInstance(androidFacet).folders
            .firstOrNull { !androidModel.isGenerated(it) }
            ?.toPsiDirectory(project)
            ?: throw IllegalStateException("Resources directory not found")
    }

    private fun getAndroidArtifactDirectory(artifact: IdeArtifactName, createIfNotFound: Boolean): PsiDirectory? {
        return try {
            val file = androidModel.getTestSourceProviders(artifact).firstOrNull()?.javaDirectories?.firstOrNull()
                ?: throw IllegalStateException("Source set directory for $artifact not found")
            file.mkdirs()
            val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
            if (createIfNotFound) {
                RefactoringUtil.createPackageDirectoryInSourceRoot(targetPackage, virtualFile!!)
            } else {
                targetPackage.directories.firstOrNull { VfsUtil.isAncestor(virtualFile!!, it.virtualFile, false) }
            }
        } catch (t: ClassCastException) {
            null
        } catch (t: NoSuchMethodError) {
            null
        }
    }
}
