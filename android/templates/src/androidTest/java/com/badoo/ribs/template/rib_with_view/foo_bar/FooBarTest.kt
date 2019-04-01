package com.badoo.ribs.template.rib_with_view.foo_bar

import android.content.Intent
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.android.IntentCreator
import com.badoo.ribs.android.PermissionRequester
import com.badoo.ribs.core.Identifiable
import com.badoo.ribs.core.directory.Directory
import com.badoo.ribs.template.rib_with_view.foo_bar.builder.FooBarBuilder
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.junit.Rule
import org.junit.Test

class FooBarTest {

    @get:Rule
    val ribsRule = RibsRule { buildRib(it) }

    private fun buildRib(ribTestActivity: RibTestActivity) =
        FooBarBuilder(object : FooBar.Dependency {
            override fun fooBarInput(): ObservableSource<FooBar.Input> = empty()
            override fun fooBarOutput(): Consumer<FooBar.Output> = Consumer {}
            override fun ribCustomisation(): Directory = TODO()
            override fun activityStarter(): ActivityStarter = object : ActivityStarter {
                override fun startActivity(createIntent: IntentCreator.() -> Intent) = TODO()
                override fun startActivityForResult(client: Identifiable, requestCode: Int, createIntent: IntentCreator.() -> Intent) = TODO()
                override fun events(client: Identifiable): Observable<ActivityStarter.ActivityResultEvent> = Observable.empty()
            }

            override fun permissionRequester(): PermissionRequester = object : PermissionRequester {
                override fun checkPermissions(client: Identifiable, permissions: Array<String>) = TODO()
                override fun requestPermissions(client: Identifiable, requestCode: Int, permissions: Array<String>) = TODO()
                override fun events(client: Identifiable): Observable<PermissionRequester.RequestPermissionsEvent> = Observable.empty()
            }
        }).build()

    @Test
    fun testTextDisplayed() {
        TODO("Write UI tests")
    }
}
