package com.badoo.ribs.example.rib.small.builder

import android.os.Bundle
import com.badoo.ribs.example.rib.big.Big
import com.badoo.ribs.example.rib.small.Small
import com.badoo.ribs.example.rib.small.SmallNode
import dagger.BindsInstance

@SmallScope
@dagger.Component(
    modules = [SmallModule::class],
    dependencies = [Small.Dependency::class]
)
internal interface SmallComponent : Big.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: Small.Dependency,
            @BindsInstance customisation: Small.Customisation,
            @BindsInstance savedInstanceState: Bundle?
        ): SmallComponent
    }

    fun node(): SmallNode
}
