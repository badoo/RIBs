# Routing

## What's routing?

In simple terms, routing describes the state of local navigation in your tree of Nodes.

In practice, this mostly overlaps with which child ```Nodes``` are added to a parent ```Node``` at any given moment.

The combination of these local, stateful navigation concerns uniquely describe the state of your whole application.


## The difficult parts are taken care of for you

Routing is both stateful and persistent, automatically handled by the framework.

You only need to care about defining, resolving, and manipulating your possible routings.


## Defining possible routing configurations

The core idea: every ```Node``` in your application structure might have a _finite_ set of possible child ```Nodes``` that it will ever add, based on the business requirements. Let's try to describe a currently active child with a "label", so that we can refer to them more easily. Let's call this label a ```Configuration```.

For example, let's say you have a main screen and a bottom menu with three options: ```My profile```, ```Activity feed```, ```Messages```. Pressing any of them would show the corresponding screen in the content area. So this ```Node``` of ours that serves as the container to them can have only these options as its possible children:

 ```kotlin
 sealed class Configuration {
   object MyProfile : Configuration()
   object ActivityFeed : Configuration()
   object Messages : Configuration()
 }
 ```

Imagine that by "activating" any of these labels, the corresponding ```Node``` gets built and attached to our current container ```Node```! (We'll see shortly how to do that in practice)

At this point, we can repeat the same idea on any of the activated children too!

 For example, imagine that we activated the ```Messages``` screen, and there we have have a list / detail view container – so that it's either the list, or the detail view that this container will ever show.

 We know this already when designing our app, and any new option will only arise by the change of business requirements. This means we can represent this in code to cover all the possibilities:

```kotlin
sealed class Configuration {
  object List : Configuration()
  data class DetailView(itemId: Int) : Configuration()
}
```

## Goal: triggering routing changes


What we want to achieve is that we can say something like this in code:

```kotlin
router.set(Configuration.MyProfile)
```

Or even manipulate the routing with a back stack:
```kotlin
router.push(Screen1)
router.push(Screen2)
router.pop()
```

ℹ️ _Note: actual operations can be different. We'll see this in the next chapters._

Also, we want this to be stateful, so that the framework persists and restores the state of routing (along with its history!) for us.

It would be a lot more difficult to achieve this without these simple "labels" that we declare in our ```Configuration``` sealed classes! Thankfully, with a slight change we can make them Parcelable:

 ```kotlin
 sealed class Configuration : Parcelable {
   @Parcelize object MyProfile : Configuration()
   @Parcelize object ActivityFeed : Configuration()
   @Parcelize object Messages : Configuration()
 }
 ```

Great! Now the framework can persist and restore a simple list (or other data structure) of them easily. But how should the framework understand what each of them represent?


## Resolving routing

Let's have a brief look at the ```Router``` class in the framework:

```kotlin
abstract class Router<C : Parcelable> // <-- C is our Configuration type
```

```Router``` implements the ```RoutingResolver``` interface:

```kotlin
interface RoutingResolver<C : Parcelable> {
    fun resolve(routing: Routing<C>): Resolution
}
```

```Router``` doesn't define an implementation for this method (being an ```abstract``` class), so this will be our job in our client code.

Let's see an example!

```kotlin
internal class MainScreenRouter(
    /* remainder omitted */
): Router<Configuration>( // <-- this is our sealed class Configuration from above
    /* remainder omitted */
) {
    // These are Builders for other RIBs, and are best created via DI.
    // Now kept here for demonstration purposes only:
    private val myProfileBuilder: MyProfileBuilder = TODO()
    private val activityFeedBuilder: ActivityFeedBuilder = TODO()
    private val messagesBuilder: MessagesBuilder = TODO()

    // The key part:
    override fun resolve(routing: Routing<Configuration>): Resolution =
        when (routing.configuration) {
            MyProfile -> child { myProfileBuilder.build(it) }
            ActivityFeed -> child { activityFeedBuilder.build(it) }
            Messages -> child { messagesBuilder.build(it) }
        }
}
```

What's happening here?
- The ```resolve``` method describes the resolution of a current ```Routing``` in a declarative way
- Here we say what child we want attached in each of the possible branches
- Whatever RIB is built by the ```Builder``` instances inside the ```child { }``` lambdas, is treated as a child ```Node``` by the framework, and will be automatically attached / detached when needed
- ```child``` is defined in the ```companion object``` of ```ChildResolution```, and is statically imported for brevity's sake
- ```ChildResolution``` (and some others) come from the framework – most probably you won't ever need to write your own ones

ℹ️ Noticed the ```it``` implicit lambda parameter? It's an instance of ```BuildContext``` passed by the framework. It contains ancestry info ("who's my parent?"), a savedInstanceState ```Bundle``` among other things. You should always pass this ```it``` object to your builders. See more about this in [Build context]()


## A parallel with Fragments

We can draw a parallel here with ```Fragments```: you could add nested ```Fragments``` to each other, much the same way as you can can a nested ```Node``` structure.

The key difference is that while you can add _any_ ```Fragment``` to a ```FragmentManager```, using the above approach to routing, you will only have a pre-defined, finite set of possible children to any ```Node```.

This is actually a powerful trade-off!

On the one hand, you can always define the whole possible set of routings based on the business requirements, so giving up the "anything goes" dynamism of ```Fragments``` isn't a real loss.

On the other hand, since now there's a finite set, we can cover all the possibilities with exhaustive Kotlin ```when``` expressions, and can do the construction ourselves.

**This means, that the framework can expect our client code to be able to construct all children, and we can use non-empty arg constructors in a natural and compile-time safe way.**

No ```FragmentFactory``` or other hacks are needed.

