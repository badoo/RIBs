package com.badoo.ribs.sandbox.rib.hello_world

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.android.ActivityStarter.ActivityResultEvent
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.sandbox.app.OtherActivity
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorldRouter.Configuration
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorldRouter.Configuration.Content
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorldRouter.Configuration.Permanent
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorldView.ViewModel
import com.badoo.ribs.sandbox.rib.hello_world.analytics.HelloWorldAnalytics
import com.badoo.ribs.sandbox.rib.hello_world.feature.HelloWorldFeature
import com.badoo.ribs.sandbox.rib.hello_world.mapper.InputToWish
import com.badoo.ribs.sandbox.rib.hello_world.mapper.NewsToOutput
import com.badoo.ribs.sandbox.rib.hello_world.mapper.ViewEventToAnalyticsEvent
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class HelloWorldInteractor(
    buildParams: BuildParams<Nothing?>,
    private val router: Router<Configuration, Permanent, Content, Nothing, HelloWorldView>,
    private val input: ObservableSource<HelloWorld.Input>,
    private val output: Consumer<HelloWorld.Output>,
    private val feature: HelloWorldFeature,
    private val activityStarter: ActivityStarter
) : Interactor<HelloWorldView>(
    buildParams = buildParams,
    disposables = feature
) {
    companion object {
        private const val REQUEST_CODE_OTHER_ACTIVITY = 1
    }

    private val dummyViewInput = BehaviorRelay.createDefault(
        ViewModel("My id: " + id.replace("${HelloWorldInteractor::class.java.name}.", ""))
    )

    override fun onAttach(ribLifecycle: Lifecycle) {
        ribLifecycle.createDestroy {
            bind(feature.news to output using NewsToOutput)
            bind(input to feature using InputToWish)
        }
    }

    override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to HelloWorldAnalytics using ViewEventToAnalyticsEvent)
            bind(view to viewEventConsumer)
            bind(activityStarter.events(this@HelloWorldInteractor) to activityResultConsumer)
            bind(dummyViewInput to view)
        }
    }

    private val viewEventConsumer : Consumer<HelloWorldView.Event> = Consumer {
        when (it) {
            HelloWorldView.Event.ButtonClicked -> launchOtherActivityForResult()
        }
    }

    private fun launchOtherActivityForResult() {
        activityStarter.startActivityForResult(this, REQUEST_CODE_OTHER_ACTIVITY) {
            Intent(this, OtherActivity::class.java)
                .putExtra(OtherActivity.KEY_INCOMING, "Data sent by HelloWorld - 123123")
        }
    }

    private val activityResultConsumer : Consumer<ActivityResultEvent> = Consumer {
        if (it.requestCode == REQUEST_CODE_OTHER_ACTIVITY) {
            if (it.resultCode == Activity.RESULT_OK) {
                dummyViewInput.accept(
                    ViewModel(
                        "Data returned: " + it.data?.getIntExtra(OtherActivity.KEY_RETURNED_DATA, -1)?.toString()
                    )
                )
            }
        }
    }
}
