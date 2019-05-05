package com.badoo.ribs.plugin.util

import com.badoo.ribs.plugin.index.RIBIndexer
import com.intellij.psi.PsiDirectory
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex

fun PsiDirectory.topLevelScope() =
    GlobalSearchScope.filesScope(project, files.map { it.virtualFile }.toList())

fun PsiDirectory.getRibName(): String? {
    var ribName: String? = null
    FileBasedIndex.getInstance()
        .processValues(
            RIBIndexer.INDEX_ID,
            RIBIndexer.RIB_NAME_KEY,
            null,
            { _, string ->
                ribName = string
                true
            },
            topLevelScope()
        )
    return ribName
}