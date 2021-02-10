package com.badoo.ribs.samples.retained_instance_store.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.*
import kotlin.concurrent.fixedRateTimer

private const val PERIOD_MS = 1000L

interface Clock {

    val elapsedTicks: Int

    fun startTimer()

    fun stopTimer()

    fun bindOnTick(onTick: (counter: Int) -> Unit)

    fun dispose(){
        Log.d("Clock", "disposed")
        stopTimer()
    }
}

class SecondsClock : Clock {

    private var activeTimer: Timer? = null
    private var counter = 0
    private var onTick: ((Int) -> Unit)? = null

    override val elapsedTicks: Int
        get() = counter


    override fun startTimer() {
        if (activeTimer == null) {
            val uiHandler = Handler(Looper.getMainLooper())
            activeTimer = fixedRateTimer(period = PERIOD_MS) {
                uiHandler.post {
                    onTick?.invoke(counter)
                    counter++
                }
            }
        }
    }

    override fun stopTimer() {
        activeTimer?.cancel()
        counter = 0
        onTick = null
        activeTimer = null
    }

    override fun bindOnTick(onTick: (counter: Int) -> Unit) {
        this.onTick = onTick
    }

}