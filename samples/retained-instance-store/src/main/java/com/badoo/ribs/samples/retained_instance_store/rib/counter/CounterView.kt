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

    interface Factory : ViewFactory<Nothing?, CounterView>

    fun updateCount(count: Int)

    fun updateRetainedCount(count: Int)

}

class CounterViewImpl private constructor(override val androidView: ViewGroup) : AndroidRibView(), CounterView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_counter
    ) : CounterView.Factory {
        override fun invoke(nothing: Nothing?): (RibView) -> CounterView = {
            CounterViewImpl(androidView = it.inflate(layoutRes))
        }
    }

    private val retainedCounter: TextView = androidView.findViewById(R.id.retained_seconds_counter)
    private val notRetainedCounter: TextView = androidView.findViewById(R.id.not_retained_seconds_counter)

    override fun updateCount(count: Int) {
        notRetainedCounter.text = count.toString()
    }

    override fun updateRetainedCount(count: Int) {
        retainedCounter.text = count.toString()
    }
}
