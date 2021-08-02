package com.badoo.ribs.example

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.functions.Function
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import java.util.concurrent.Callable

class RxSchedulerExtension : AfterEachCallback, BeforeEachCallback {

    private val scheduler = Schedulers.trampoline()
    private val schedulerFunction: Function<Scheduler, Scheduler> = Function { scheduler }
    private val schedulerFunctionLazy: Function<Callable<Scheduler>, Scheduler> =
        Function { scheduler }

    @Throws(Throwable::class)
    override fun beforeEach(context: ExtensionContext?) {
        RxAndroidPlugins.reset()
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerFunctionLazy)

        RxJavaPlugins.reset()
        RxJavaPlugins.setIoSchedulerHandler(schedulerFunction)
        RxJavaPlugins.setNewThreadSchedulerHandler(schedulerFunction)
        RxJavaPlugins.setComputationSchedulerHandler(schedulerFunction)
    }

    @Throws(Throwable::class)
    override fun afterEach(context: ExtensionContext?) {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }
}
