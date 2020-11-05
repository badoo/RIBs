# Routing in practice: defining and resolving

## Configurations

Routing in RIBs is both stateful and persistent, and the framework does the heavy lifting for us. 

What we'll need to do is to describe our routing using labels, and tell the framework what each of them mean; then we're free to manipulate routing using these labels only.

Let's call them ```Configurations```.


## Defining possible routing configurations

Let's see the example of the main screen we had previously:

[screenshot – card swiping game]()

We said we modeled this with a structure like:

[diagram – container]()

Let's imagine that all of these screens are already implemented as standalone ```Nodes```, which can be built using their corresponding ```Builders```.

What we want is the ```Container``` to decide when any of them should be active, in which case they will be built and attached to it.

Let's cover them each with a ```Configuration``` first:

 ```kotlin
 sealed class Configuration {
   object MyProfile : Configuration()
   object CardSwipingGame : Configuration()
   object Messages : Configuration()
 }
 ```

At this point, we can repeat the same idea on any of the activated children too! 

Going one level deeper in the tree, if we activated the ```Messages``` screen, we have these options that we can express as ```Configurations```:

```kotlin
sealed class Configuration {
  object ConversationList : Configuration()
  data class Chat(id: Int) : Configuration()
}
```


## Goal: triggering routing changes

What we want to achieve is that we can say something like this in code:

```kotlin
router.set(MyProfile)
```

Or even manipulate the routing with a back stack:
```kotlin
router.push(CardSwipingGame)
router.push(Messages)
router.pop()
```

ℹ️ _Note: actual operations can be different. We'll see this in the next chapters._

Also, we want this to be stateful, so that the framework persists and restores the state of routing (along with its history!) for us.

It would be a lot more difficult to achieve this without these simple labels that we declare in our ```Configuration``` sealed classes! Thankfully, with a slight change we can make them Parcelable:

 ```kotlin
 sealed class Configuration : Parcelable {
   @Parcelize object MyProfile : Configuration()
   @Parcelize object CardSwipingGame : Configuration()
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
    private val cardSwipingGameBuilder: ActivityFeedBuilder = TODO()
    private val messagesBuilder: MessagesBuilder = TODO()

    // The key part:
    override fun resolve(routing: Routing<Configuration>): Resolution =
        when (routing.configuration) {
            MyProfile -> child { myProfileBuilder.build(it) }
            CardSwipingGame -> child { cardSwipingGameBuilder.build(it) }
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

