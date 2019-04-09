package com.badoo.ribs.tutorials.tutorial2.rib.greetings_container

import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.android.PermissionRequester
import com.badoo.ribs.core.directory.Directory
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.builder.GreetingsContainerBuilder
import io.reactivex.Observable.empty
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.junit.Rule
import org.junit.Test

class GreetingsContainerTest {

    @get:Rule
    val ribsRule = RibsRule { buildRib(it) }

    private fun buildRib(ribTestActivity: RibTestActivity) =
        GreetingsContainerBuilder(object : GreetingsContainer.Dependency {
            override fun greetingsContainerInput(): ObservableSource<GreetingsContainer.Input> = empty()
            override fun greetingsContainerOutput(): Consumer<GreetingsContainer.Output> = Consumer {}
            override fun ribCustomisation(): Directory = TODO()
            override fun activityStarter(): ActivityStarter = ribTestActivity.activityStarter
            override fun permissionRequester(): PermissionRequester = ribTestActivity.permissionRequester
            override fun dialogLauncher(): DialogLauncher = ribTestActivity
        }).build()

    @Test
    fun testTextDisplayed() {
        TODO("Write UI tests")
    }
}
