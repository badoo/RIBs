# Deep link workflows

Workflows allow you to expose a public API for your `Rib`, and compile them into a chain of operations on the application level. This way you should be able to set your application into any possible state with a fine granularity, while keeping the individual operations local to any `Rib`.

## Reactive operations

Operations are best defined using the reactive approach of your choice, as this way it will be easier:

- to chain them
- to introduce waiting for some operation to happen (e.g. user to log in) before continuing the workflow

The below examples use RxJava2 APIs, but feel free to use anything else.


## Good candidates for operations

- If you have a container that can attach different children, you could expose triggering related configuration
- If you have internal 


## Defining operations

Add operations in the main interface:

```kotlin
interface SomeContainer : Rib {

    interface Dependency {
        // ...
    }

    class Customisation(
        // ...
    ) : RibCustomisation

    // Workflow operations
    // 1.
    fun doSomeOperationAndStayOnThisNode(): Single<SomeContainer>
    // 2.
    fun attachChild1(): Single<Child1>
    fun attachChild2(): Single<Child2>
    // 3.
    fun waitForChild3(): Single<Child3>
}
```

These operations are to be implemented in the `Node`, and can be roughly grouped into 3 types:

1. Operations that work on the current `Node` (set it into some state)
2. Operations that manipulate routing under the hood so that a specific child gets attached
2. Operations that will wait for a specific child to get attached (not guaranteed to ever happen though!)

Also note how the return types in the above operations each refer to either the current `Rib`'s interface or that of another child – this allows chaining the operations between different `Rib`s and compile them into a single workflow.


## Implementing operations

The place to implement these operations is your `Node`, which implements the `Rib` interface.

You can also choose to extend the `RxWorkflowNode` class (found in the `rib-rx2` dependency) offers some helpful methods to build on top of:

- `executeWorkflow {}` executes the given lambda, and returns `Single<T>` where `T` is the current `Rib`
- `attachWorkflow {}` executes the given lambda, and returns `Single<T>` where `T` is the expected child `Rib`'s interface. Note that the lambda is responsible for triggering any operation (e.g. manipulating routing) that will cause the child to be attached, otherwise an `IllegalStateException` will be thrown.
- `waitForChildAttached {}` returns `Single<T>` where `T` is the expected child `Rib`'s interface. It will wait until the certain child type is attached (if ever!), or continue immediately if it's already attached. This is an extremely useful operation when you need the user to authenticate themselves before continuing a workflow that builds on top of an authenticated state.

```kotlin
class SomeNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<SomeView>?,
    plugins: List<Plugin>,
    private val backStack: BackStack<Configuration>
) : RxWorkflowNode<SomeView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Switcher {

    override fun doSomethingAndStayOnThisNode(): Single<SomeRib> =
        executeWorkflow {
            // TODO: Execute any internal operation
            // someObject.foo()
        }
    
    override fun attachChild1(): Single<Child1> =
        attachWorkflow {
            // TODO Manipulate routing:
            backStack.push(Content.Child1)
        }

    override fun attachChild2(): Single<Child2> =
        attachWorkflow {
            // TODO Manipulate routing:
            backStack.push(Content.Child2)
        }

    override fun waitForChild3(): Single<Child3> =
        waitForChildAttached<Child3>()
    
}
```

## Chaining operations into workflows

The place to do them is in your root `Activity`. Even though the operations are defined separately in each `Rib` (and possibly in very different modules) here you can nicely compile them together into meaningful chains of actions:


```kotlin
private lateinit var workflowRoot: YourRoot // set it when building your root
val disposables = CompositeDisposable()

/**
 * Attaches a series of RIBs and executes an operation on Child1
 */
fun executeAWorkflow() {
    disposables += Single.just(workflowRoot)
        .flatMap { attachSomeContainer() } // operation provided by YourRoot
        .flatMap { attachChild1() }        // operation provided by SomeContainer
        .flatMap { someOperation() }       // operation provided by Child1
        .subscribe {}
}

```


## Launching workflows from deep links

**1. Define a deep link intent**

Add an `intent-filter` In your `AndroidManifest.xml`:

```xml
<activity android:name=".RootActivity">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />

        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>

    <intent-filter>
        <action android:name="android.intent.action.VIEW"/>
        <category android:name="android.intent.category.DEFAULT"/>
        <category android:name="android.intent.category.BROWSABLE"/>
        <data android:scheme="workflow-example"/>
    </intent-filter>
</activity>
```

**2. Handle the intent in your Activity**

You can, of course, have any number of workflows:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (intent?.action == Intent.ACTION_VIEW) {
        handleDeepLink(intent)
    }
}

fun handleDeepLink(intent: Intent) {
    when {
        // Corresponds to URI: "workflow-example://workflow1"
        (intent.data?.host == "workflow1") -> executeWorkflow1()

        // Corresponds to URI: "workflow-example://workflow2"
        (intent.data?.host == "workflow2") -> executeWorkflow2()
        
        // Feel free to use query parameters un URIs too to add some dynamism
    }
}
```

## Launching workflows from console

You can launch any workflow from console:

```
adb shell am start -a "android.intent.action.VIEW" -d "workflow-example://workflow1"
```

## Launching workflows from the debug panel 

You can use the `DebugControls` plugin provided by the framework to setup a contextual debug panel for your RIBs. It's a useful idea to place buttons in this debug panel that each trigger a workflow operation, so you can set your RIBs into any desired state easily.
