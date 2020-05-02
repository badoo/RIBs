# Tutorial #4

## The goal of this tutorial

To cover how to implement proper `Input` / `Output` communication with child RIBs.


## Outline

Every RIB can potentially expose a set of `Inputs` and `Outputs` on its main interface.

This is the public API of the RIB, and every RIB guarantees that as long as the proper dependencies are supplied (i.e. a consumer of its `Output` that it can talk to / a source of its `Input` that it can observe), it will automatically wire the required setup internally.

Which is an amazing thing when you think about it:
1. When using a RIB, it's not necessary to look inside and understand its internals to make it work, it's enough to have a look at its public API
2. It's also not necessary to create *any* additional wiring or setup to make it work: if you can build it, it works!


## Outputs

There's one thing we left out so far: in **tutorial2** we removed the functionality of the **Say hello!** button, and we said we'd put it back later.

Now's the time!

The classes in this tutorial have all the removed pieces put back in:
- `HelloWorld` interface (`Output` and dependency for it)
- `HelloWorldModule` - uses `output` when constructing Interactor
- `HelloWorldInteractor` - constructor parameter, and using `output` in `onViewCreated`
- `ViewEventToOutput` in the `hello_world.mapper` package

This means that as far as `HelloWorld` RIB is concerned, when its button is pressed, it will correctly trigger `Output.HelloThere` on its output channel.

Now of course we need to provide the dependency for consuming this `Output` from `GreetingsContainer`.
 
This will be similar to what we did in the previous tutorial - we will satisfy it in the parent, directly. 

More specifically, as `Outputs` and `Inputs` are forms of communication with a RIB, they should be handled as part of the business logic. Which means, we will want to implement it in the parent `Interactor`.

Let's open `GreetingsContainerInteractor` and implement reacting to its child's `Output`. What to do with its `Output` actually? Well, `GreetingsContainer` has its own `Output` that it communicates to the outside world, so right now we will make it trigger just that.

```kotlin
// mind the correct imports:
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorld
import android.os.Bundle

class GreetingsContainerInteractor(
    savedInstanceState: Bundle?,
    router: Router<Configuration, Nothing, Configuration, Nothing, Nothing>,
    output: Consumer<GreetingsContainer.Output>
) : Interactor<Configuration, Configuration, Nothing, Nothing>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = null
) {

    internal val helloWorldOutputConsumer: Consumer<HelloWorld.Output> = Consumer {
        when (it) {
            HelloThere -> output.accept(GreetingsSaid("Someone said hello!"))
        }
    }
}
```
 
Now let's tell Dagger to use this!

Scroll to the bottom of `GreetingsContainerModule` and replace the `TODO()` block you find there to the object we created:

```kotlin
@GreetingsContainerScope
@Provides
@JvmStatic
internal fun helloWorldOutputConsumer(
    interactor: GreetingsContainerInteractor
) : Consumer<HelloWorld.Output> =
    interactor.helloWorldOutputConsumer
```

"If you can give me `GreetingsContainerInteractor`, I know how to give you a `Consumer<HelloWorld.Output>`". 

And since `GreetingsContainerInteractor` was marked with `@GreetingsContainerScope` in the same DI configuration (check the method named `interactor`), it's treated as a singleton in the scope, making `Dagger` reuse the same exact instance of it when constructing this `Consumer`, instead of creating a new one. 

Now we've established a chain:

`HelloWorldView` =(`Event`)⇒ `ViewEventToOutput` =(`HelloWorld.Output`)⇒ `GreetingsContainerInteractor` =(`GreetingsContainer.Output`)⇒ `RootActivity`

The **tutorial4** app should build and run at this point, and display the Snackbar when the button is pressed.


## Inputs

Let's say we want to update the text on the button from outside of the RIB.

To expose this functionality, let's add an `Input` to `HelloWorld`:

```kotlin
interface HelloWorld : Rib {

    interface Dependency {
        fun helloWorldInput(): ObservableSource<Input> // add this
        fun helloWorldOutput(): Consumer<Output>
        // remainder omitted
    }

    // add this
    sealed class Input {
        data class UpdateButtonText(val text: Text): Input()
    }

    sealed class Output {
        object HelloThere : Output()
    }
    
    // remainder omitted
}    
```

