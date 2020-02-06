package com.badoo.ribs.plugin.index

import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.DefaultFileTypeSpecificInputFilter
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileBasedIndexExtension
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.io.DataExternalizer
import com.intellij.util.io.EnumeratorStringDescriptor
import com.intellij.util.io.KeyDescriptor
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.idea.core.util.readNullable
import org.jetbrains.kotlin.idea.core.util.writeNullable
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtVisitorVoid
import org.jetbrains.kotlin.psi.psiUtil.getSuperNames
import java.io.DataInput
import java.io.DataOutput

class RibIndexer : FileBasedIndexExtension<String, String?>() {
    override fun getValueExternalizer(): DataExternalizer<String?> =
        object : DataExternalizer<String?> {
            override fun save(out: DataOutput, value: String?) {
                out.writeNullable(value) {
                    writeUTF(value)
                }
            }

            override fun read(input: DataInput): String? {
                return input.readNullable {
                    readUTF()
                }
            }
        }

    override fun getIndexer(): DataIndexer<String, String?, FileContent> =
        DataIndexer { content ->
            val file = content.psiFile
            var ribName: String? = null
            file.acceptChildren(object : KtVisitorVoid() {
                /**
                 * Search for the interface with Rib interface as super
                 */
                override fun visitClass(klass: KtClass) {
                    super.visitClass(klass)
                    if (!klass.isInterface()) return

                    val isRib = klass.getSuperNames().contains(RIB_SIMPLE_NAME)
                    val hasImport = klass.containingKtFile
                        .importDirectives
                        .any { it.importPath?.fqName?.asString() == RIB_CANONICAL_NAME }

                    ribName = if (isRib && hasImport) klass.name else null
                }
            })

            mutableMapOf<String, String?>().apply {
                ribName?.let {
                    put(RIB_NAME_KEY, it)
                }
            }
        }

    override fun getName(): ID<String, String?> = INDEX_ID

    /**
     * Don't forget to increment if you changing something here, otherwise the values will not be updated
     */
    override fun getVersion(): Int = 0

    override fun dependsOnFileContent(): Boolean = true

    override fun getInputFilter(): FileBasedIndex.InputFilter =
        DefaultFileTypeSpecificInputFilter(KotlinFileType.INSTANCE)

    override fun getKeyDescriptor(): KeyDescriptor<String> =
        EnumeratorStringDescriptor()

    companion object {
        val INDEX_ID = ID.create<String, String?>(RibIndexer::class.java.canonicalName)

        private const val RIB_CANONICAL_NAME = "com.badoo.ribs.core.Rib"
        private val RIB_SIMPLE_NAME = RIB_CANONICAL_NAME.takeLastWhile { it != '.' }

        const val RIB_NAME_KEY = "com.badoo.ribs.index.ribName"
    }
}
