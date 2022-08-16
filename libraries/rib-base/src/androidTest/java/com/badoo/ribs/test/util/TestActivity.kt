package com.badoo.ribs.test.util

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.badoo.ribs.android.activitystarter.ActivityBoundary
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.permissionrequester.PermissionRequestBoundary
import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.android.requestcode.RequestCodeRegistry

class TestActivity : AppCompatActivity() {

    private lateinit var requestCodeRegistry: RequestCodeRegistry

    val activityStarter: ActivityStarter
        get() = activityBoundary

    val permissionRequester: PermissionRequester
        get() = permissionRequestBoundary

    var ignoreActivityStarts: Boolean = false
    var lastStartedRequestCode: Int = -1
    var lastStartedOptions: Bundle? = null

    private val activityBoundary: ActivityBoundary by lazy {
        ActivityBoundary(
            activity = this,
            requestCodeRegistry = requestCodeRegistry
        )
    }

    private val permissionRequestBoundary: PermissionRequestBoundary by lazy {
        PermissionRequestBoundary(
            activity = this,
            requestCodeRegistry = requestCodeRegistry
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestCodeRegistry = RequestCodeRegistry(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        requestCodeRegistry.onSaveInstanceState(outState)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        if (!ignoreActivityStarts) {
            super.startActivityForResult(intent, requestCode, options)
        }
        lastStartedOptions = options
        lastStartedRequestCode = requestCode
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        lastStartedOptions = options
        super.startActivity(intent, options)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activityBoundary.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) =
        permissionRequestBoundary.onRequestPermissionsResult(requestCode, permissions, grantResults)

}
