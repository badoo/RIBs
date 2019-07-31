# Tutorial #1
## The goal of this tutorial
Building and attaching an existing RIB to an integration point. Hello world!


## How an app using RIBs is structured
1. There's one or more integration points in the form of `Activities` / `Fragments` / etc. Ideally the app should be single-Activity, but the library makes no difference about it.
2. Integration points build a `Node` (can be any `Node`), and attach it to themselves, propagating all lifecycle methods to it. This `Node` will be the root. 
3. A tree structure of `Node`s represent the application's business logic state.
4. A tree structure of `View`s represent the application on the view level. This tree is not necessarily the same as the `Node` tree, as `Node`s do not necessarily have their own views, or might be attached to a different place in the view tree (dialogs). We'll see examples about these later.


## How a RIB is structured
Open up the `tutorial1` module, which has a single RIB in the `com.badoo.ribs.tutorials.tutorial1.rib.hello_world` package.

It contains a bunch of classes and packages, where to start?

The immediate go-to when checking out any RIB you are not familiar with, is the **main interface** of the RIB, which serves as the documentation of its public API.

In Badoo RIBs, this main interface always has the name of the RIB itself without any suffix, and you can find it in the root of the package.

An example of such a public interface typically looks like:
```kotlin

interface ExampleRib : Rib {

    interface Dependency : Rib.Dependency {
        fun exampleRibInput(): ObservableSource<Input>
        fun exampleRibOutput(): Consumer<Output>
        // + other dependencies
    }

    sealed class Input {
        // possible Inputs
    }
    
    sealed class Output {
        // possible Outputs
    }

    class Customisation(
        val viewFactory: ViewFactory<HelloWorldView> = inflateOnDemand(
            R.layout.rib_hello_world
        )
    )
}
```


### Dependencies
Here, the `Dependency` interface describes all external dependencies this RIB needs to be built.

RIBs act as a black box that can be plugged in anywhere. This means, that as long as you can provide their dependencies, you can build them, and they are supposed to be auto-wired and ready to go immediately without any further actions.


### Inputs and Outputs

RIBs can also have `Inputs` / `Outputs`. These represent the way we can communicate with them, and they are always defined as part of the public API.

We'll see this in practical examples, but for now, the only things that matter, is that:
- if a RIB has `Input`, then it also has a dependency of `ObservableSource<Input>`
- if a RIB has `Output`, then it also has a dependency of `Consumer<Output>`

This is important to satisfy the plug-and-play functionality of the RIB. Every RIB implementation guarantees that as long as you can give it a source of its `Inputs` that it can observe, and a consumer of its possible `Outputs` that will handle them, it will do all the required wiring internally, and will "just work" out of the box. 


## Getting our hands dirty

Enough theory, let's get down to the practical details, and check out `HelloWorld` interface:

```kotlin
package com.badoo.ribs.tutorials.tutorial1.rib.hello_world

interface HelloWorld : Rib {

    interface Dependency {
        fun helloWorldOutput(): Consumer<Output>
    }

    sealed class Output {
        object HelloThere : Output()
    }

    class Customisation(
        val viewFactory: ViewFactory<HelloWorldView> = inflateOnDemand(
            R.layout.rib_hello_world
        )
    )
}
```

The new thing here is the `Customisation` part, but we'll talk about that in other tutorials. For now, it's enough to understand that this RIB will have a default view inflated from `R.layout.rib_hello_world`.

This RIB does not have any `Inputs`, and it has only one possible `Output`: `HelloThere`. Following from what we said above, this also means that it has a dependency of someone to consume this `Output`.

It has no other dependencies, so as long as we can give it a `Consumer`, we can build it!

Let's take it for a test run!

Open up `RootActivity` in the `tutorial1` module:

```kotlin
/** The tutorial app's single activity */
class RootActivity : RibActivity() {

    /* remainder omitted */

    override fun createRib(savedInstanceState: Bundle?): Node<*> =
        TODO("Create HelloWorldBuilder, supply dependencies in constructor, return built Node")
}
``` 
This `Activity` extends the `RibActivity` class from the library, which is just a convenience class to forward all lifecycle methods to a single root RIB. It has an abstract method `createRib(): Node<*>` which we need to implement here.

To do this, we first need to create an instance of `HelloWorldBuilder`. Opening this class we can see that it indeed needs an instance of the `Dependency` interface defined in the public API of the RIB:

```kotlin
class HelloWorldBuilder(dependency: HelloWorld.Dependency) {
    
    /* remainder omitted */
    
    fun build(savedInstanceState: Bundle?): Node<HelloWorldView> {
        /* remainder omitted */
    }
}
``` 

And also that it has a `build` method which will give us the `Node` object we need to return from the `createRib()` method.

So let's do that!

```kotlin
/** The tutorial app's single activity */
class RootActivity : RibActivity() {

    /* remainder omitted */

    override fun createRib(savedInstanceState: Bundle?): Node<*> =
        HelloWorldBuilder(
            object : HelloWorld.Dependency {
                override fun helloWorldOutput(): Consumer<HelloWorld.Output> = Consumer {
                    // not sure what to do here yet
                }
            }
        ).build(savedInstanceState)
}
```

Now the project should compile. Build it, and we should see this screen:

