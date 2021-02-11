package com.badoo.ribs.samples.retained_instance_store.rib.counter

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.retained_instance_store.R

interface CounterView : RibView {

    interface Factory : ViewFactory<Dependency, CounterView>

    interface Dependency {
        val isRetained: Boolean
    }

    fun updateCount(count: Int)
}

class CounterViewImpl private constructor(
    override val androidView: ViewGroup,
    private val isRetained: Boolean
) : AndroidRibView(), CounterView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_counter
    ) : CounterView.Factory {
        override fun invoke(deps: CounterView.Dependency): (RibView) -> CounterView = {
            CounterViewImpl(
                androidView = it.inflate(layoutRes),
                isRetained = deps.isRetained
            )
        }
    }

    private val counterView: TextView = androidView.findViewById(R.id.seconds_counter)
    private val descriptionText = androidView.findViewById<TextView>(R.id.description_text)

    init {
        with(androidView?.resources) {
            descriptionText.text = if (isRetained) {
                getString(R.string.retained_description)
            } else {
                getString(R.string.not_retained_description)
            }
        }
    }

    override fun updateCount(count: Int) {
        counterView.text = count.toString()
    }
}
