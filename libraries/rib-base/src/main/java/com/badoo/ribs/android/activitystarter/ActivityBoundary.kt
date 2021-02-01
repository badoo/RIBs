package com.badoo.ribs.android.activitystarter

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.badoo.ribs.android.activitystarter.ActivityStarter.ActivityResultEvent
import com.badoo.ribs.android.requestcode.RequestCodeBasedEventStreamImpl
import com.badoo.ribs.android.requestcode.RequestCodeClient
import com.badoo.ribs.android.requestcode.RequestCodeRegistry

class ActivityBoundary(
    private val activityStarterHost: ActivityStarterHost,
    requestCodeRegistry: RequestCodeRegistry
) : RequestCodeBasedEventStreamImpl<ActivityResultEvent>(requestCodeRegistry),
    ActivityStarter,
    ActivityResultHandler {

    constructor(
        activity: Activity,
        requestCodeRegistry: RequestCodeRegistry
    ) : this(
        ActivityStarterHost.ActivityHost(activity),
        requestCodeRegistry
    )

    constructor(
        fragment: Fragment,
        requestCodeRegistry: RequestCodeRegistry
    ) : this(
        ActivityStarterHost.FragmentHost(fragment),
        requestCodeRegistry
    )

    override fun startActivity(createIntent: Context.() -> Intent) {
        activityStarterHost.startActivity(activityStarterHost.context.createIntent())
    }

    override fun startActivityForResult(
        client: RequestCodeClient,
        requestCode: Int,
        createIntent: Context.() -> Intent
    ) {
        activityStarterHost.startActivityForResult(
            activityStarterHost.context.createIntent(),
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
