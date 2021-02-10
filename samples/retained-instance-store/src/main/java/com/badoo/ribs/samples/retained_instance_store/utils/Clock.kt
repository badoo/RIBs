package com.badoo.ribs.samples.retained_instance_store.utils

import android.os.Handler
import android.os.Looper
import java.util.*
import kotlin.concurrent.fixedRateTimer

private const val PERIOD_MS = 1000L

interface Clock {

    val elapsedTicks: Int

    fun starTimer(onTick: (counter: Int) -> Unit)

    fun stopTimer()
}

class SecondsClock() : Clock {

    private var activeTimer: Timer? = null
    private var counter = 0

    override val elapsedTicks: Int
        get() = counter


    override fun starTimer(onTick: (counter: Int) -> Unit) {
        if (activeTimer == null) {
            val uiHandler = Handler(Looper.getMainLooper())
            activeTimer = fixedRateTimer(period = PERIOD_MS) {
                uiHandler.post {
                    onTick(counter)
                    counter++
                }
            }
        }
    }

    override fun stopTimer() {
        activeTimer?.cancel()
        counter = 0
        activeTimer = null
    }

}