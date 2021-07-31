package com.badoo.ribs.samples.android.launching_activities.rib.launching_activities_example

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.android.launching_activities.R
import com.badoo.ribs.samples.android.launching_activities.rib.child1.LaunchingActivitiesChild1
import com.badoo.ribs.samples.android.launching_activities.rib.child2.LaunchingActivitiesChild2

interface LaunchingActivitiesExampleView : RibView {

    interface Dependency {
        @StringRes
        fun getTitleResource(): Int

        @StringRes
        fun getDescriptionResource(): Int
    }

    interface Factory : ViewFactoryBuilder<Dependency, LaunchingActivitiesExampleView>
}

class LaunchingActivitiesExampleViewImpl constructor(
    override val androidView: ViewGroup,
    val dependency: LaunchingActivitiesExampleView.Dependency
) : AndroidRibView(), LaunchingActivitiesExampleView {

    private val title: TextView = androidView.findViewById(R.id.title)
    private val description: TextView = androidView.findViewById(R.id.description)

    init {
        title.setText(dependency.getTitleResource())
        description.setText(dependency.getDescriptionResource())
    }

    private val child1Container: ViewGroup = androidView.findViewById(R.id.child1Container)
    private val child2Container: ViewGroup = androidView.findViewById(R.id.child2Container)
    override fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup =
        when (subtreeOf) {
            is LaunchingActivitiesChild1 -> child1Container
            is LaunchingActivitiesChild2 -> child2Container
            else -> super.getParentViewForSubtree(subtreeOf)
        }

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_launching_activities_root,
    ) : LaunchingActivitiesExampleView.Factory {
        override fun invoke(dependency: LaunchingActivitiesExampleView.Dependency): ViewFactory<LaunchingActivitiesExampleView> =
            ViewFactory {
                LaunchingActivitiesExampleViewImpl(
                    androidView = it.inflate(layoutRes),
                    dependency = dependency
                )
            }
    }
}
