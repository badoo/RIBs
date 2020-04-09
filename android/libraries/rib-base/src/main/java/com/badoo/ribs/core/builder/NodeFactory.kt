package com.badoo.ribs.core.builder

import com.badoo.ribs.core.Node

typealias NodeFactory = (buildContext: BuildContext) -> Node<*>

