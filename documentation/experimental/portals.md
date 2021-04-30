# Portals

Portals provide mechanics to open a new screen while remaining in the same tree.

⚠️ Portals are an experimental feature


## Problem definition

Let's imagine this app structure:
```
app
└── A
    ├── B
    │   └── D
    │   └── E
    │
    └── C
```

Here, `A` is a container that has control over the entire screen space.

`A` divides this available space two two parts: one to host `B`, the other to host `C`. As a consequence, these children have now control over only a portion of the screen.

Repeating this pattern, the screen space available to any `Node` can only get smaller and smaller. The `Nodes` themselves shouldn't care about this of course.

But what happens when we would like to show something full screen?

For example, let's say `E` has a button, which we would like to trigger opening a new screen (marked here with `X`):

```
app
└── A
    ├── B
    │   └── D
    │   └── E
    |       └── X
    │
    └── C
```

`X` is "owned" by `E` just the same as any other parent-child relationships in the tree: resolving dependencies, and orchestrating communication is the responsibility of the parent.

But following from the division of screen space, `X` could only be hosted in the screen space available to `E`, which is only a portion of the full screen.

## Approach 1: another Activity

We could of course launch another Activity, which would host `X` as its own root. However, by doing this, we would also lose all benefits of a single-Activity, single-tree approach, and we would also make it more difficult to have communication between `E` and `X`. 

- `E` is no longer scoping `X`
- `E` has a lot more difficult way of sharing state and dependencies with `X`
- `E` has a lot more difficult way of communicating with `X` if it needs to


## Approach 2: Adding directly to A

As `A` is the first (and here also the last) level in the tree which has access to the full screen space, we would need to add `X` to `A` instead:

```
app
└── A
    ├── B
    │   └── D
    │   └── E
    │
    └── C
    └── X
```

The first problems with this are similar to `Approach #1`:

- `E` is no longer scoping `X`
- `E` has a lot more difficult way of sharing state and dependencies with `X`
- `E` has a lot more difficult way of communicating with `X` if it needs to

Furthermore:
- `E` is no longer responsible for creating `X`, and we add this responsibility at the wrong place (`A`) 

Imagine what happens if we add more full screen elements with this approach:

```
app
└── A
    ├── B
    │   └── D
    │   └── E
    │
    └── C
    └── X1
    └── X3
    └── ...
    └── XN
```

`A` will soon become bloated with lots of extra concerns and dependencies that logically it shouldn't have to care about. And more than just an inconvenience, this becomes 

Remember: we're tried adding these `Nodes` here only to show them on full screen - and not because `A` has anything to do with them. Let's try to avoid it!
 

## Approach 3: Portals

Portals allow you to solve the above problem by separating the logical tree structure from the view tree structure. You can keep the logical tree structure as is, while allowing the child to be attached to a higher level in the view tree.

Let's see an example by introducing an additional level into our tree structure:

```
app
└── Portal
    └── A
        ├── B
        │   └── D
        │   └── E
        |       └── X
        │
        └── C
```

`Portal` can be added anywhere - but it's most useful somewhere close to the root of our tree, where the available screen space hasn't been divided yet.

What we'll want to do is to separate the logical and the visual tree structures:
- keep the above logical structure for business logic
- but in effect, attach `X` as a child of `Portal` in the view tree, as if it was actually organised like this:

```
app
└── Portal
    └── X
```

This way, `X` will render itself on full screen.


## Full screen content, full screen overlays

You can achieve full screen rendering for both "Content" and "Overlay" configurations. See [back stack documentation](../tree-structure-101/back-stack.md) for what these terms means.  


## Setup

Add the dependency:

```groovy
implementation 'com.github.badoo.RIBs:rib-portal:{latest-version}'
```

Optionally, you can depend on the RxJava version of it, which provides some workflow operations:

```groovy
implementation 'com.github.badoo.RIBs:rib-portal-rx:{latest-version}'
```

To use portals, you will want to add it as a dependency in your tree:

```kotlin
interface SomeRib : Rib {
    
    interface Dependency : CanProvidePortal {
        // ...
    }
}
```

This is all you need so far. But if you're curious what this means, `CanProvidePortal` is actually:
```kotlin
@ExperimentalApi
interface CanProvidePortal {

    val portal: Portal.OtherSide
}
```

And `Portal.Otherside` is what gives us the operations we need:

```kotlin
@ExperimentalApi
interface Portal : Rib {

    @ExperimentalApi
    interface OtherSide {
        fun showContent(remoteNode: Node<*>, remoteConfiguration: Parcelable)
        fun showOverlay(remoteNode: Node<*>, remoteConfiguration: Parcelable)
    }
}
```

This is provided by the framework, you won't need to implement it.


## Add a portal to your tree

In the simplest case, you want `Portal` to be your actual root, and add yours as a child to it:
```kotlin
override fun createRib(savedInstanceState: Bundle?) =
    PortalBuilder( // or RxPortalBuilder
        object : Portal.Dependency {
            override val defaultResolution: (Portal.OtherSide) -> Resolution =
                { portal -> child { buildYourRib(portal, it) } }
            
            private fun buildYourRib(
                portal: Portal.OtherSide,
                buildContext: BuildContext
            ): YourRib {
                return YourRibBuilder(
                    object : YourRib.Dependency {
                        // ...
                        override val portal: Portal.OtherSide = portal
                        // ...
                    }
                ).build(buildContext)
            }
        }
    ).build(root(savedInstanceState))
```

You'll also want to propagate this dependency down your whole tree, so that you can interact with the portal on any level.


## Using portals
Define some routing configurations first. It's helpful to place them in a nested sealed class:

```kotlin
sealed class Configuration : Parcelable {
    sealed class Content : Configuration() {
        // only if you have any - these are meant to be displayed the regular way
    }
    sealed class Overlay : Configuration() {
        // only if you have any - these are meant to be displayed the regular way
    }
    sealed class FullScreen : Configuration() {
        @Parcelize object X : FullScreen()
    }
}
```

Add something to the portal from your business logic. Here we are using an `Interactor`, but feel free to use any other architectural pattern of course:

```kotlin
// Built in SomeRibBuilder:
class SomeInteractor(
    // ...
    portal: Portal.OtherSide // Coming from SomeRib.Dependency 
) : Interactor<Small, SmallView>(
    buildParams = buildParams
) {
    
    fun businessLogic() {
        portal.showContent(node, FullScreen.X) 
        // or:
        portal.showOverlay(node, FullScreen.X)
    }
}
```

Notice how the methods take 2 params: the first refers to the current `Node` this `Interactor` belongs to.

Note: here `node` is available because `Interactor` (base class provided by the framework) provides it automatically. If you're using a different pattern / class, you can still access `node` either directly through the `NodeAware` or indirectly through the `RibAware` plugins. See [Plugins](../basics/plugins.md) for more details.

The last bit we need to put in place is resolving our configuration in our `Router`:

```kotlin
override fun resolve(routing: Routing<Configuration>): Resolution =
    when (routing.configuration) {
        FullScreen.X -> remoteChild(anchor = node) { xBuilder.build(it) }
    }
```
Notice how we don't use a `child { }` resolution here, but `remoteChild` with an additional parameter, `anchor`. This is an important detail. Note again the usage of the current `Node`, which `Router` automatically has a reference to.

The importance of these `Node` references is that it provides a way for `Portal` to backtrack where `X` is coming from. `Portal` itself is agnostic of the children it hosts, and delegates their resolution to the logical parent (`E` in the original scheme / `SomeRib` in the code examples).
