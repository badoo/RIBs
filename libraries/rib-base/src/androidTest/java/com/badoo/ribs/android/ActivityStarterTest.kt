package com.badoo.ribs.android

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.Instrumentation
import android.content.ComponentName
import android.content.Intent
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.badoo.ribs.android.activitystarter.ActivityResultHandler
import com.badoo.ribs.android.activitystarter.ActivityStarter.ActivityResultEvent
import com.badoo.ribs.android.requestcode.RequestCodeClient
import com.badoo.ribs.rx2.adapter.rx2
import com.badoo.ribs.test.util.OtherActivity
import com.badoo.ribs.test.util.TestActivity
import com.badoo.ribs.test.util.TestRequestCodeClient
import com.badoo.ribs.test.util.restartActivitySync
import com.badoo.ribs.test.util.subscribeOnTestObserver
import com.badoo.ribs.test.util.waitForIdle
import io.reactivex.observers.TestObserver
import org.assertj.core.api.Assertions.assertThat
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
        activityRule.activity.activityStarter.events(identifiable).rx2().subscribeOnTestObserver()

        activityRule.activity.activityStarter.startActivityForResult(identifiable, requestCode = 1) {
            Intent(this, OtherActivity::class.java)
        }

        intended(hasComponent(ComponentName(getTargetContext(), OtherActivity::class.java)))
    }

    @Test
    fun startActivityForResult_startActivityThatReturnsOkResult_returnsOkResultCode() {
        givenResultForActivity<OtherActivity>(resultCode = RESULT_OK)
        val observer = activityRule.activity.activityStarter.events(identifiable).rx2().subscribeOnTestObserver()

        activityRule.activity.activityStarter.startActivityForResult(identifiable, requestCode = 1) {
            Intent(this, OtherActivity::class.java)
        }

        waitForIdle {
            observer.assertEvent(ActivityResultEvent(1, RESULT_OK, null))
        }
    }

    @Test
    fun startActivityForResult_startActivityThatReturnsOkResultAndRestart_returnsOkResultCode() {
        activityRule.activity.activityStarter.events(identifiable).rx2().subscribeOnTestObserver()
        activityRule.activity.ignoreActivityStarts = true

        activityRule.activity.activityStarter.startActivityForResult(identifiable, requestCode = 1) {
            Intent(this, OtherActivity::class.java)
        }
        val requestCode = activityRule.activity.lastStartedRequestCode
        activityRule.restartActivitySync()
        val observer = activityRule.activity.activityStarter.events(identifiable).rx2().subscribeOnTestObserver()
        (activityRule.activity.activityStarter as ActivityResultHandler).onActivityResult(
            requestCode,
            RESULT_OK,
            null
        )

        waitForIdle {
            observer.assertEvent(ActivityResultEvent(1, RESULT_OK, null))
        }
    }

    @Test
    fun startActivityForResult_startActivitiesWithCollisionThatReturnsOkResultAndRestart_returnsOkResultCode() {
        assertThat(collisionIdentifiable1.requestCodeClientId.hashCode()).isEqualTo(collisionIdentifiable2.requestCodeClientId.hashCode())
        activityRule.activity.activityStarter.events(identifiable).rx2().subscribeOnTestObserver()
        activityRule.activity.ignoreActivityStarts = true

        startOtherActivity(collisionIdentifiable1, requestCode = 1)
        startOtherActivity(collisionIdentifiable2, requestCode = 1)
        val requestCode = activityRule.activity.lastStartedRequestCode
        activityRule.restartActivitySync()
        val observer = activityRule.activity.activityStarter.events(collisionIdentifiable2).rx2().subscribeOnTestObserver()
        (activityRule.activity.activityStarter as ActivityResultHandler).onActivityResult(
            requestCode,
            RESULT_OK,
            null
        )

        waitForIdle {
            observer.assertEvent(ActivityResultEvent(1, RESULT_OK, null))
        }
    }

    @Test
    fun startActivityForResult_startActivityThatReturnsCancelledResult_returnsCancelledResultCode() {
        givenResultForActivity<OtherActivity>(resultCode = RESULT_CANCELED)
        val observer = activityRule.activity.activityStarter.events(identifiable).rx2().subscribeOnTestObserver()

        activityRule.activity.activityStarter.startActivityForResult(identifiable, requestCode = 1) {
            Intent(this, OtherActivity::class.java)
        }

        waitForIdle {
            observer.assertEvent(ActivityResultEvent(1, RESULT_CANCELED, null))
        }
    }

    @Test
    fun startActivityForResult_startActivityThatReturnsIntentData_returnsIntentData() {
        val data = Intent().apply { putExtra("some_param", "some_value") }
        givenResultForActivity<OtherActivity>(resultCode = RESULT_OK, data = data)
        val observer = activityRule.activity.activityStarter.events(identifiable).rx2().subscribeOnTestObserver()

        activityRule.activity.activityStarter.startActivityForResult(identifiable, requestCode = 1) {
            Intent(this, OtherActivity::class.java)
        }

        waitForIdle {
            observer.assertEvent(ActivityResultEvent(1, RESULT_OK, data))
        }
    }

    @Test
    fun startActivityForResult_startActivityWhenWeHaveMultipleIdentifiers_returnsResultEventOnlyForOne() {
        val otherIdentifiable = TestRequestCodeClient("other")
        givenResultForActivity<OtherActivity>(resultCode = RESULT_OK)
        val otherIdentifiableObserver = activityRule.activity.activityStarter.events(otherIdentifiable).rx2().subscribeOnTestObserver()
        val observer = activityRule.activity.activityStarter.events(identifiable).rx2().subscribeOnTestObserver()

        activityRule.activity.activityStarter.startActivityForResult(otherIdentifiable, requestCode = 1) {
            Intent(this, OtherActivity::class.java)
        }

        waitForIdle {
            otherIdentifiableObserver.assertEvent(ActivityResultEvent(1, RESULT_OK, null))
            observer.assertEmpty()
        }
    }

    private fun TestObserver<ActivityResultEvent>.assertEvent(event: ActivityResultEvent) {
        awaitCount(1)
        assertValue(event)
    }

    private inline fun <reified T> givenResultForActivity(resultCode: Int, data: Intent? = null) {
        intending(hasComponent(T::class.java.name)).respondWith(Instrumentation.ActivityResult(resultCode, data))
    }

    private fun startOtherActivity(client: RequestCodeClient, requestCode: Int) {
        activityRule.activity.activityStarter.startActivityForResult(client, requestCode = requestCode) {
            Intent(this, OtherActivity::class.java)
        }
    }

    companion object {
        private val identifiable = TestRequestCodeClient()
        private val collisionIdentifiable1 = TestRequestCodeClient(requestCodeClientId = "Siblings")
        private val collisionIdentifiable2 = TestRequestCodeClient(requestCodeClientId = "Teheran")
    }
}
