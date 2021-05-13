package com.badoo.ribs.plugin.generator

import com.android.builder.model.AndroidProject
import com.android.tools.idea.gradle.project.model.AndroidModuleModel
import com.badoo.ribs.plugin.generator.SourceSet.ANDROID_TEST
import com.badoo.ribs.plugin.generator.SourceSet.MAIN
import com.badoo.ribs.plugin.generator.SourceSet.RESOURCES
import com.badoo.ribs.plugin.generator.SourceSet.TEST
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
        TEST -> getAndroidArtifactDirectory(AndroidProject.ARTIFACT_UNIT_TEST, createIfNotFound)
        ANDROID_TEST -> getAndroidArtifactDirectory(AndroidProject.ARTIFACT_ANDROID_TEST, createIfNotFound)
        RESOURCES -> ResourceFolderManager.getInstance(androidFacet).folders
            .firstOrNull { !androidModel.isGenerated(it) }
            ?.toPsiDirectory(project)
            ?: throw IllegalStateException("Resources directory not found")
    }

    private fun getAndroidArtifactDirectory(artifact: String, createIfNotFound: Boolean): PsiDirectory? {
        val file = androidModel.getTestSourceProviders(artifact).firstOrNull()?.javaDirectories?.firstOrNull()
            ?: throw IllegalStateException("Source set directory for $artifact not found")
        file.mkdirs()
        val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
        return if (createIfNotFound) {
            RefactoringUtil.createPackageDirectoryInSourceRoot(targetPackage, virtualFile!!)
        } else {
            targetPackage.directories.firstOrNull { VfsUtil.isAncestor(virtualFile!!, it.virtualFile, false) }
        }
    }
}
