package com.badoo.ribs.core.plugin.utils.logger

interface LoggingPolicy {

    val logOnCreate: Boolean
    val logOnAttach: Boolean
    val logOnDetach: Boolean
    val logOnAttachToView: Boolean
    val logOnDetachFromView: Boolean
    val logOnChildCreated: Boolean
    val logOnAttachChild: Boolean
    val logOnDetachChild: Boolean
    val logOnAttachChildView: Boolean
    val logOnDetachChildView: Boolean
    val logOnStart: Boolean
    val logOnStop: Boolean
    val logOnResume: Boolean
    val logOnPause: Boolean
    val logOnSaveInstanceState: Boolean
    val logOnLowMemory: Boolean
    val logHandleBackPress: Boolean
    val logHandleBackPressFirst: Boolean
    val logHandleBackPressFallback: Boolean

    /**
     * Allows what you tell it to, denies everything else by default
     */
    class AllowList(
        override val logOnCreate: Boolean = false,
        override val logOnAttach: Boolean = false,
        override val logOnDetach: Boolean = false,
        override val logOnAttachToView: Boolean = false,
        override val logOnDetachFromView: Boolean = false,
        override val logOnChildCreated: Boolean = false,
        override val logOnAttachChild: Boolean = false,
        override val logOnDetachChild: Boolean = false,
        override val logOnAttachChildView: Boolean = false,
        override val logOnDetachChildView: Boolean = false,
        override val logOnStart: Boolean = false,
        override val logOnStop: Boolean = false,
        override val logOnResume: Boolean = false,
        override val logOnPause: Boolean = false,
        override val logOnSaveInstanceState: Boolean = false,
        override val logOnLowMemory: Boolean = false,
        override val logHandleBackPress: Boolean = false,
        override val logHandleBackPressFirst: Boolean = false,
        override val logHandleBackPressFallback: Boolean = false
    ) : LoggingPolicy

    /**
     * Denies what you tell it to, allows everything else by default
     */
    class DenyList(
        override val logOnCreate: Boolean = true,
        override val logOnAttach: Boolean = true,
        override val logOnDetach: Boolean = true,
        override val logOnAttachToView: Boolean = true,
        override val logOnDetachFromView: Boolean = true,
        override val logOnChildCreated: Boolean = true,
        override val logOnAttachChild: Boolean = true,
        override val logOnDetachChild: Boolean = true,
        override val logOnAttachChildView: Boolean = true,
        override val logOnDetachChildView: Boolean = true,
        override val logOnStart: Boolean = true,
        override val logOnStop: Boolean = true,
        override val logOnResume: Boolean = true,
        override val logOnPause: Boolean = true,
        override val logOnSaveInstanceState: Boolean = true,
        override val logOnLowMemory: Boolean = true,
        override val logHandleBackPress: Boolean = true,
        override val logHandleBackPressFirst: Boolean = true,
        override val logHandleBackPressFallback: Boolean = true
    ) : LoggingPolicy
}
