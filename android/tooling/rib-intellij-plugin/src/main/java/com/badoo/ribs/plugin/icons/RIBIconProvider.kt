package com.badoo.ribs.plugin.icons

import com.intellij.ide.IconProvider
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.Key
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtSimpleNameExpression
import org.jetbrains.kotlin.psi.KtVisitorVoid
import org.jetbrains.kotlin.utils.addToStdlib.firstNotNullResult
import javax.swing.Icon

class RIBIconProvider: IconProvider() {

    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        if (element !is PsiDirectory) return null

        if (element.hasRib()) {
            return IconLoader.getIcon("icons/generate_rib.png")
        }

        return null
    }

    private fun PsiElement.hasRib(): Boolean {
//        // Check the cache for result
//        getUserData(IS_RIB_KEY)?.let {
//            return it
//        }

        if (DumbService.isDumb(project)) return false

        val childFiles = children.filter { it !is PsiDirectory }
        val ribName = childFiles.firstNotNullResult {
            var ribName: String? = null
            it.acceptChildren(object : KtVisitorVoid() {
                /**
                 * Search for the interface with Rib interface as super
                 */
                override fun visitClass(klass: KtClass) {
                    super.visitClass(klass)
                    if (!klass.isInterface()) return

                    val isRib = klass.superTypeListEntries
                        .mapNotNull { it.typeAsUserType?.referenceExpression }
                        .any { it.isRib() }

                    ribName = if (isRib) klass.name else null
                }
            })
            ribName
        }

        CachedValuesManager.getManager(project)
            .createCachedValue { CachedValueProvider.Result(ribName == null, children) }

        // Cache the result
        putUserData(IS_RIB_KEY, ribName != null)
        putUserData(RIB_NAME_KEY, ribName)

        return ribName != null
    }

    fun KtSimpleNameExpression.isRib(): Boolean {
        val references = references.map { it.resolve() }
        return references.any { it is KtClass && it.fqName?.asString() == RIB_FQ_NAME }
    }


    companion object {
        private const val RIB_FQ_NAME = "com.badoo.ribs.core.Rib"

        val IS_RIB_KEY = Key<Boolean>("com.badoo.ribs.isRib")
        val RIB_NAME_KEY = Key<String>("com.badoo.ribs.ribName")
    }
}