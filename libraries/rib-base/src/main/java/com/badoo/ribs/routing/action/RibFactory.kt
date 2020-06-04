package com.badoo.ribs.routing.action

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.builder.BuildContext

typealias RibFactory = (buildContext: BuildContext) -> Rib

