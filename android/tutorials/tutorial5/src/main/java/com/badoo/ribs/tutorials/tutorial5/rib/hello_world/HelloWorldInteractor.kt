package com.badoo.ribs.tutorials.tutorial5.rib.hello_world

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.tutorials.tutorial5.R
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorld.Config
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorld.Input
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorld.Output
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorldRouter.Configuration
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorldRouter.Configuration.Content
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorldView.Event
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorldView.ViewModel
import com.badoo.ribs.android.Text
import com.badoo.ribs.tutorials.tutorial5.util.User
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class HelloWorldInteractor(
    private val user: User,
    private val config: Config,
    private val input: ObservableSource<Input>,
    private val output: Consumer<Output>,
    savedInstanceState: Bundle?,
    router: Router<Configuration, Nothing, Content, Nothing, HelloWorldView>
) : Interactor<Configuration, Content, Nothing, HelloWorldView>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = null
) {

    private var currentButtonText = config.buttonText
    private val initialViewModel = viewModel(currentButtonText)
    private val inputToViewModel = { input: Input ->
        when (input) {
            is Input.UpdateButtonText -> {
                viewModel(input.text.also { currentButtonText = it })
            }
        }
    }

    private fun viewModel(buttonText: Text): ViewModel {
        return ViewModel(
            titleText = Text.Resource(R.string.hello_world_title, user.name()),
            welcomeText = config.welcomeMessage,
            buttonText = buttonText
        )
    }

    private val viewEventToOutput = { event: Event ->
        when (event) {
            Event.HelloButtonClicked -> Output.HelloThere(currentButtonText)
        }
    }

    override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        view.accept(initialViewModel)
        viewLifecycle.startStop {
            bind(view to output using viewEventToOutput)
            bind(input to view using inputToViewModel)
        }
    }
}
