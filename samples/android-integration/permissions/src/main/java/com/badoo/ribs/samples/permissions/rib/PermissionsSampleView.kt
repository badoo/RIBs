package com.badoo.ribs.samples.permissions.rib

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.permissions.R

interface PermissionsView : RibView {

    interface Factory : ViewFactoryBuilder<Dependency, PermissionsView>

    interface Dependency {
        val presenter: PermissionsSamplePresenter
    }

    fun setText(text: String)
}

class PermissionsViewImpl private constructor(
    override val androidView: ViewGroup,
    private val presenter: PermissionsSamplePresenter
) : AndroidRibView(), PermissionsView {

    class Factory(@LayoutRes private val layoutRes: Int = R.layout.rib_permissions_sample) : PermissionsView.Factory {
        override fun invoke(deps: PermissionsView.Dependency): ViewFactory<PermissionsView> = ViewFactory {
            PermissionsViewImpl(
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
