package com.badoo.ribs.android

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.Instrumentation
import android.content.ComponentName
import android.content.Intent
import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra
import android.support.test.espresso.intent.rule.IntentsTestRule
import com.badoo.ribs.android.ActivityStarter.ActivityResultEvent
import com.badoo.ribs.test.util.OtherActivity
import com.badoo.ribs.test.util.TestActivity
import com.badoo.ribs.test.util.TestIdentifiable
import com.badoo.ribs.test.util.subscribeOnTestObserver
import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test


class ActivityStarterTest {

    @get:Rule
    val activityRule = IntentsTestRule<TestActivity>(TestActivity::class.java)

    @Test
    fun startActivity_startsTargetActivity() {
        activityRule.activity.activityStarter.startActivity {
            Intent(this, OtherActivity::class.java)
        }

        intended(hasComponent(ComponentName(getTargetContext(), OtherActivity::class.java)))
    }

    @Test
    fun startActivity_intentDataIsDeliveredToTargetActivity() {
        activityRule.activity.activityStarter.startActivity {
            Intent(this, OtherActivity::class.java)
                .putExtra("some_param", "some_value")
        }

        intended(allOf(
            hasComponent(ComponentName(getTargetContext(), OtherActivity::class.java)),
            hasExtra("some_param", "some_value")
        ))
    }

    @Test
    fun startActivityForResult_startsTargetActivity() {
        activityRule.activity.activityStarter.events(identifiable).subscribeOnTestObserver()

        activityRule.activity.activityStarter.startActivityForResult(identifiable, requestCode = 1) {
            Intent(this, OtherActivity::class.java)
        }

        intended(hasComponent(ComponentName(getTargetContext(), OtherActivity::class.java)))
    }

    @Test
    fun startActivityForResult_startActivityThatReturnsOkResult_returnsOkResultCode() {
        givenResultForActivity<OtherActivity>(resultCode = RESULT_OK)
        val observer = activityRule.activity.activityStarter.events(identifiable).subscribeOnTestObserver()

        activityRule.activity.activityStarter.startActivityForResult(identifiable, requestCode = 1) {
            Intent(this, OtherActivity::class.java)
        }

        observer.assertEvent(ActivityResultEvent(1, RESULT_OK, null))
    }

    @Test
    fun startActivityForResult_startActivityThatReturnsCancelledResult_returnsCancelledResultCode() {
        givenResultForActivity<OtherActivity>(resultCode = RESULT_CANCELED)
        val observer = activityRule.activity.activityStarter.events(identifiable).subscribeOnTestObserver()

        activityRule.activity.activityStarter.startActivityForResult(identifiable, requestCode = 1) {
            Intent(this, OtherActivity::class.java)
        }

        observer.assertEvent(ActivityResultEvent(1, RESULT_CANCELED, null))
    }

    @Test
    fun startActivityForResult_startActivityThatReturnsIntentData_returnsIntentData() {
        val data = Intent().apply { putExtra("some_param", "some_value") }
        givenResultForActivity<OtherActivity>(resultCode = RESULT_OK, data = data)
        val observer = activityRule.activity.activityStarter.events(identifiable).subscribeOnTestObserver()

        activityRule.activity.activityStarter.startActivityForResult(identifiable, requestCode = 1) {
            Intent(this, OtherActivity::class.java)
        }

        observer.assertEvent(ActivityResultEvent(1, RESULT_OK, data))
    }

    @Test
    fun startActivityForResult_startActivityWhenWeHaveMultipleIdentifiers_returnsResultEventOnlyForOne() {
        val otherIdentifiable = TestIdentifiable("other")
        givenResultForActivity<OtherActivity>(resultCode = RESULT_OK)
        val otherIdentifiableObserver = activityRule.activity.activityStarter.events(otherIdentifiable).subscribeOnTestObserver()
        val observer = activityRule.activity.activityStarter.events(identifiable).subscribeOnTestObserver()

        activityRule.activity.activityStarter.startActivityForResult(otherIdentifiable, requestCode = 1) {
            Intent(this, OtherActivity::class.java)
        }

        otherIdentifiableObserver.assertEvent(ActivityResultEvent(1, RESULT_OK, null))
        observer.assertEmpty()
    }

    private fun TestObserver<ActivityResultEvent>.assertEvent(event: ActivityResultEvent) {
        awaitCount(1)
        assertValue(event)
    }

    private inline fun <reified T> givenResultForActivity(resultCode: Int, data: Intent? = null) {
        intending(hasComponent(T::class.java.name)).respondWith(Instrumentation.ActivityResult(resultCode, data))
    }

    companion object {
        private val identifiable = TestIdentifiable()
    }
}
