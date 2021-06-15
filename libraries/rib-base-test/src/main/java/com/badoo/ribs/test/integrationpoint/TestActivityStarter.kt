package com.badoo.ribs.test.integrationpoint

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.requestcode.RequestCodeClient
import com.badoo.ribs.minimal.reactive.Relay
import com.badoo.ribs.minimal.reactive.Source
import org.mockito.Mockito.mock

class TestActivityStarter : ActivityStarter {

    private val events = Relay<ActivityStarter.ActivityResultEvent>()
    private val startedIntents = ArrayList<Intent>()
    private val stubbed = ArrayList<ActivityStarter.ActivityResultEvent>()

    override fun events(client: RequestCodeClient): Source<ActivityStarter.ActivityResultEvent> =
        events

    override fun startActivity(createIntent: Context.() -> Intent) {
        startedIntents += createIntent(mock(Context::class.java))
    }

    override fun startActivityForResult(
        client: RequestCodeClient,
        requestCode: Int,
        createIntent: Context.() -> Intent
    ) {
        startedIntents += createIntent(mock(Context::class.java))
        val result =
            stubbed
                .find { it.requestCode == requestCode || it.requestCode == REQUEST_CODE_ANY }
                ?: error("Stub request code $requestCode first")
        events.accept(result)
    }

    fun stubActivityResult(
        requestCode: Int = REQUEST_CODE_ANY,
        resultCode: Int = Activity.RESULT_OK,
        data: Intent? = null
    ) {
        stubbed += ActivityStarter.ActivityResultEvent(requestCode, resultCode, data)
    }

    companion object {
        const val REQUEST_CODE_ANY: Int = -1
    }

}
