package com.badoo.ribs.samples.buildtime.parent

import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.buildtime.R

typealias ProfileIdFunction = ((Int) -> Unit)

interface BuildTimeDepsParentView : RibView {

    interface Factory : ViewFactory<Nothing?, BuildTimeDepsParentView>

    fun setBuildChildListener(profileIdFunc: ProfileIdFunction?)
}

class BuildTimeDepsParentViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    BuildTimeDepsParentView {

    private val profileIdSpinner: Spinner = androidView.findViewById(R.id.parent_profile_id_spinner)
    private val buildButton: Button = androidView.findViewById(R.id.parent_profile_build_button)
    private val childContainer: ViewGroup = androidView.findViewById(R.id.parent_child_container)

    init {
        val profileContentList: List<ProfileContent> = (1..PROFILE_ID_MAX).map { profileId ->
            ProfileContent(id = profileId, label = "Profile $profileId")
        }

        val adapter = ArrayAdapter(
            context, android.R.layout.simple_spinner_item, profileContentList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        profileIdSpinner.adapter = adapter
    }

    override fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup = childContainer

    override fun setBuildChildListener(profileIdFunc: ProfileIdFunction?) {
        if (profileIdFunc != null) {
            buildButton.setOnClickListener {
                profileIdFunc((profileIdSpinner.selectedItem as ProfileContent).id)
            }
        } else {
            buildButton.setOnClickListener(null)
        }
    }

    class ProfileContent(val id: Int, private val label: String) {
        override fun toString(): String = label
    }

    private companion object {
        private const val PROFILE_ID_MAX = 1000
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
