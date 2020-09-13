package com.badoo.ribs.routing.transition

/**
 * See the commit this class was introduced in.
 *
 * Before that exiting + entering was hidden behind a single Transition, which gives a simpler
 * but more limited way of interacting with them from code.
 *
 * It was added to support continuation (build on top of mid-flight incoming transition), which
 * was dropped for now. If we ever plan to add back support for it, this abstraction is needed,
 * and introducing this class at that point will break client code (TransitionHandler needs to
 * return an instance of this instead of just [Transition]), that's why this is left in place for now.
 */
data class TransitionPair(
    val exiting: Transition?,
    val entering: Transition?
)