![](https://i.imgur.com/c2fUEHO.png)

And checking the view hierarchy, this is what we should see:

[![View hierarchy of HelloWorld RIB](https://i.imgur.com/cf2gi9N.png)](https://i.imgur.com/Xkh2XmP.png)

So indeed, that button we see is coming from the RIB we built! Yay!

Now this doesn't do to much. So let's change the `Consumer` dependency that we pass to the `Builder` so that it displays a Snackbar:

```kotlin
override fun createRib(savedInstanceState: Bundle?): Node<*> =
        HelloWorldBuilder(
            object : HelloWorld.Dependency {
                override fun helloWorldOutput(): Consumer<HelloWorld.Output> = Consumer {
                    Snackbar.make(rootViewGroup, "Hello world!", Snackbar.LENGTH_SHORT).show()
                }
            }
        ).build(savedInstanceState)
```

Build and deploy the app, and pressing the button this is what we should see:

![](https://i.imgur.com/F5GEBpw.png)

As promised, beyond providing the required dependencies, we did not need to make any additional wiring - the HelloWorld RIB takes care of the setup that makes the button click invoke the lambda that we passed in.

## 🎉 🎉 🎉 Congratulations  🎉 🎉 🎉
 You just built your first RIB!
 
 You can advance to the next tutorial, or continue reading for some additional understanding of the internals.

## Further reading - internals of the HelloWorld RIB

If we want to understand *why* pressing the button on the UI actually invokes showing the Snackbar, we'll have to check some other classes in the `hello_world` package, too.

### The View

Let's start from the view side, and open up `HelloWorldView`. We see an interface and an implementation of it. Let's start with the interface:

```kotlin
interface HelloWorldView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object ButtonClicked : Event()
    }

    /* remainder omitted */
}
```

Views (as well as other components in RIBs) are reactive. 

This `HelloWorldView` interface states that any implementation of the view is a source of `Events` (here a single one for `ButtonClicked`) and a consumer of `ViewModels` (which we are not using now).


### The View implementation

You can check in the Android view implementation right below the interface how this is done:

```kotlin
class HelloWorldViewImpl( 
    /* remainder omitted */
) {
    /* remainder omitted */
    
    private val button: Button by lazy { findViewById<Button>(R.id.hello_world_button) }

    override fun onFinishInflate() {
        super.onFinishInflate()
        button.setOnClickListener { events.accept(Event.ButtonClicked) }
    }

    /* remainder omitted */
}
``` 

Any click on the button with the id `R.id.hello_world_button` will result in the associated `ButtonClicked` event being published.

The `View` itself never cares for how a certain UI interaction affects anything related to business logic. It only renders `ViewModels`, and triggers `Events` based on the user interaction.

So where is this event being used?


### Connecting the dots

Open up `HelloWorldInteractor`, which is responsible for connecting different parts of business logic:

```kotlin
class HelloWorldInteractor(
    /* remainder omitted */
    private val output: Consumer<HelloWorld.Output>
    /* remainder omitted */
) {

    override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to output using ViewEventToOutput)
        }
    }
}
``` 

The only important parts are that:
1. This class receives `output` in its constructor. This is the object that we required when we declared the RIB's dependencies - the consumer which knows how to react to our `Output` event -, and is passed here via dependency injection. If you are familiar with Dagger, you can check `HelloWorldComponent` and `HelloWorldModule` classes in the `builder` package to see how.

2. The maybe strange looking syntax in onViewCreated is a lifecycle-scoped binding between two reactive endpoints. Here, these two endpoints are the `view` (reactive source of `HelloWorldView.Event`) and the `output` (reactive consumer of HelloWorld.Output), and the scope of the binding is a START-STOP cycle. As the two endpoints have different types, the binding uses a simple mapper `ViewEventToOutput` found in the `mapper` package. In this implementation it provides a simple 1:1 mapping between the two types:

```kotlin
object ViewEventToOutput : (HelloWorldView.Event) -> HelloWorld.Output {
    override fun invoke(event: HelloWorldView.Event): HelloWorld.Output =
        when (event) {
            HelloWorldView.Event.ButtonClicked -> Output.HelloThere
        }
}
```

### Sidenote ###
Badoo RIBs uses [MVICore](https://github.com/badoo/MVICore) both under the hood and in RIB implementations for state machines as well as providing the tools to connect reactive components.

Both the `viewLifecycle.startStop` and the `bind(view to output using ViewEventToOutput)` parts come from the library. You can check out its [documentation](https://github.com/badoo/MVICore) for more information about these topics.


### To sum it up ###
1. The default view is inflated from `R.layout.rib_hello_world` (defined in main interface), which gives us an instance of `HelloWorldViewImpl`
2. `HelloWorldViewImpl` finds a button in the view hierarchy by id
3. An onClickListener is set on it that will trigger publishing an `Event` defined in `HelloWorldView` interface
4. Interactor will:
    1. take `HelloWorldView` as a reactive output
    2. transforms elements coming from it using a mapper
    3. connect this transformed stream of `Output` events to the output consumer (which we got from our dependencies)
    
The result is a clear separation of concerns. View implementation doesn't care what the event will trigger, and even the whole RIB doesn't care what should happen once a certain `Output` is triggered. You can integrate this same RIB in different places, and provide a `Consumer` dependency that shows a Snackbar in one place, and one that shows a Toast in another.

