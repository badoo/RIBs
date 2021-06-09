package com.badoo.ribs.android.integrationpoint

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView

class FloatingIntegrationPoint : IntegrationPoint(
    lifecycleOwner = NoLifecycle(),
    viewLifecycleOwner = MutableLiveData(),
    savedInstanceState = null,
    rootViewHostFactory = { NoopRibView() }
) {
    companion object {
        private const val ERROR = "You're accessing a FloatingIntegrationPoint. " +
            "This means you're using a RIB without ever integrating it to a proper IntegrationPoint. " +
            "This is fine during tests with limited scope, but it looks like the code that leads here " +
            "requires interfacing with a valid implementation."
    }

    override val activityStarter: ActivityStarter
        get() = error(ERROR)

    override val permissionRequester: PermissionRequester
        get() = error(ERROR)

    override val dialogLauncher: DialogLauncher
        get() = error(ERROR)

    override val isFinishing: Boolean
        get() = true

    override fun handleUpNavigation() {
        error(ERROR)
    }

    private class NoLifecycle : LifecycleOwner {
        override fun getLifecycle(): Lifecycle =
            LifecycleRegistry(this)
    }

    private class NoopRibView : RibView {

        override val androidView: ViewGroup
            get() = error(ERROR)

        override fun attachChild(child: Node<*>, subtreeOf: Node<*>) {
            error(ERROR)
        }

        override fun detachChild(child: Node<*>, subtreeOf: Node<*>) {
            error(ERROR)
        }
    }
}
