# Tutorial #2

## The goal of this tutorial
To add a child RIB to another RIB


## Outline
Building on top of what we achieved in **tutorial1**, we have our `HelloWorld` RIB with its single button. 

So far, we directly attached it to our integration point, `RootActivity`, but now we want to see an example how to add it as a child RIB of another.


## Starting out

Check the `com.badoo.ribs.tutorials.tutorial2.rib` package in the `tutorial2` module, you now see two RIBs: `HelloWorld`, and `GreetingsContainer`, the new one.

If you check `RootActivity`, it builds and attaches `GreetingsContainer` directly, and `HelloWorld` is not used anywhere (yet).

The project should compile without any changes, and if you launch the app, this is what you should see:

If you compile and launch, this is what you should see:

![Empty greetings container](https://i.imgur.com/2zs5wiq.png) 

Not too much to see here yet. Our goal is to display the `HelloWorld` RIB in the place of the colorful area.

To do that, first we need to familiarise ourselves with the concept of routing.


## Routing

Parent-child relationships and their dynamic changes are described under the concept of routing.

In case of Badoo RIBs, routing is done via these three steps:
1. define the set of possible configurations a certain RIB can be in
2. define what a certain configuration means, by resolving it to a routing action
3. manipulating the Router to set it into any of the pre-defined configurations, based on business logic


## What is a configuration?

A configuration is just a label used to describe the routing state.

For example, imagine a `Root` RIB, which, on a very basic level, can have two possible subtrees attached: one if the user is logged out, and another if the user is logged in. But not both at the same time!

In this case, `LoggedOut` and `LoggedIn` would be the two possible configurations, which we could define as a Kotlin sealed class:

```kotlin
sealed class Configuration : Parcelable {
    @Parcelize object LoggedOut : Configuration()
    @Parcelize object LoggedIn : Configuration()
}
```

> ⚠️ Note how it implements `Parcelable`, so that the framework can save/restore our configurations later.

## What is a routing action?

Once we defined our possible configurations, we need to say *what* should happen when any of them is activated.

This is done by implementing the `resolveConfiguration` method in the `Router`:

```kotlin
abstract fun resolveConfiguration(configuration: C): RoutingAction<V>
```

> ⚠️ This method is important, so that the action is repeatable by the framework when restoring a configuration - that's why we marked those as `Parcelable` in the first place!

The library offers a `RoutingAction` interface and some implementations of it. 

For simplicity, we will now only look at one of them: the `AttachRibRoutingAction`, which, as the name implies, tells the `Router` to attach a certain child RIB.


## Getting our hands dirty
Let's have a look at routing for the `GreetingsContainer`:

```kotlin
class GreetingsContainerRouter: Router</* ... */>(
    initialConfiguration = Configuration.Default
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<GreetingsContainerView> =
        RoutingAction.noop()
}
```

This looks pretty basic:
1. The possible set of configurations contain only one: `Default` 
2. `GreetingsContainerRouter` passes in `Configuration.Default` as `initialConfiguration` in the parent constructor. All `Routers` need to state their initial configurations. 
3. When resolving configurations, we always return `noop()` as a `RoutingAction`, doing nothing

Let's spice it up a bit!

First, rename `Default` to describe what we actually want to do:

```kotlin
sealed class Configuration : Parcelable {
    @Parcelize object HelloWorld : Configuration()
}
```

Next, change the resolution so that it attaches something:

```kotlin
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach

/* ... */

override fun resolveConfiguration(configuration: Configuration): RoutingAction<GreetingsContainerView> =
    when (configuration) {
        Configuration.HelloWorld -> attach { TODO() }
    }
```

> ⚠️ Notice how `AttachRibRoutingAction` also offers a convenience method `attach` to construct it, for which you can use static imports. The same goes for other `RoutingActions`, too.

Alright, but what to put in place of the `TODO()` statement?

Looking at the signature of the `attach` method it needs a lambda that can build another `Node`:

```kotlin
fun attach(builder: () -> Node<*>): RoutingAction
```

## We need a Builder

To build the `HelloWorld` RIB, we need an instance its Builder: `HelloWorldBuilder`, just as we did in **tutorial1**. So first just add it as a constructor dependency to our Router:

```kotlin
class GreetingsContainerRouter(
    private val helloWorldBuilder: HelloWorldBuilder
): Router</* ... */>(
    initialConfiguration = Configuration.HelloWorld
) {
    /* ... */
}
```

We'll care about it in a moment how to pass it here, but let's finish our job here, and replace the `TODO()` block in our `attach` block:

```kotlin
override fun resolveConfiguration(configuration: Configuration): RoutingAction<GreetingsContainerView> =
    when (configuration) {
        Configuration.HelloWorld -> attach { helloWorldBuilder.build() }
    }
```

Our job is done here in the `GreetingsContainerRouter`, let's see how we can provide the Builder we need.


## The dependency chain

Badoo RIBs relies on Dagger2 to provide dependencies. In the `builder` subpackage, let's open `GreetingsContainerModule`, and notice how the first method constructs the Router:

```kotlin
@GreetingsContainerScope
@Provides
@JvmStatic
internal fun router(
    // pass component to child rib builders, or remove if there are none
    component: GreetingsContainerComponent
): GreetingsContainerRouter =
        GreetingsContainerRouter()
```

Notice how the constructor invocation has a compilation error now, since we don't yet pass in the required dependency. Let's do that, and change the constructor invocation to:

```kotlin
GreetingsContainerRouter(
    helloWorldBuilder = HelloWorldBuilder()
)
```

Now it's the `HelloWorldBuilder()` part that's missing something. If we open it up, we get a reminder that in order to construct it, we need to satisfy the `HelloWorld` RIB's dependencies. 

> ⚠️ When constructing child RIBs, you always want the parent RIB to satisfy those dependencies automatically - that is, never just construct them manually (as we did at the integration point)

Notice how the function has an unused parameter we can use:

```kotlin
internal fun router(
    // pass component to child rib builders, or remove if there are none
    component: GreetingsContainerComponent
)
```

Let's do that!
   
```kotlin
GreetingsContainerRouter(
    helloWorldBuilder = HelloWorldBuilder(component)
)
```

Notice the compilation error again:

![](https://imgur.com/0Kk27dp.png)

That's fine and expected! We need to make the connection and say that `HelloWorldComponent` should actually provide those dependencies:

![](https://i.imgur.com/yyJklDY.png)

If this dialog opens up automatically in Android Studio, cancel it:

![](https://i.imgur.com/VSUCpJe.png)

It reminds us that it's not enough to *say* we provide those dependencies, we should actually do so. If we forgot it, and build the project right now, Dagger will provide a reminder and fail the build:

```
e: /*...*/GreetingsContainerComponent.java:8: error:
[Dagger/MissingBinding]
io.reactivex.functions.Consumer<com.badoo.ribs.tutorials.tutorial2.rib.hello_world.HelloWorld.Output> 
cannot be provided without an @Provides-annotated method.
```

Bear with me, we're almost there!


## Satisfying dependencies for Inputs / Outputs of children 

Communicating with child RIBs, i.e. reacting to their outputs or feeding them inputs belongs to the responsibilities of the `Interactor`. 

Open up `GreetingsContainerInteractor`, and add this Consumer implementation:

```kotlin
class GreetingsContainerInteractor(
    router: Router</* ... */>,
    output: Consumer<GreetingsContainer.Output>
) : Interactor<Configuration, Configuration, Nothing, GreetingsContainerView>(
    router = router,
    disposables = null
) {
    internal val helloWorldOutputConsumer: Consumer<HelloWorld.Output> = Consumer {
        when (it) {
            HelloThere -> output.accept(GreetingsSaid("Hello there :)"))
        }
    }
}
```

Notice what we did there: whenever the child RIB triggers `HelloWorld` output (its only possible output this time), we translate it to *our* own output, `GreetingsSaid` with a string inside. If we check `RootActivity` in **tutorial2**, it's similar to the one we implemented in **tutorial1**, and display a Snackbar to display the provided string.


> Is this to say you should always forward all outputs to a higher level?
> 
> Far from it!
> 
> The rule of a thumb is:
> 1. Can you definitely decide how to react to the Output? Then implement the action right there in the `Interactor`
> 2. Do you want to add some flexibility, and make it an implementation detail what to do with it? Forward it higher.
> 
> The second option is useful in multi-application cases, when he same RIB should behave differently when a certain action is triggered.
 
There's one last thing to do, tell Dagger to grab this Consumer, so that it can provide it as a dependency for the child. Open up `GreetingsContainerModule` again, and add this to the bottom: 

```
@GreetingsContainerScope
@Provides
@JvmStatic
internal fun helloWorldOutputConsumer(
    interactor: GreetingsContainerInteractor
) : Consumer<HelloWorld.Output> =
    interactor.helloWorldOutputConsumer
```

## Test run!

The application shoud now compile, and launching it, this is what we should see:

![](https://i.imgur.com/xqLppHn.png)

Well, this is awesome! But there's a small problem with it.

## View targeting

[TODO] correct imports, as Configuration name and RIB name collides :|

## Summary
Steps to add child and listen to its output:
1. Go to Router
    1. Define configuration
    2. Resolve configuration to `attach { childBuilder.build() }` routing action
    3. Pass the required builder as a constructor dependency 
2. Go to Dagger module
    1. Add builder to Router constructor
    2. Add component to Builder constructor
    3. Let component extend the required Dependency interface
3. Go to Interactor
    1. Implement Consumer<Output>, do something with it
4. Go back to Dagger module
    1. Add method to grab consumer from `Interactor`


This is important, so that the action is repeatable by the framework.
