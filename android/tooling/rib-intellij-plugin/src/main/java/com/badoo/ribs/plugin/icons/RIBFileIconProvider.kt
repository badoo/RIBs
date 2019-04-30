package com.badoo.ribs.plugin.icons

import com.intellij.ide.IconProvider
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.Key
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtVisitorVoid
import org.jetbrains.kotlin.psi.psiUtil.getSuperNames
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameOrNull
import org.jetbrains.kotlin.resolve.descriptorUtil.getSuperInterfaces
import javax.swing.Icon

class RIBFileIconProvider: IconProvider() {
    val logger = Logger.getInstance(javaClass)

    val ribKey = Key<Boolean>("com.badoo.rib")

    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        if (element !is PsiDirectory) return null

        if (element.hasRib()) {
            return IconLoader.getIcon("icons/generate_rib.png")
        }

        return null
    }

    private fun PsiElement.hasRib(): Boolean {
        getUserData(ribKey)?.let {
            return it
        }

        val childFiles = children.filter { it !is PsiDirectory }
        val start = System.currentTimeMillis()
        val hasRib = childFiles.any {
            var isRib = false
            it.acceptChildren(object : KtVisitorVoid() {
                override fun visitClass(klass: KtClass) {
                    super.visitClass(klass)
                    if (!klass.isInterface()) return

                    isRib = klass.getSuperNames().any { it == "Rib" }
                }
            })
            isRib
        }
        logger.info("Finished analyzis for $this in ${System.currentTimeMillis() - start} millis")

        putUserData(ribKey, hasRib)

        return hasRib
    }

    private fun ClassDescriptor?.isRib() =
        this?.getSuperInterfaces()
            ?.any {
                it.fqNameOrNull()?.asString() == "com.badoo.ribs.core.Rib"
            }
}