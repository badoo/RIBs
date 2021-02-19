package com.badoo.ribs.samples.retained_instance_store.utils

import android.os.Handler
import android.os.Looper
import java.util.*
import kotlin.concurrent.fixedRateTimer

private const val PERIOD_MS = 1000L

interface Clock {

    val elapsedTicks: Int

    fun start()

    fun stop()

    fun bindOnTick(onTick: (counter: Int) -> Unit)

    fun unBindOnTick()

    fun dispose() {
        stop()
    }
}

class SecondsClock : Clock {

    private var activeTimer: Timer? = null
    private var counter = 0
    private var onTick: ((Int) -> Unit)? = null

    override val elapsedTicks: Int
        get() = counter


    override fun start() {
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

    override fun stop() {
        unBindOnTick()
        activeTimer?.cancel()
        counter = 0
        activeTimer = null
    }

    override fun bindOnTick(onTick: (counter: Int) -> Unit) {
        this.onTick = onTick
    }

    override fun unBindOnTick() {
        onTick = null
    }

}