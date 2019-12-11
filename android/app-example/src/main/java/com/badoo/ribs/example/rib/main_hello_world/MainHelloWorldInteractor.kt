package com.badoo.ribs.example.rib.main_hello_world

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.android.ActivityStarter.ActivityResultEvent
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.app.OtherActivity
import com.badoo.ribs.example.rib.main_hello_world.MainHelloWorldRouter.Configuration
import com.badoo.ribs.example.rib.main_hello_world.MainHelloWorldRouter.Configuration.Content
import com.badoo.ribs.example.rib.main_hello_world.MainHelloWorldRouter.Configuration.Permanent
import com.badoo.ribs.example.rib.main_hello_world.MainHelloWorldView.ViewModel
import com.badoo.ribs.example.rib.main_hello_world.analytics.MainHelloWorldAnalytics
import com.badoo.ribs.example.rib.main_hello_world.feature.MainHelloWorldFeature
import com.badoo.ribs.example.rib.main_hello_world.mapper.InputToWish
import com.badoo.ribs.example.rib.main_hello_world.mapper.NewsToOutput
import com.badoo.ribs.example.rib.main_hello_world.mapper.ViewEventToAnalyticsEvent
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class MainHelloWorldInteractor(
    savedInstanceState: Bundle?,
    router: Router<Configuration, Permanent, Content, Nothing, MainHelloWorldView>,
    private val input: ObservableSource<MainHelloWorld.Input>,
    private val output: Consumer<MainHelloWorld.Output>,
    private val feature: MainHelloWorldFeature,
    private val activityStarter: ActivityStarter
) : Interactor<Configuration, Content, Nothing, MainHelloWorldView>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = feature
) {
    companion object {
        private const val REQUEST_CODE_OTHER_ACTIVITY = 1
    }

    private val dummyViewInput = BehaviorRelay.createDefault(
        ViewModel("My id: " + id.replace("${MainHelloWorldInteractor::class.java.name}.", ""))
    )

    override fun onAttach(ribLifecycle: Lifecycle, savedInstanceState: Bundle?) {
        ribLifecycle.createDestroy {
            bind(feature.news to output using NewsToOutput)
            bind(input to feature using InputToWish)
        }
    }

    override fun onViewCreated(view: MainHelloWorldView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to MainHelloWorldAnalytics using ViewEventToAnalyticsEvent)
            bind(view to viewEventConsumer)
            bind(activityStarter.events(this@MainHelloWorldInteractor) to activityResultConsumer)
            bind(dummyViewInput to view)
        }
    }

    private val viewEventConsumer : Consumer<MainHelloWorldView.Event> = Consumer {
        when (it) {
            MainHelloWorldView.Event.ButtonClicked -> launchOtherActivityForResult()
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
