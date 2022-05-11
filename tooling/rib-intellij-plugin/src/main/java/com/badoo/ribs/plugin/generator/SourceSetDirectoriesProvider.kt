package com.badoo.ribs.plugin.generator

import com.android.tools.idea.gradle.model.IdeArtifactName
import com.android.tools.idea.gradle.project.model.GradleAndroidModel
import com.intellij.ide.util.DirectoryUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiDirectory
import com.intellij.refactoring.PackageWrapper
import com.intellij.refactoring.util.RefactoringUtil
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.kotlin.idea.core.util.toPsiDirectory
import java.io.File

class SourceSetDirectoriesProvider(
    private val project: Project,
    private val androidFacet: AndroidFacet,
    private val targetPackage: PackageWrapper,
    private val mainSourceDirectory: PsiDirectory
) {

    private val gradleAndroidModel = GradleAndroidModel.get(androidFacet)!!
    private val directoriesCache: MutableMap<SourceSet, PsiDirectory> = hashMapOf()

    fun getDirectory(sourceSet: SourceSet, createIfNotFound: Boolean = true): PsiDirectory? =
        directoriesCache[sourceSet] ?: findDirectory(sourceSet, createIfNotFound)?.also {
            directoriesCache[sourceSet] = it
        }

    private fun findDirectory(sourceSet: SourceSet, createIfNotFound: Boolean): PsiDirectory? =
        when (sourceSet) {
            SourceSet.MAIN -> mainSourceDirectory
            SourceSet.TEST -> getAndroidArtifactDirectory(
                artifact = IdeArtifactName.UNIT_TEST,
                createIfNotFound = createIfNotFound
            )
            SourceSet.ANDROID_TEST -> getAndroidArtifactDirectory(
                artifact = IdeArtifactName.ANDROID_TEST,
                createIfNotFound = createIfNotFound
            )
            SourceSet.RESOURCES -> getResourcesArtifactDirectory(
                createIfNotFound = createIfNotFound
            )
        }

    private fun getAndroidArtifactDirectory(
        artifact: IdeArtifactName,
        createIfNotFound: Boolean
    ): PsiDirectory? {
        return try {
            val file = gradleAndroidModel.getTestSourceProviders(artifact).firstOrNull()?.javaDirectories?.firstOrNull()
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

    private fun getResourcesArtifactDirectory(createIfNotFound: Boolean): PsiDirectory =
        gradleAndroidModel.allSourceProviders[0].resDirectories
            .firstOrNull()
            ?.also {
                if (!it.exists() && createIfNotFound) {
                    DirectoryUtil.createSubdirectories(
                        it.name,
                        it.parentFile.toPsiDirectory(project),
                        File.separator
                    )
                }
            }
            ?.toPsiDirectory(project)
            ?: throw IllegalStateException("Resources directory not found")
}
