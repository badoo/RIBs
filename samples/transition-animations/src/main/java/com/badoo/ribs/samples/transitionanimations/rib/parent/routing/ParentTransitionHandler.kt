package com.badoo.ribs.samples.transitionanimations.rib.parent.routing

import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.badoo.ribs.routing.transition.TransitionDirection
import com.badoo.ribs.routing.transition.effect.sharedelement.SharedElementTransition
import com.badoo.ribs.routing.transition.handler.CrossFader
import com.badoo.ribs.routing.transition.handler.SharedElements
import com.badoo.ribs.routing.transition.handler.Slider
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.samples.transitionanimations.R
import com.badoo.ribs.samples.transitionanimations.rib.parent.routing.ParentRouter.Configuration

class ParentTransitionHandler(duration: Long = 500) : TransitionHandler.Multiple<Configuration>(
    listOf(
        SharedElements(
            params = listOf(
                SharedElementTransition.Params(
                    duration = duration,
                    findExitingElement = { it.findViewById(R.id.sharedElement) },
                    findEnteringElement = { it.findViewById(R.id.sharedElement) },
                    translateYInterpolator = FastOutLinearInInterpolator()
                )
            )
        ),
        CrossFader(
            duration = duration,
            condition = { it.direction == TransitionDirection.EXIT }
        ),
        Slider(
            duration = duration
        )
    )
)