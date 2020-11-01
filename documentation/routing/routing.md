# Routing

## What's routing?

In simple terms, routing describes the state of local navigation in your tree of Nodes.

This in practice mostly overlaps with which child ```Nodes``` are added to a parent ```Node``` at any given moment.

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

## A sneak peak at routing changes

TODO simple example: set(Configuration.MyProfile)
TODO simple routing source
TODO stateful! needs to be repetable

## Resolving routing

Now that we've defined the possible children to our current ```Node```, it's time to tell the framework what they mean. 

After all, we'll want to manipulate only these simple tokens (the elements of our ```Configuration``` sealed classes), and we'll want the framework to do the rest for us: building, attaching, detaching, destroying corresponding ```Nodes``` for us automatically.

Let's have a brief look at the ```Router``` class in the framework:

```kotlin
abstract class Router<C : Parcelable> // <-- C is our Configuration type
```

```Router``` also implements the ```RoutingResolver``` interface:

```kotlin
interface RoutingResolver<C : Parcelable> {
    fun resolve(routing: Routing<C>): Resolution
}
```

```Router``` doesn't define an implementation for this method (being an ```abstract``` class), so this will be our job in our client code.

It's probably easier to see this in an example!

```kotlin
internal class MainScreenRouter(
    routingSource: RoutingSource<Configuration> = TODO() // <-- we'll talk about this shortly
    /* remainder omitted */
): Router<Configuration>( // <-- this is our sealed class Configuration from above
    routingSource = routingSource
    /* remainder omitted */
) {
    // These are Builders for other RIBs, and are best created via DI.
    // Now kept here for demonstration purposes:
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

**This means, that the framework can ask our client code to construct children, and we can use non-empty arg constructors in a natural and compile-time safe way.**

No ```FragmentFactory``` or other hacks are needed.


## Manipulating routing

TODO cut this
// Now that we've covered how to define and how to resolve possible routings, let's see how to trigger a change in the current routing.
//
// Remember this part of the snippet?
// ```kotlin
// internal class MainScreenRouter(
//     routingSource: RoutingSource<Configuration> = TODO() // <-- we'll talk about this shortly
// ): Router<Configuration>(
//     routingSource = routingSource
// )
// ```
//
// The ```RoutingSource``` type here is our source of information for the ```Router```.
//
// It's something the framework can observe to detect changes in the current routing. In practice, we will want to:
//  1. have an implementation of it passed to our business logic, so that we can manipulate it on one end
//  2. have the same instance passed to our ```Router``` so that it can observe the changes on the other end

See the next chapter for an implementation of the ```RoutingSource``` you might already be familiar with: the [Back stack](backstack.md)

