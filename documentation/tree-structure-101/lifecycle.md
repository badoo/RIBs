# Lifecycle

RIBs use Android-like lifecycle events, though there are some differences.


## Two lifecycles

Any `Node` has two separate lifecycle objects:

1. A main lifecycle for the whole of the `Node`'s existence (business logic)
2. A shorter lived one for the lifecycle of its view (if any)

Both of them are `androidx.lifecycle.Lifecycle`, so their usage should be very similar.


## Practical usage

For client code, it's suggested that you use [Plugins](../basics/plugins.md) to receive any kind of callback from the framework.

For example, `Interactor` (as a class dedicated to hold business logic) extends both `NodeLifecycleAware` and `ViewLifecycleAware`. These are the two methods you will mostly ever need:

```kotlin
    override fun onCreate(nodeLifecycle: Lifecycle) {
        // it's androidx.lifecycle.Lifecycle
    }

    override fun onViewCreated(view: SomeRibView, viewLifecycle: Lifecycle) {
        // it's androidx.lifecycle.Lifecycle
    }
```

In both cases you can just use the `Lifecycle` object to subscribe / scope some operations.


## Android system events

All the above lifecycles are always capped by Android system lifecycle as an upper limit.

For example, when the hosting Activity gets paused, internal lifecycles also get paused if they were resumed, but not change if they were already below that level, e.g.:

```
Node (RESUMED) + Android (PAUSED) = PAUSED
Node (STOPPED) + Android (PAUSED) = STOPPED
```

This also means that if after the above, Android goes back to RESUMED, this will happen:

```
Node (RESUMED) + Android (RESUMED) = RESUMED
Node (STOPPED) + Android (RESUMED) = STOPPED
```

## Further similarity with Android

If a `Node` has a view, but that view gets destroyed, the main lifecycle moves to `STOPPED`. This mimics that in Android, if something is off-screen, it receives an `onStop` callback.


## Lifecycle and back stack

See [Back stack documentation](./back-stack.md)

As said over there, the rule is simple: only the last element and its overlays are on-screen, the other configurations in the back stack are taken off-screen.

For `Nodes` related to these back stack elements this means:

1. Their view will be destroyed
2. Their view lifecycle will be moved to `DESTROYED` state
3. Their node lifecycle will be moved to `STOPPED` state (as if off-screen)

Regardless, any operations you set up for a create-destroy scope will still remain active until the element is completely removed from the back stack. When that happens:

1. The node lifecycle will be moved to `DESTROYED` state
2. The node itself will be destroyed


## More callbacks for plugins

In most cases just relying on the two lifecycle objects should be enough.

However, if you need a more fine-grained control, you can receive additional callbacks by implementing any of these plugin interfaces:

```kotlin
interface NodeLifecycleAware : Plugin {
    fun onBuild() {} // <- Called right after building the Node object, but before it's notified to start its lifecycle 

    fun onCreate(nodeLifecycle: Lifecycle) {}

    fun onAttach() {}

    fun onDestroy() {}
}
```

```kotlin
interface ViewAware<V : RibView> : Plugin {
    fun onViewCreated(view: V, viewLifecycle: Lifecycle) {}
}
```

```kotlin
interface ViewLifecycleAware : Plugin {
    fun onAttachToView() {}

    fun onDetachFromView() {}
}
```
