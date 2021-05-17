# Plugins

## Before you proceed

⚠️ Make sure you've read:
- [Nodes](nodes.md)


## Keeping extra concerns out of Node

```Nodes``` are meant to be simple structural elements, and should be kept lean.

To keep the framework agnostic of any specific approach / pattern you want to use, there aren't any fixed parts. Rather, the ```Node``` offers an extension point using ```Plugins``` in its constructor:

```kotlin
open class Node<V : RibView>(
    val buildParams: BuildParams<*>,
    private val viewFactory: ((RibView) -> V?)?,
    plugins: List<Plugin> = emptyList() // <--
)
```

So what is a ```Plugin```?

A ```Plugin``` is an empty interface extended by many actual ones:

```kotlin
interface Plugin

```

## Plugins

### RIBs lifecycle related plugins

Your business logic might be interested in the lifecycle events of the ```Node``` or the view:
```kotlin
interface NodeLifecycleAware : Plugin {
    fun onBuild() {}

    fun onCreate(nodeLifecycle: Lifecycle) {} // <-- Android lifecycle

    fun onAttach() {}

    fun onDestroy() {}
}
```

```kotlin
interface ViewAware<V : RibView> : Plugin {
    fun onViewCreated(view: V, viewLifecycle: Lifecycle) {} // <-- Android lifecycle
}
```

```kotlin
interface ViewLifecycleAware : Plugin {
    fun onAttachToView() {}

    fun onDetachFromView() {}
}
```

### Tree structure changes

Working in a tree structure, you might want to react to events related to children and their views:

```kotlin
interface SubtreeChangeAware : Plugin {
    fun onChildBuilt(child: Node<*>) {}

    fun onChildAttached(child: Node<*>) {}

    fun onChildDetached(child: Node<*>) {}
}
```

```kotlin
interface SubtreeViewChangeAware : Plugin {
    fun onAttachedChildView(child: Node<*>) {}

    fun onDetachedChildView(child: Node<*>) {}
}
```

### Android related plugins

```kotlin
interface AndroidLifecycleAware : Plugin {
    fun onStart() {}

    fun onStop() {}

    fun onResume() {}

    fun onPause() {}
}
```

```kotlin
interface BackPressHandler : Plugin {

    fun handleBackPress(): Boolean =
        false
}
```

```kotlin
interface SavesInstanceState : Plugin {
    fun onSaveInstanceState(outState: Bundle) {}
}
```

```kotlin
interface SystemAware : Plugin {
    fun onLowMemory() {}
}
```

### Component level plugins

Sometimes you need to grab a reference to the component as a whole, either as an interface, or its implementation, the ```Node```.

This will come especially handy in [Workflows](../tree-structure-101/deep-link-workflows.md)


```kotlin
interface RibAware<T : Rib> : Plugin {
    val rib: T

    fun init(rib: T) {}
}

```

```kotlin
interface NodeAware : Plugin {
    val node: Node<*>

    fun init(node: Node<*>) {}
}
```

There are helper classes found in the library, so you don't have to implement the above interfaces, you can just use delegation:

```kotlin
class SomeClass(
    private val nodeAware: NodeAware = NodeAwareImpl()
) : NodeAware by nodeAware {

    fun foo() {
        // [node] is an automatically available property coming from the NodeAware interface
        // the reference is automatically set for you by the framework + the NodeAwareImpl class
        // so you can use it right away:
        node.doSomething()
    }
}
```

⚠️ Note: the reference to ```node``` is set by ```Node``` automatically, and isn't available immediately after constructing your object, but only after the construction of the ```Node``` itself.


```kotlin
class SomeClass<T : Rib>(
    private val ribAware: RibAware<T> = RibAwareImpl()
) : RibAware<T> by ribAware {


    fun foo() {
        // [rib] is an automatically available property coming from the RibAware<T> interface
        // the reference is automatically set for you by the framework + the RibAwareImpl class
        // so you can use it right away:
        rib.doSomething() // doSomething is any method defined in your main interface
    }
}
```

⚠️ Note: the reference to ```rib``` is set by ```Builder```, and isn't available immediately after constructing your object, but only after completing the ```.build()``` method. 


## Using Plugins 

All plugins are designed to have empty ```{}``` default implementations (or other sensible defaults when a return value is defined), so it's convenient to implement them only if you need.

Don't forget to pass your ```Plugins``` to your ```Node```:

```kotlin
internal class MyNode(
    // ...
    plugins: List<Plugins> = emptyList()
    // ...
) : Node<Nothing>(
    // ...
    plugins = plugins
    // ...
)
```

⚠️ Note: ```plugins``` is a ```List```, as the order matters here. All ```Plugin``` instances are invoked in the order they appear in the list.


## Default plugins

Whenever creating your root, you can also choose to inject a list of default plugins. Any plugin you pass here will be automatically added to all your `Nodes`:

```kotlin
override fun createRib(savedInstanceState: Bundle?) =
    YourRootBuilder(
        object : YourRoot.Dependency {
            // ...
        }
    ).build(root(
        savedInstanceState = savedInstanceState,
        defaultPlugins = { node ->
            // TODO list default plugins
            //  node can be used for conditional checks e.g if (node.isRoot)
        }
    ))
```

This way you can add plugins from the application level without having to change the individual modules. 

Also see [Tooling](../extras/tooling.md) for some utility plugins you might find useful. 


## Suggested read

Check the tutorial found in the ```Hello world!``` chapters to see this topic implemented in practice.
