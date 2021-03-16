package com.badoo.ribs.samples.helloworld.hello_world

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.helloworld.R
import com.google.android.material.snackbar.Snackbar

interface HelloWorldView : RibView {

    interface Factory : ViewFactoryBuilder<Dependency, HelloWorldView>

    interface Dependency {
        val presenter: HelloWorldPresenter
    }

    fun setText(text: String)

    fun showCount(count: Int)
}

class HelloWorldViewImpl private constructor(
    override val androidView: ViewGroup,
    private val presenter: HelloWorldPresenter
) : AndroidRibView(), HelloWorldView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_hello_world
    ) : HelloWorldView.Factory {
        override fun invoke(deps: HelloWorldView.Dependency): ViewFactory<HelloWorldView> =
            ViewFactory {
                HelloWorldViewImpl(
                    androidView = it.inflate(layoutRes),
                    presenter = deps.presenter
                )
            }
    }

    private val textView: TextView = androidView.findViewById(R.id.hello_world_text)
    private val button: Button = androidView.findViewById(R.id.hello_world_button)

    init {
        button.setOnClickListener { presenter.onShowCountClicked() }
    }

    override fun setText(text: String) {
        textView.text = text
    }

    override fun showCount(count: Int) {
        Snackbar.make(androidView, "Current count: $count", Snackbar.LENGTH_SHORT).show()
    }
}
