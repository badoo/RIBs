package com.badoo.ribs.example.rib.big.builder

import android.os.Bundle
import com.badoo.ribs.example.rib.big.Big
import com.badoo.ribs.example.rib.big.BigNode
import com.badoo.ribs.example.rib.small.Small
import dagger.BindsInstance

@BigScope
@dagger.Component(
    modules = [BigModule::class],
    dependencies = [Big.Dependency::class]
)
internal interface BigComponent : Small.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: Big.Dependency,
            @BindsInstance customisation: Big.Customisation,
            @BindsInstance savedInstanceState: Bundle?
        ): BigComponent
    }

    fun node(): BigNode
}
