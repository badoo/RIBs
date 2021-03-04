package com.badoo.ribs.samples.buildtime.rib.build_time_deps

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
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.buildtime.R

interface BuildTimeDepsView : RibView {

    interface Factory : ViewFactoryBuilder<Dependency, BuildTimeDepsView>

    interface Dependency {
        val presenter: BuildTimeDepsPresenter
    }
}

class BuildTimeDepsViewImpl private constructor(
    override val androidView: ViewGroup,
    private val presenter: BuildTimeDepsPresenter
) : AndroidRibView(),
    BuildTimeDepsView {

    private val profileIdSpinner: Spinner = androidView.findViewById(R.id.parent_profile_id_spinner)
    private val buildButton: Button = androidView.findViewById(R.id.parent_profile_build_button)
    private val childContainer: ViewGroup = androidView.findViewById(R.id.parent_child_container)

    init {
        val profileContentList: List<ProfileContent> = (1..PROFILE_ID_MAX).map { profileId ->
            ProfileContent(id = profileId, label = "Profile $profileId")
        }

        val adapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_item, profileContentList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        profileIdSpinner.adapter = adapter

        buildButton.setOnClickListener {
            presenter.onBuildChildClicked((profileIdSpinner.selectedItem as ProfileContent).id)
        }
    }

    override fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup =
        childContainer

    class ProfileContent(val id: Int, private val label: String) {
        override fun toString(): String = label
    }

    private companion object {
        private const val PROFILE_ID_MAX = 10
    }

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_parent
    ) : BuildTimeDepsView.Factory {
        override fun invoke(deps: BuildTimeDepsView.Dependency): ViewFactory<BuildTimeDepsView> =
            ViewFactory {
                BuildTimeDepsViewImpl(
                    androidView = it.inflate(layoutRes),
                    presenter = deps.presenter
                )
            }
    }
}
