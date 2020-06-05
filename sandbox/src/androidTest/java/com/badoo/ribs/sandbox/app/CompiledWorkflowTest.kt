package com.badoo.ribs.sandbox.app

import android.content.Intent
import android.net.Uri
import androidx.test.rule.ActivityTestRule
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Rule
import org.junit.Test

class CompiledWorkflowTest {

    @get:Rule
    val activityRule = ActivityTestRule(RootActivity::class.java, false, false)

    companion object {
        private const val SCHEME = "app"
        private const val URI_WORKFLOW1 = "$SCHEME://workflow1"
        private const val URI_WORKFLOW2 = "$SCHEME://workflow2"
    }

    private fun String.toIntent() =
        Intent().apply {
            data = Uri.parse(this@toIntent)
        }

    @Test
    fun workflow1() {
        activityRule.launchActivity(null)
        activityRule.runOnUiThread {
            val testObserver = TestObserver<Any>()
            workflowFor(URI_WORKFLOW1).subscribe(testObserver)
            testObserver.assertComplete()
            testObserver.assertNoErrors()
        }
    }

    @Test
    fun workflow2() {
        activityRule.launchActivity(null)
        activityRule.runOnUiThread {
            val testObserver = TestObserver<Any>()
            val workflow = workflowFor(URI_WORKFLOW2)
            workflow.subscribe(testObserver)
            testObserver.assertNoErrors()
        }
    }

    private fun workflowFor(uriString: String): Observable<*> = activityRule
        .activity
        .workflowFactory
        .invoke(uriString.toIntent())!!
}
