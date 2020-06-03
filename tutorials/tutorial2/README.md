# Tutorial #2

## The goal of this tutorial
To add a child RIB to another RIB


## Outline
Building on top of what we achieved in **tutorial1**, we have our `HelloWorld` RIB with its single button. 

So far, we directly attached it to our integration point, `RootActivity`, but now we want to see an example how to add it as a child RIB of another.


## Viewless RIBs

In the framework, individual RIBs are not forced to implement a view, they can also be purely business logic driven. In this case, they only add a `Node` to the `Node` tree, but no `View` to the View tree.

Of course, they can do everything else, and can have child RIBs all the same. If these children have their own `Views`, those will be attached directly to the `View` of the closest parent up in the tree that has one. 


## Starting out

Check the `com.badoo.ribs.tutorials.tutorial2.rib` package in the `tutorial2` module, you now see two RIBs: `HelloWorld`, and `GreetingsContainer`, the new one.
  
If you check `RootActivity`, it builds and attaches `GreetingsContainer` directly, and `HelloWorld` is not used anywhere (yet).

The project should compile without any changes, and if you launch the app, this is what you should see:

![Empty greetings container](https://i.imgur.com/4XmUpqF.png) 

Not too much to see here yet. `GreetingsContainer` is a viewless container RIB, so it does not add anything to the screen. We will add functionality to it in later tutorials, right now the only thing we want to do is to attach `HelloWorld` to it as its child.

To do that, first we need to familiarise ourselves with the concept of routing.


## Routing

Parent - child relationships and their dynamic changes are described under the concept of routing.

In case of Badoo RIBs, routing is done via these steps:
1. define the set of possible configurations a certain RIB can be in
2. define what a certain configuration means, by resolving it to a routing action
3. set the initial configuration for the Router
4. *(dynamically manipulate the Router to set it into any of the pre-defined configurations, based on business logic - in later tutorials)* 


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
abstract fun resolveConfiguration(configuration: C): RoutingAction
```

The library offers implementations of the `RoutingAction` interface, which should cover all cases you encounter.

For simplicity, we will now only look at one of them: the `AttachRibRoutingAction`, which, as the name implies, tells the `Router` to attach a certain child RIB.


## Getting our hands dirty
Let's have a look at routing for the `GreetingsContainer`:

```kotlin

class GreetingsContainerRouter(
    savedInstanceState: Bundle?
): Router</* ... */>(
    savedInstanceState = savedInstanceState,
    initialConfiguration = Configuration.Default
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
    }

    override fun resolve(routing: RoutingElement<Configuration>): RoutingAction =
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

override fun resolve(routing: RoutingElement<Configuration>): RoutingAction =
    when (routing.configuration) {
        is Configuration.HelloWorld -> attach { TODO() }
    }
```

> ⚠️ Notice how `AttachRibRoutingAction` also offers a convenience method `attach` to construct it, for which you can use static imports. The same goes for other `RoutingActions`, too.

> ⚠️ Also notice that even though `Configuration.HelloWorld` is a Kotlin object, there's still an `is` for it in the `when` expression. This is actually important, since after save/restore cycle, the instance restored from `Bundle` will be a different instance! Without using `is`, the `when` expression will not match in that case.

Alright, but what to put in place of the `TODO()` statement?

Looking at the signature of the `attach` method it needs a lambda that can build another `Node` given a nullable `Bundle` (representing `savedInstanceState`):

```kotlin
fun attach(builder: (Bundle?) -> Node<*>): RoutingAction
```

## We need a Builder

To build the `HelloWorld` RIB, we need an instance its Builder: `HelloWorldBuilder`, just as we did in **tutorial1**. So first just add it as a constructor dependency to our Router:

```kotlin
class GreetingsContainerRouter(
    private val helloWorldBuilder: HelloWorldBuilder
) /* rest is the same */
```

We'll care about how to pass it here just in a moment, but first let's finish our job here, and replace the `TODO()` block in our `attach` block:

```kotlin
override fun resolve(routing: RoutingElement<Configuration>): RoutingAction =
    when (routing.configuration) {
        is Configuration.HelloWorld -> attach { helloWorldBuilder.build(it) }
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

> For simplicity, we removed all actual dependencies from `HelloWorld.Dependency` interface, so that we can attach it as easily as possible.
>
> Don't worry, we will add it back in the next tutorial!


## Test run!

The application should now compile, and launching it, this is what we should see:

![](https://i.imgur.com/iuarP6N.png)

It's not very different from what we've seen in **tutorial1**, but now it's added as a child RIB of `GreetingsContainer`.

> Note that because we removed the `Consumer<Output>` dependency from `HelloWorld`, the button now doesn't do anything when pressed. We'll fix that soon! 


## 🎉 🎉 🎉 Congratulations  🎉 🎉 🎉
 
 You can advance to the next tutorial!


## Summary

Steps to add a child RIB:
1. Go to Router
    1. Define configuration
    2. Resolve configuration to `attach { childBuilder.build(it) }` routing action
    3. Define `childBuilder` as a constructor dependency 
2. Go to Dagger module
    1. When constructing the `Router`, create and pass in an instance of the required child `Builder`
    2. Add `component` as a parameter to `Builder` constructor
    3. Let our component extend the required `Dependency` interface
    4. *(Actually satisfy those dependencies - coming up in next tutorial)*
