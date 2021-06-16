package com.badoo.ribs.test.integrationpoint

import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.integrationpoint.IntegrationPoint
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.test.assertTrue
import org.mockito.Mockito.mock

class TestIntegrationPoint(
    lifecycle: LifecycleOwner,
    savedInstanceState: Bundle? = null
) : IntegrationPoint(
    lifecycleOwner = lifecycle,
    viewLifecycleOwner = TestLiveData(lifecycle),
    savedInstanceState = savedInstanceState,
    rootViewHostFactory = {
        object : AndroidRibView() {
            override val androidView: ViewGroup = mock(ViewGroup::class.java)
        }
    },
) {

    private var upNavigationHandled = false

    override val dialogLauncher: TestRibDialogLauncher = TestRibDialogLauncher()
    override val activityStarter: ActivityStarter = TestActivityStarter()
    override val permissionRequester: TestPermissionRequester = TestPermissionRequester()
    override val isFinishing: Boolean = true

    override fun handleUpNavigation() {
        upNavigationHandled = true
    }

    fun assertNavigatedUp() {
        assertTrue(upNavigationHandled) { "Has not navigated up" }
    }

    fun assertNotNavigatedUp() {
        assertTrue(!upNavigationHandled) { "Has navigated up" }
    }

    // LiveData can't be launched in JUnit environment without Robolectric (thanks Google!), use test stub to avoid usage of Looper.
    private class TestLiveData<T>(private val value: T) : LiveData<T>() {

        override fun getValue(): T? = value

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            owner.lifecycle.subscribe(
                onCreate = { observer.onChanged(value) }
            )
        }

    }

}
