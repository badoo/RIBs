package com.badoo.ribs.tutorials.tutorial5.rib.hello_world

import com.badoo.ribs.core.Rib
import com.badoo.ribs.android.Text
import com.badoo.ribs.tutorials.tutorial5.util.User
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface HelloWorld : Rib {

    interface Dependency {
        fun helloWorldInput(): ObservableSource<Input>
        fun helloWorldOutput(): Consumer<Output>
        fun user(): User
        fun config(): Config
    }

    sealed class Input {
        data class UpdateButtonText(val text: Text): Input()
    }

    sealed class Output {
        data class HelloThere(val greeting: Text) : Output()
    }

    data class Config(
        val welcomeMessage: Text,
        val buttonText: Text
    )

    class Customisation(
        val viewFactory: HelloWorldView.Factory = HelloWorldViewImpl.Factory()
    )
}
