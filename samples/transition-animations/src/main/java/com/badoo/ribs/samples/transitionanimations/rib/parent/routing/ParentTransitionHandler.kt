package com.badoo.ribs.samples.transitionanimations.rib.parent.routing

import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.badoo.ribs.routing.transition.TransitionDirection
import com.badoo.ribs.routing.transition.TransitionElement
import com.badoo.ribs.routing.transition.effect.sharedelement.SharedElementTransition
import com.badoo.ribs.routing.transition.handler.CrossFader
import com.badoo.ribs.routing.transition.handler.SharedElements
import com.badoo.ribs.routing.transition.handler.Slider
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.samples.transitionanimations.R
import com.badoo.ribs.samples.transitionanimations.rib.parent.routing.ParentRouter.Configuration

class ParentTransitionHandler(duration: Long = 750) : TransitionHandler.Multiple<Configuration>(
    listOf(
        SharedElements(
            params = listOf(
                SharedElementTransition.Params(
                    duration = duration,
                    findExitingElement = { it.findViewById(R.id.shared_element) },
                    findEnteringElement = { it.findViewById(R.id.shared_element) },
                    translateYInterpolator = FastOutLinearInInterpolator()
                )
            )
        ),
        CrossFader(
            duration = duration,
            condition = { it.direction == TransitionDirection.EXIT }
        ),
        Slider(
            duration = duration,
            condition = {
                it.isNotEnteringFirstChild()
                    && it.isNotExitingLastChild()

            }
        )
    )
)

fun TransitionElement<out Configuration>.isNotEnteringFirstChild(): Boolean =
    !(configuration == Configuration.Child1
        && direction == TransitionDirection.ENTER
        && addedOrRemoved)

fun TransitionElement<out Configuration>.isNotExitingLastChild(): Boolean =
    !(configuration == Configuration.Child3
        && direction == TransitionDirection.EXIT)