Note that we added a new dependency, too, on `ObservableSource<Input>`. 
 
> Reminder – as we've established in **tutorial1**:
> - if a RIB has `Input`, then it also has a dependency of 
> `ObservableSource<Input>`
> - if a RIB has `Output`, then it also has a dependency of `Consumer<Output>`
> 
> This is so that we can implement the guaranteed auto-wiring functionality of the RIB, described in the **Outline** section at the beginning of this tutorial

Let's use this input - add it to the constructor of our `Interactor` inside `HelloWorldModule`:

```kotlin
@HelloWorldScope
@Provides
@JvmStatic
internal fun interactor(
    savedInstanceState: Bundle?,
    user: User,
    config: HelloWorld.Config,
    input: ObservableSource<HelloWorld.Input>, // add this
    output: Consumer<HelloWorld.Output>
): HelloWorldInteractor =
    HelloWorldInteractor(
        savedInstanceState = savedInstanceState,
        user = user,
        config = config,
        input = input, // add this
        output = output
    )
```

And:

```kotlin
class HelloWorldInteractor(
    savedInstanceState: Bundle?,
    private val user: User,
    private val config: HelloWorld.Config,
    private val input: ObservableSource<HelloWorld.Input>, // add this
    private val output: Consumer<HelloWorld.Output>
)
```

Now let's implement the functionality on the other end, before connecting the dots.

If we want to change the label of the button, we need to go to the view:

```kotlin
interface HelloWorldView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object ButtonClicked : Event()
    }

    data class ViewModel(
        val titleText: Text,
        val welcomeText: Text,
        val buttonText: Text // add this
    )
}
```

And in `HelloWorldViewImpl`:

```kotlin
override fun accept(vm: ViewModel) {
    title.text = vm.titleText.resolve(androidView.context)
    welcome.text = vm.welcomeText.resolve(androidView.context)
    button.text = vm.buttonText.resolve(androidView.context) // add this
}
```

Now we need to update providing a `ViewModel` from the `Interactor`.

We can easily modify the initial one by adding the last line: 

```kotlin
view.accept(
    HelloWorldView.ViewModel(
        titleText = Text.Resource(R.string.hello_world_title, user.name()),
        welcomeText = config.welcomeMessage,
        buttonText = Text.Resource(R.string.hello_world_button_text)
    )
)
```

But this will only create one, and never update it - and we want to do just that, whenever an `Input.UpdateButtonText` arrives.

We can use the `Binder` for that, similarly to how we already use it just a couple lines below the `ViewModel` creation:

```kotlin
viewLifecycle.startStop {
    bind(view to output using ViewEventToOutput)
}
```
   
To outline what we want to do, add a new line here:

```kotlin
viewLifecycle.startStop {
    bind(view to output using ViewEventToOutput)
    bind(input to view using InputToViewModel)
}
```

Create `InputToViewModel` in the `mapper` package:

```kotlin
package com.badoo.ribs.tutorials.tutorial4.rib.hello_world.mapper

import com.badoo.ribs.android.Text
import com.badoo.ribs.tutorials.tutorial4.R
import com.badoo.ribs.tutorials.tutorial4.rib.hello_world.HelloWorld
import com.badoo.ribs.tutorials.tutorial4.rib.hello_world.HelloWorld.Input.UpdateButtonText
import com.badoo.ribs.tutorials.tutorial4.rib.hello_world.HelloWorldView.ViewModel
import com.badoo.ribs.tutorials.tutorial4.util.User

class InputToViewModel(
    private val user: User,
    private val config: HelloWorld.Config
) : (HelloWorld.Input) -> ViewModel? {
    
    override fun invoke(input: HelloWorld.Input): ViewModel? =
        when (input) {
            is UpdateButtonText -> ViewModel(
                titleText = Text.Resource(R.string.hello_world_title, user.name()),
                welcomeText = config.welcomeMessage,
                buttonText = input.text // using the incoming data
            )
        }
}
```
      
Two things worth mentioning here:
1. Notice how the return type is nullable: `ViewModel?` – meaning we don't have to create one if we don't want to. A legitimate use-case is if in the future we decide to add more types of `Input`, and not all of them actually do something with the view – in those branches in the `when` expression we can just return `null`, and `Binder` will not propagate it to the view.

