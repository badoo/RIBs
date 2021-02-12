package com.badoo.ribs.core.plugin.utils.logger

interface LoggingPolicy {

    val logOnBuild: Boolean
    val logOnAttach: Boolean
    val logOnDestroy: Boolean
    val logOnAttachToView: Boolean
    val logOnDetachFromView: Boolean
    val logOnChildBuilt: Boolean
    val logOnChildAttached: Boolean
    val logOnChildDetached: Boolean
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
    val logHandleUpNavigation: Boolean

    /**
     * Allows what you tell it to, denies everything else by default
     */
    @SuppressWarnings("LongParameterList")
    class AllowList(
        override val logOnBuild: Boolean = false,
        override val logOnAttach: Boolean = false,
        override val logOnDestroy: Boolean = false,
        override val logOnAttachToView: Boolean = false,
        override val logOnDetachFromView: Boolean = false,
        override val logOnChildBuilt: Boolean = false,
        override val logOnChildAttached: Boolean = false,
        override val logOnChildDetached: Boolean = false,
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
        override val logHandleBackPressFallback: Boolean = false,
        override val logHandleUpNavigation: Boolean = false
    ) : LoggingPolicy

    /**
     * Denies what you tell it to, allows everything else by default
     */
    @SuppressWarnings("LongParameterList")
    class DenyList(
        override val logOnBuild: Boolean = true,
        override val logOnAttach: Boolean = true,
        override val logOnDestroy: Boolean = true,
        override val logOnAttachToView: Boolean = true,
        override val logOnDetachFromView: Boolean = true,
        override val logOnChildBuilt: Boolean = true,
        override val logOnChildAttached: Boolean = true,
        override val logOnChildDetached: Boolean = true,
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
        override val logHandleBackPressFallback: Boolean = true,
        override val logHandleUpNavigation: Boolean = true
    ) : LoggingPolicy
}
