package com.badoo.ribs.routing.resolution

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext

typealias RibFactory = (buildContext: BuildContext) -> Rib

