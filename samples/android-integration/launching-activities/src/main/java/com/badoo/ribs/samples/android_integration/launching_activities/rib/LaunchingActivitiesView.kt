package com.badoo.ribs.samples.android_integration.launching_activities.rib

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.minimal.reactive.Cancellable
import com.badoo.ribs.minimal.reactive.Relay
import com.badoo.ribs.minimal.reactive.Source
import com.badoo.ribs.samples.android_integration.launching_activities.R

interface LaunchingActivitiesView : RibView {

    interface Dependency {
        @StringRes
        fun getTitleResource(): Int
        @StringRes
        fun getDescriptionResource(): Int
    }

    interface Factory : ViewFactoryBuilder<Dependency, LaunchingActivitiesView>

    val events: Source<Event>

    fun setData(data: String)

    sealed class Event {
        data class LaunchActivityForResult(val data: String) : Event()
    }
}

class LaunchingActivitiesViewImpl constructor(
        override val androidView: ViewGroup,
        val dependency: LaunchingActivitiesView.Dependency
) : AndroidRibView(), LaunchingActivitiesView, Source<LaunchingActivitiesView.Event> {

    private val _events: Relay<LaunchingActivitiesView.Event> = Relay()
    override val events: Source<LaunchingActivitiesView.Event>
        get() = _events

    private val launchButton: Button = androidView.findViewById(R.id.launchActivityButton)
    private val dataReturned: TextView = androidView.findViewById(R.id.dataReturned)
    private val title: TextView = androidView.findViewById(R.id.title)
    private val description: TextView = androidView.findViewById(R.id.description)

    init {
        title.setText(dependency.getTitleResource())
        description.setText(dependency.getDescriptionResource())
        launchButton.setOnClickListener {
            _events.accept(
                    LaunchingActivitiesView.Event.LaunchActivityForResult(
                            "Launched from `${title.text}`")
            )
        }
    }

    override fun setData(data: String) {
        dataReturned.text = data
    }

    override fun observe(callback: (LaunchingActivitiesView.Event) -> Unit): Cancellable =
            events.observe(callback)

    private val childContainer: ViewGroup? = androidView.findViewById(R.id.childContainer)

    override fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup = childContainer
            ?: super.getParentViewForSubtree(subtreeOf)

    class Factory(
            @LayoutRes private val layoutRes: Int = R.layout.rib_launching_activities_root,
    ) : LaunchingActivitiesView.Factory {
        override fun invoke(dependency: LaunchingActivitiesView.Dependency): ViewFactory<LaunchingActivitiesView> =
                ViewFactory {
                    LaunchingActivitiesViewImpl(
                            androidView = it.inflate(layoutRes),
                            dependency = dependency
                    )
                }
    }
}
