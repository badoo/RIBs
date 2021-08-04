# Adding a view

## Before you proceed

⚠️ Make sure you've read:
- [Hello world!](helloworld.md)

⚠️ Reminder

These pages describe a step-by-step approach to building up a RIB for better understanding. 

In real life circumstances, you can save a lot of time by using the IntelliJ template generator plugin you can find in this repo, and you won't have to write all of this yourself.

## View lifecycle

Nodes can be in a state with or without their views on the screen (similar to Fragments).

As ```Nodes``` are added to / restored from a back stack (or other screen history construct), the framework automatically destroys / recreates their views.

To do this for you, a ```Node``` will need a reference to a view factory.


## ViewFactory

Let's have a look at the constructor of ```Node```:

```kotlin
open class Node<V : RibView>(
    // ...
    private val viewFactory: ((RibView) -> V?)?,
    // ...
)
```

What's happening here:
- ```Node``` has a type: ```V``` for its view
- There's an optional ```viewFactory``` argument: passing in null is saying "This RIB doesn't have a view"
- When not null, it should be in the format of ```(RibView) -> V?```
- The ```(RibView)``` part refers to the parent view, and is provided by the framework
- The ```viewFactory``` should return an (optional) instance of ```V``` when invoked

If this looks complicated, don't worry, it's much simpler in practice. Let's see!

## Create a simple layout

Let's create ```rib_hello_world.xml``` with a simple text field:

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/rib_hello_world"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Let's have a drink in the Foo Bar!" />

</FrameLayout>
```

(RIBs support ```Jetpack Compose``` too, more on that later)

## Create the View interface

Let's create ```HelloWorldView```:

```kotlin
interface HelloWorldView : RibView {

    interface Factory : ViewFactory<Nothing?, HelloWorldView>

	fun setText(text: String)
}
```

It's a good idea to hide our view implementation behind an interface. This will make it easier to:
- replace it with customisations
- replace it with fake ones in tests
- add operations / define UI events / view models


Here ```Factory``` extends ```ViewFactory```, which is defined as:

```kotlin
interface ViewFactory<Dependency, View>: (Dependency) -> (RibView) -> View
```

And provides dependency resolution. In our case however, there are no dependencies for our view, so we'll use ```Nothing?```.

We've also defined a single operation on our view: ```fun setText(text: String)```. 


## Create the View implementation

Let's create ```HelloWorldViewImpl```:

```kotlin
class HelloWorldViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    HelloWorldView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_hello_world
    ) : HelloWorldView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> HelloWorldView = {
            HelloWorldViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

    private val textView: TextView = androidView.findViewById(R.id.text)

    override fun setText(text: String) {
        textView.text = text
    }
}
```

What's happening here:
- We extend the ```AndroidRibView``` class. (There's an equivalent for ```Jetpack Compose``` based views too)
- We implement our view's interface
- We define a ```Factory``` as an implementation of ```HelloWorldView.Factory```
- By default we inflate the ```R.layout.rib_hello_world``` layout (point of customisation)
- When invoked
  - we receive an instance of our defined dependencies (here ```Nothing?```)
  - we return the lambda with ```HelloWorldViewImpl``` as a result


## Pass the ViewFactory to your Node

Update ```HelloWorldNode```:

```kotlin
internal class HelloWorldNode(
    viewFactory: ((RibView) -> HelloWorldView?)? // <-- 
) : Node<HelloWorldView>( // <-- Note that we now set the view type here
    buildParams = BuildParams.Empty(),
    viewFactory = viewFactory // <-- 
), HelloWorld {

    override fun onCreate() {
        super.onCreate()
        Log.d("RIBs","Hello world!")
    }
}
```

Also update ```HelloWorldBuilder```:

```kotlin
class HelloWorldBuilder(
    private val dependency: HelloWorld.Dependency
) : SimpleBuilder<HelloWorld>() {

    override fun build(buildParams: BuildParams<Nothing?>): HelloWorld =
        HelloWorldNode(
            // Note how we are using the view implementation's factory, not that of the interface!
            // We invoke it with its dependencies (null):
            viewFactory = HelloWorldViewImpl.Factory().invoke(deps = null)
        )
}
```

If we wish, we can also pass a different layout to the constructor of ```HelloWorldViewImpl.Factory()```, as long as it has the same ```TextView``` with the same ```id``` that ```HelloWorldViewImpl``` is looking for. This way we can customise the look & feel of the view.

Later we'll see how to automate this using [Customisations].


## Test run

Launching the application you should now see the minimalistic UI with a single text: ```Let's have a drink in the Foo Bar!```

This is a bit static now, however. We'll add some business logic to it in the next chapter.


