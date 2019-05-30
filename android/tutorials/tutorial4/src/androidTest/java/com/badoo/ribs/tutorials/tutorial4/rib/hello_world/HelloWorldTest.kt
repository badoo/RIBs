package com.badoo.ribs.tutorials.tutorial4.rib.hello_world

import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.android.PermissionRequester
import com.badoo.ribs.core.directory.Directory
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.tutorials.tutorial4.rib.hello_world.builder.HelloWorldBuilder
import io.reactivex.Observable.empty
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.junit.Rule
import org.junit.Test

class HelloWorldTest {

    @get:Rule
    val ribsRule = RibsRule { buildRib(it) }

    private fun buildRib(ribTestActivity: RibTestActivity) =
        HelloWorldBuilder(object : HelloWorld.Dependency {
            override fun helloWorldInput(): ObservableSource<HelloWorld.Input> = empty()
            override fun helloWorldOutput(): Consumer<HelloWorld.Output> = Consumer {}
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
