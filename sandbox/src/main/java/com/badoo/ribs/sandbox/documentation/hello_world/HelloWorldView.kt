package com.badoo.ribs.sandbox.documentation.hello_world

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.sandbox.R

interface HelloWorldView : RibView {

    interface Factory : ViewFactory<Nothing?, HelloWorldView>

    fun setText(text: String)
}


class HelloWorldViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    HelloWorldView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_hello_world_doc
    ) : HelloWorldView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> HelloWorldView = {
            HelloWorldViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

    private val textView: TextView = androidView.findViewById(R.id.text)

    override fun setText(text: String) {
        textView.text = text
    }
}
