package com.badoo.ribs.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.badoo.ribs.android.ActivityStarter.ActivityResultEvent
import com.badoo.ribs.android.requestcode.RequestCodeBasedEventStreamImpl
import com.badoo.ribs.android.requestcode.RequestCodeRegistry
import com.badoo.ribs.core.Identifiable

class ActivityStarterImpl(
    private val activity: Activity,
    requestCodeRegistry: RequestCodeRegistry
) : RequestCodeBasedEventStreamImpl<ActivityResultEvent>(requestCodeRegistry),
    ActivityStarter,
    ActivityResultHandler {

    override fun startActivity(createIntent: Context.() -> Intent) {
        activity.startActivity(activity.createIntent())
    }

    override fun startActivityForResult(client: Identifiable, requestCode: Int, createIntent: Context.() -> Intent) {
        activity.startActivityForResult(
            activity.createIntent(),
            client.forgeExternalRequestCode(requestCode)
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        publish(
            requestCode,
            ActivityResultEvent(
                requestCode = requestCode.toInternalRequestCode(),
                resultCode = resultCode,
                data = data
            )
        )
    }
}
