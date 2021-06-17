package com.badoo.ribs.test.sample

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams

class SampleNode(
    buildParams: BuildParams<*>
) : Node<SampleView>(
    buildParams = buildParams,
    viewFactory = { SampleViewImpl(it) }
), SampleRib
