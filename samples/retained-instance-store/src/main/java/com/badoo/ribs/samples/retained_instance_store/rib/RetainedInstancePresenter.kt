package com.badoo.ribs.samples.retained_instance_store.rib

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.plugin.ViewAware
import java.util.*
import kotlin.concurrent.fixedRateTimer

private const val PERIOD_MS = 1000L

interface RetainedInstancePresenter {

    fun onRotateScreenClicked()

    fun dispose()
}

internal class RetainedInstancePresenterImpl(private val orientationController: Activity) : RetainedInstancePresenter, ViewAware<RetainedInstanceView> {

    private var view: RetainedInstanceView? = null
    private var activeTimer: Timer? = null
    private var counter = 0

    override fun onViewCreated(view: RetainedInstanceView, viewLifecycle: Lifecycle) {
        viewLifecycle.subscribe(
                onCreate = {
                    this@RetainedInstancePresenterImpl.view = view
                    updateCount(counter)
                },
                onDestroy = {
                    this@RetainedInstancePresenterImpl.view = null
                }
        )
        startTimer()
    }

    override fun onRotateScreenClicked() {
        if (orientationController.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            orientationController.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            orientationController.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    override fun dispose() {
        stopTimer()
    }

    private fun updateCount(counter: Int) {
        view?.updateCount(counter % 10)
    }

    private fun startTimer() {
        if (activeTimer == null) {
            val uiHandler = Handler(Looper.getMainLooper())
            activeTimer = fixedRateTimer(initialDelay = PERIOD_MS, period = PERIOD_MS) {
                uiHandler.post {
                    updateCount(counter)
                }
                counter++
            }
        }
    }

    private fun stopTimer() {
        activeTimer?.cancel()
        activeTimer = null
    }

}
