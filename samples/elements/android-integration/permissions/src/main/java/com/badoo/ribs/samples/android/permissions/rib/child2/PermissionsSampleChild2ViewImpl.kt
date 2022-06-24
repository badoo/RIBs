package com.badoo.ribs.samples.android.permissions.rib.child2

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.android.permissions.R

interface PermissionsSampleChild2View : RibView {

    interface Factory : ViewFactoryBuilder<Dependency, PermissionsSampleChild2ViewImpl>

    interface Dependency {
        val presenter: PermissionsSampleChild2Presenter
    }

    fun setText(text: String)
}

class PermissionsSampleChild2ViewImpl private constructor(
    override val androidView: ViewGroup,
    private val presenter: PermissionsSampleChild2Presenter
) : AndroidRibView(), PermissionsSampleChild2View {

    class Factory(@LayoutRes private val layoutRes: Int = R.layout.rib_permissions_sample) : PermissionsSampleChild2View.Factory {
        override fun invoke(deps: PermissionsSampleChild2View.Dependency): ViewFactory<PermissionsSampleChild2ViewImpl> = ViewFactory {
            PermissionsSampleChild2ViewImpl(
                androidView = it.inflate(layoutRes),
                presenter = deps.presenter
            )
        }
    }

    private val availablePermissionsTextView: TextView = androidView.findViewById(R.id.availablePermissions_text)
    private val checkButton: Button = androidView.findViewById(R.id.check_permissions_button)
    private val requestButton: Button = androidView.findViewById(R.id.request_permissions_button)

    init {
        checkButton.setOnClickListener { presenter.onCheckPermissionsClicked() }
        requestButton.setOnClickListener { presenter.onRequestPermissionsClicked() }
    }

    override fun setText(text: String) {
        availablePermissionsTextView.text = text
    }
}