2. Notice that we actually needed to pass `user` and `config` here, so it can no longer be just a Kotlin object. Modify our `Interactor` to create it:

```kotlin
    // create the transformer
    private val inputToViewModel = InputToViewModel(user, config)

    override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        setInitialViewModel(view)
        viewLifecycle.startStop {
            bind(view to output using ViewEventToOutput)
            bind(input to view using inputToViewModel) // use it here
        }
    }

    // extracted this to a method too
    private fun setInitialViewModel(view: HelloWorldView) {
        view.accept(
            HelloWorldView.ViewModel(
                titleText = Text.Resource(R.string.hello_world_title, user.name()),
                welcomeText = config.welcomeMessage,
                buttonText = Text.Resource(R.string.hello_world_button_text)
            )
        )
    }
```

> Alternatively, we could create the transformer in our DI module and pass it in the constructor (then we could spare passing in `user` and `config`). Since we don't need it outside of the `Interactor`, and since it's just a simple mapper (it's not like we'd want to mock it), it's also ok to keep it here. Up to you I guess. 


## Test run

Right now, we only implemented the functionality to react to exposed `Input` type on the `HelloWorld` side of things.

Now we also need to satisfy the dependency of `ObservableSource<Input>` in the parent, which we will do similarly to how we did with `Output` before:

```kotlin

class GreetingsContainerInteractor(
    savedInstanceState: Bundle?,
    router: Router<Configuration, Nothing, Configuration, Nothing, Nothing>,
    output: Consumer<GreetingsContainer.Output>
) : Interactor<Configuration, Configuration, Nothing, Nothing>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = null
) {
    // Add this:
    internal val helloWorldInputSource: Relay<HelloWorld.Input> = BehaviorRelay.create()

    internal val helloWorldOutputConsumer: Consumer<HelloWorld.Output> = Consumer {
        when (it) {
            HelloThere -> output.accept(GreetingsSaid("Someone said hello"))
        }
    }

    // Add this to send an Input immediately when this RIB attaches
    override fun onAttach(ribLifecycle: Lifecycle, savedInstanceState: Bundle?) {
        super.onAttach(ribLifecycle, savedInstanceState)
        helloWorldInputSource.accept(
            HelloWorld.Input.UpdateButtonText(
                Text.Plain("Woo hoo!")
            )
        )
    }
}
```

> Right now we will only provide an `Input` statically and once. We will make it more useful in the next tutorial.

And add it to the bottom of `GreetingsContainerModule` so that Dagger understands to grab this when providing `HelloWorld.Dependency`:

```kotlin
@GreetingsContainerScope
@Provides
@JvmStatic
internal fun helloWorldInputSource(
    interactor: GreetingsContainerInteractor
) : ObservableSource<HelloWorld.Input> =
    interactor.helloWorldInputSource
```

Now we've established a chain:

`GreetingsContainerInteractor` =(`HelloWorld.Input`)⇒`Relay`⇒`InputToViewModel` =(`ViewModel`)⇒ `HelloWorldView`

The app should now compile. Test it!

![](https://i.imgur.com/ISU6yZU.png)


## Tutorial complete
 
Congratulations! You can advance to the next one.


## Summary

**`Inputs` and `Outputs`**

- `Inputs` and `Outputs` are forms of communication with a RIB:
    - `Inputs` expose functionality that can be triggered from the outside
    - `Outputs` signal some events where deciding what to do lies beyond the responsibility of the RIB in question

- Paired dependencies: 
    - `Inputs` always come with a dependency of `ObservableSource<Input>`.
    - `Outputs` always come with a dependency of `Consumer<Output>`.
    - This is to ensure auto-wiring of the RIB, so that as long as you can build it, it works automatically.

- When satisfying `Output` and `Input` dependencies, we **always** want to do that directly in the parent:
    - By satisfying these dependencies, we create a connection between the place where we satisfy them and the RIB in question.
    - Having a certain RIB as a child is an implementation detail of the parent, that should be kept hidden to maintain flexibility.
    - If we were to bubble up the dependency to a higher level, we would expose this implementation detail. We would create a connection between the outside world and the implementation detail, making the immediate parent lose its ability to easily change it. This should be avoided at all costs.
    - If the parent cannot handle an `Output` message directly, it can transform it to its own `Output` type, keeping the implementation detail hidden.

