package com.badoo.ribs.core.plugin.utils.debug

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.badoo.ribs.debugutils.R
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.debug.TreePrinter

class DebugControlsHost(
    viewGroupForChildren: (() -> ViewGroup),
    growthDirection: GrowthDirection = GrowthDirection.TOP,
    includeDefaultDebugTools: Boolean = true,
    private val defaultTreePrinterFormat: (Node<*>) -> String = TreePrinter.FORMAT_SIMPLE_INSTANCE
) : AbstractDebugControls<Rib>(
    viewFactory = if (includeDefaultDebugTools) defaultDebugTools else null,
    viewGroupForChildren = viewGroupForChildren,
    growthDirection = growthDirection
) {
    companion object {
        private val defaultDebugTools: ((ViewGroup) -> View) = { it.inflate(R.layout.debug_root) }
    }

    override val label: String = "Debug root"

    override fun onDebugViewCreated(debugView: View) {
        super.onDebugViewCreated(debugView)
        debugView.findViewById<Button>(R.id.debug_tree).setOnClickListener {
            TreePrinter.printNodeSubtree(node, defaultTreePrinterFormat)
            Toast.makeText(debugView.context, "Check logs", Toast.LENGTH_SHORT).show()
        }
    }
}
