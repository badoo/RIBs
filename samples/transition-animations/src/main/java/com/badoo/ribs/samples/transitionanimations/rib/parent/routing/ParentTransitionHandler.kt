package com.badoo.ribs.samples.transitionanimations.rib.parent.routing

import com.badoo.ribs.routing.transition.handler.CrossFader
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.samples.transitionanimations.rib.parent.routing.ParentRouter.Configuration

class ParentTransitionHandler : TransitionHandler.Multiple<Configuration>(
        listOf<TransitionHandler<Configuration>>(
                CrossFader(
                        duration = 1000L
                )
        )
)