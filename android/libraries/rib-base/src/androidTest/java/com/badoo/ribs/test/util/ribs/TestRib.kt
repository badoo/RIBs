package com.badoo.ribs.test.util.ribs

import com.badoo.ribs.core.Rib

interface TestRib : Rib {

    interface Dependency : Rib.Dependency
}
