# Business logic

## Before you proceed

⚠️ Make sure you've read:
- [Plugins](../basics/plugins.md)


## Extra concerns using Plugins

So far we've:
1. Created our RIB's main interface, Node, and Builder in [Hello world!](helloworld.md)
2. Added a [view](view.md)

Let's add some very simple business logic now that will update the view!

As we've seen in [Plugins](../basics/plugins.md), we have a dedicated way of adding extra concerns to our components without polluting our ```Nodes```.


## Adding a Presenter

⚠️ RIBs is agnostic of any patterns that you want to use. Here we'll create a simple Presenter only for the sake of demonstration, as MVP is a widely known and simple pattern – not because there's anything inherently important to it.


Let's create ```HelloWorldPresenter```:

```kotlin
internal class HelloWorldPresenter(
    private val greeting: String
) : ViewAware<HelloWorldView> {

    override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
        view.setText(greeting)
    }
}
```

Let's update our ```HelloWorldNode``` so that it can receives a list of ```Plugins```:

```kotlin
internal class HelloWorldNode(
    viewFactory: ((RibView) -> HelloWorldView?)?,
    plugins: List<Plugin> // <--
) : Node<HelloWorldView>(
    buildParams = BuildParams.Empty(),
    viewFactory = viewFactory,
    plugins = plugins // <--
), HelloWorld {

    // Also let's remove this, this is no longer needed:
    // override fun onCreate() {
    //     super.onCreate()
    //     Log.d("RIBs", "Hello world!")
    // }
}
```

Finally, let's pass our presenter as a plugin to ```HelloWorldNode``` in ```HelloWorldBuilder```:

```kotlin
class HelloWorldBuilder(
    private val dependency: HelloWorld.Dependency
) : SimpleBuilder<HelloWorld>() {

    override fun build(buildParams: BuildParams<Nothing?>): HelloWorld {
        val presenter = HelloWorldPresenter(greeting = "Hello ${dependency.name}!") // <--

        return HelloWorldNode(
            viewFactory = HelloWorldViewImpl.Factory().invoke(deps = null),
            plugins = listOf(presenter) // <--
        )
    }
}
```

## That's it!

Launching the app, you should now see the greeting on the screen.

