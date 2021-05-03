# Tooling

RIBs comes with some debug utils you might find useful.

Don't forget to add a dependency on:

```groovy
implementation 'com.github.badoo.RIBs:rib-debug-utils:{latest-version}'
```


## Utility classes

### TreePrinter

Offers an API to print your `Node` subtree to console:

```
* RootNode
    └── * PortalNode
        ├── LoggedInContainerNode
        │   └── FeedContainerNode
        │       └── PhotoFeedNode
        └── * PhotoDetailsNode
```

`*` marks those `Nodes` that are currently active on screen (attached to view)


## Plugins

Some utility plugins you might find useful to pass as `defaultPlugins` to your `Nodes` when in `DEBUG` mode (See [Plugins](../basics/plugins.md) for details)


### Logger

Implements all other plugin interfaces so it receives callbacks for all of them - then logs debug messages for the current RIB:

```
D/Rib Logger: com.badoo.ribs.portal.RxPortalNode@aa10ca7: onBuild
D/Rib Logger: com.badoo.ribs.portal.RxPortalNode@aa10ca7: onCreate
D/Rib Logger: com.badoo.ribs.sandbox.rib.switcher.SwitcherNode@67fc73e: onBuild
D/Rib Logger: com.badoo.ribs.portal.RxPortalNode@aa10ca7: onChildBuilt: com.badoo.ribs.sandbox.rib.switcher.SwitcherNode@67fc73e
D/Rib Logger: com.badoo.ribs.sandbox.rib.switcher.SwitcherNode@67fc73e: onCreate
D/Rib Logger: com.badoo.ribs.sandbox.rib.foo_bar.FooBarNode@d5c1aec: onBuild
D/Rib Logger: com.badoo.ribs.sandbox.rib.switcher.SwitcherNode@67fc73e: onChildBuilt: com.badoo.ribs.sandbox.rib.foo_bar.FooBarNode@d5c1aec
D/Rib Logger: com.badoo.ribs.sandbox.rib.menu.MenuNode@69b6131: onBuild
D/Rib Logger: com.badoo.ribs.sandbox.rib.switcher.SwitcherNode@67fc73e: onChildBuilt: com.badoo.ribs.sandbox.rib.menu.MenuNode@69b6131
D/Rib Logger: com.badoo.ribs.sandbox.rib.foo_bar.FooBarNode@d5c1aec: onCreate
D/Rib Logger: com.badoo.ribs.sandbox.rib.switcher.SwitcherNode@67fc73e: onChildAttached: com.badoo.ribs.sandbox.rib.foo_bar.FooBarNode@d5c1aec
D/Rib Logger: com.badoo.ribs.sandbox.rib.menu.MenuNode@69b6131: onCreate
D/Rib Logger: com.badoo.ribs.sandbox.rib.switcher.SwitcherNode@67fc73e: onChildAttached: com.badoo.ribs.sandbox.rib.menu.MenuNode@69b6131
D/Rib Logger: com.badoo.ribs.portal.RxPortalNode@aa10ca7: onChildAttached: com.badoo.ribs.sandbox.rib.switcher.SwitcherNode@67fc73e
D/Rib Logger: com.badoo.ribs.sandbox.rib.foo_bar.FooBarNode@d5c1aec: onStart
D/Rib Logger: com.badoo.ribs.sandbox.rib.menu.MenuNode@69b6131: onStart
D/Rib Logger: com.badoo.ribs.sandbox.rib.switcher.SwitcherNode@67fc73e: onStart
D/Rib Logger: com.badoo.ribs.portal.RxPortalNode@aa10ca7: onStart
```

Example setup:

```kotlin
private fun createLogger(): Plugin = Logger(
    log = { rib, event ->
        Log.d("Rib Logger", "$rib: $event")
    }
)
```

### LeakDetector

Integrates memory leak detection using the tool of your choice. Watches `view` detach and `node` destroy.

Example setup for LeakCanary:

```kotlin
private fun createLeakDetector(): Plugin = LeakDetector(
    watcher = { obj, msg ->
        AppWatcher.objectWatcher.watch(obj, msg)
    }
)
```


### DebugControls

You can create debug UI panels to manipulate your RIBs.

**1. Add a single `DebugControlsHost` as plugin to your root**

```kotlin
private fun createDebugControlHost(): Plugin =
    DebugControlsHost(
        viewGroupForChildren = { findViewById(R.id.debug_controls_host) },
        growthDirection = GrowthDirection.BOTTOM,
        defaultTreePrinterFormat = TreePrinter.FORMAT_SIMPLE
    )
```

It's suggested to add a `DrawerLayout` to your app when running in debug mode, and set `viewGroupForChildren` to point to some `ViewGroup` inside the drawer (e.g. a vertical `LinearLayout`).


**2. Create `DebugControls` for each Rib**

```kotlin
class SomeRibDebugControls : DebugControls<SomeRib>(
    label = "Hello world",
    viewFactory = { it.inflate(R.layout.debug_somerib) }
) {
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onDebugViewCreated(debugView: View) {
        debugView.findViewById<Button>(R.id.workflow_hello).setOnClickListener {
            Toast.makeText(it.context, "Hello", Toast.LENGTH_SHORT).show()

            disposables.add(
                // You could trigger workflow operations from here
                rib.someWorkflowOperation().subscribe()
            )
        }
    }

    override fun onDebugViewDestroyed(debugView: View) {
        super.onDebugViewDestroyed(debugView)
        disposables.dispose()
    }
}
```
