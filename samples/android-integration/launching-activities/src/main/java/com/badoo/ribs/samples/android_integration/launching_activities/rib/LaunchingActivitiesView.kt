package com.badoo.ribs.samples.android_integration.launching_activities.rib

import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.LayoutRes
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

    interface Factory : ViewFactoryBuilder<Nothing?, LaunchingActivitiesView>

    val events: Source<Event>

    fun setData(data: String)

    sealed class Event {
        data class LaunchActivityForResult(val data: String) : Event()
    }
}

class LaunchingActivitiesViewImpl private constructor(
        override val androidView: ViewGroup
) : AndroidRibView(), LaunchingActivitiesView, Source<LaunchingActivitiesView.Event> {
    private val _events: Relay<LaunchingActivitiesView.Event> = Relay()
    override val events: Source<LaunchingActivitiesView.Event>
        get() = _events

    private val launchButton: Button = androidView.findViewById(R.id.launchActivityButton)
    private val dataReturned: TextView = androidView.findViewById(R.id.dataReturned)
    private val dataToSend: EditText = androidView.findViewById(R.id.dataToSend)

    init {
        launchButton.setOnClickListener {
            _events.accept(
                    LaunchingActivitiesView.Event.LaunchActivityForResult(
                            dataToSend.text.toString())
            )
        }
    }

    override fun setData(data: String) {
        dataReturned.text = data
    }

    override fun observe(callback: (LaunchingActivitiesView.Event) -> Unit): Cancellable =
            events.observe(callback)

    class Factory(
            @LayoutRes private val layoutRes: Int = R.layout.rib_launching_activities
    ) : LaunchingActivitiesView.Factory {
        override fun invoke(nothing: Nothing?): ViewFactory<LaunchingActivitiesView> =
                ViewFactory {
                    LaunchingActivitiesViewImpl(
                            androidView = it.inflate(layoutRes)
                    )
                }
    }
}
