package com.badoo.ribs.samples.buildtime.parent

import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.buildtime.R

typealias ProfileIdFunction = ((Int?) -> Unit)

interface BuildTimeDepsParentView : RibView {

    interface Factory : ViewFactory<Nothing?, BuildTimeDepsParentView>

    fun setBuildChildListener(profileIdFunc: ProfileIdFunction?)
}

class BuildTimeDepsParentViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    BuildTimeDepsParentView {

    private val profileIdEditText: EditText = androidView.findViewById(R.id.parent_profile_id_entry)
    private val buildButton: Button = androidView.findViewById(R.id.parent_profile_build_button)
    private val childContainer: ViewGroup = androidView.findViewById(R.id.parent_child_container)

    override fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup = childContainer

    override fun setBuildChildListener(profileIdFunc: ProfileIdFunction?) {
        if (profileIdFunc != null) {
            buildButton.setOnClickListener {
                profileIdFunc(profileIdEditText.text.toString().toIntOrNull())
            }
        } else {
            buildButton.setOnClickListener(null)
        }
    }

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_parent
    ) : BuildTimeDepsParentView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> BuildTimeDepsParentView = {
            BuildTimeDepsParentViewImpl(
                it.inflate(layoutRes)
            )
        }
    }
}
