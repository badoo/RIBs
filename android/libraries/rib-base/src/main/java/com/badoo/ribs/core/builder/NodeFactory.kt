package com.badoo.ribs.core.builder

import com.badoo.ribs.core.Rib

typealias NodeFactory = (buildContext: BuildContext) -> Rib<*>

