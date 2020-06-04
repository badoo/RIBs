package com.badoo.ribs.test.util

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.activitystarter.ActivityStarterImpl
import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.android.permissionrequester.PermissionRequesterImpl
import com.badoo.ribs.android.requestcode.RequestCodeRegistry

class TestActivity : AppCompatActivity() {

    private lateinit var requestCodeRegistry: RequestCodeRegistry

    val activityStarter: ActivityStarter
        get() = _activityStarter

    val permissionRequester: PermissionRequester
        get() = _permissionRequester

    var ignoreActivityStarts: Boolean = false
    var lastStartedRequestCode: Int = -1

    private val _activityStarter: ActivityStarterImpl by lazy {
        ActivityStarterImpl(
            activity = this,
            requestCodeRegistry = requestCodeRegistry
        )
    }

    private val _permissionRequester: PermissionRequesterImpl by lazy {
        PermissionRequesterImpl(
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

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        if (!ignoreActivityStarts) {
            super.startActivityForResult(intent, requestCode)
        }
        lastStartedRequestCode = requestCode
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        _activityStarter.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) =
        _permissionRequester.onRequestPermissionsResult(requestCode, permissions, grantResults)

}
