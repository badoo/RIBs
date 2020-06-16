package com.badoo.ribs.sandbox.rib.big.builder

import com.badoo.ribs.sandbox.rib.big.Big
import com.badoo.ribs.sandbox.rib.big.BigNode
import com.badoo.ribs.sandbox.rib.small.Small

@BigScope
//@dagger.Component(
//    modules = [BigModule::class],
//    dependencies = [Big.Dependency::class, Big.ExtraDependencies::class]
//)
internal interface BigComponent : Small.Dependency {

    fun node(): BigNode
}
