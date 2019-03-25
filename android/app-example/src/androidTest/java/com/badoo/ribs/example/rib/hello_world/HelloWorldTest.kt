package com.badoo.ribs.example.rib.hello_world

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.android.IntentCreator
import com.badoo.ribs.android.PermissionRequester
import com.badoo.ribs.core.Identifiable
import com.badoo.ribs.core.directory.Directory
import com.badoo.ribs.example.R
import com.badoo.ribs.example.app.AppRibCustomisations
import com.badoo.ribs.example.rib.hello_world.builder.HelloWorldBuilder
import io.reactivex.Observable
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
            override fun ribCustomisation(): Directory = AppRibCustomisations
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
        onView(withId(R.id.hello_title)).check(matches(isDisplayed()))
    }
}
