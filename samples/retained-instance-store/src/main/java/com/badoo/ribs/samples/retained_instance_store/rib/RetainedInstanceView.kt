package com.badoo.ribs.samples.retained_instance_store.rib

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.retained_instance_store.R

interface RetainedInstanceView : RibView {

    interface Factory : ViewFactory<Dependency, RetainedInstanceView>

    interface Dependency {
        val presenter: RetainedInstancePresenter
    }

    fun updateCount(count: Int)
}

class RetainedInstanceViewImpl private constructor(
        override val androidView: ViewGroup,
        private val presenter: RetainedInstancePresenter
) : AndroidRibView(), RetainedInstanceView {

    class Factory(
            @LayoutRes private val layoutRes: Int = R.layout.rib_retained_instance
    ) : RetainedInstanceView.Factory {
        override fun invoke(deps: RetainedInstanceView.Dependency): (RibView) -> RetainedInstanceView = {
            RetainedInstanceViewImpl(
                    androidView = it.inflate(layoutRes),
                    presenter = deps.presenter
            )
        }
    }

    private val counterView: TextView = androidView.findViewById(R.id.seconds_counter)
    private val button: Button = androidView.findViewById(R.id.rotate_screen_button)

    init {
        button.setOnClickListener { presenter.onRotateScreenClicked() }
    }

    override fun updateCount(count: Int) {
        counterView.text = count.toString()
    }
}
