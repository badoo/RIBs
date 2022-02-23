package com.badoo.ribs.samples.android.launching_activities.rib.child_common

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.minimal.reactive.Cancellable
import com.badoo.ribs.minimal.reactive.Relay
import com.badoo.ribs.minimal.reactive.Source
import com.badoo.ribs.samples.android.launching_activities.R

interface LaunchingActivitiesChildView : RibView {

    interface Dependency {
        @get:StringRes
        val titleResource: Int

        @get:StringRes
        val descriptionResource: Int
    }

    interface Factory : ViewFactoryBuilder<Dependency, LaunchingActivitiesChildView>

    val events: Source<Event>

    fun setData(data: String)

    sealed class Event {
        data class LaunchActivityForResult(val data: String) : Event()
    }
}

class LaunchingActivitiesChildViewImpl constructor(
    override val androidView: ViewGroup,
    val dependency: LaunchingActivitiesChildView.Dependency
) : AndroidRibView(), LaunchingActivitiesChildView, Source<LaunchingActivitiesChildView.Event> {

    private val _events: Relay<LaunchingActivitiesChildView.Event> = Relay()
    override val events: Source<LaunchingActivitiesChildView.Event>
        get() = _events

    private val launchButton: Button = androidView.findViewById(R.id.launchActivityButton)
    private val dataReturned: TextView = androidView.findViewById(R.id.dataReturned)
    private val title: TextView = androidView.findViewById(R.id.title)
    private val description: TextView = androidView.findViewById(R.id.description)

    init {
        title.setText(dependency.titleResource)
        description.setText(dependency.descriptionResource)
        launchButton.setOnClickListener {
            _events.accept(
                LaunchingActivitiesChildView.Event.LaunchActivityForResult(
                    "Launched from `${title.text}`")
            )
        }
    }

    override fun setData(data: String) {
        dataReturned.text = data
    }

    override fun observe(callback: (LaunchingActivitiesChildView.Event) -> Unit): Cancellable =
        events.observe(callback)

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_launching_activities_child,
    ) : LaunchingActivitiesChildView.Factory {
        override fun invoke(dependency: LaunchingActivitiesChildView.Dependency): ViewFactory<LaunchingActivitiesChildView> =
            ViewFactory {
                LaunchingActivitiesChildViewImpl(
                    androidView = it.inflate(layoutRes),
                    dependency = dependency
                )
            }
    }
}
