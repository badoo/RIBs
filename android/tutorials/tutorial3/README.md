# Tutorial #3

## The goal of this tutorial

Satisfying a dependency of a RIB directly from its parent


## Starting out

Compiling and launching **tutorial3**, this is what we see:

![](https://i.imgur.com/Ja45nRm.png)

We will provide text to the second placeholder first manually, then as a dependency.

(We will handle the first placeholder in the next tutorial) 

Have a brief look at the `Lexem` interface found in this tutorial, as we will use it in the example:

```kotlin
interface Lexem {

    fun resolve(context: Context): String

    data class Text(val text: String) : Lexem {
        override fun resolve(context: Context): String =
            text
    }

    data class Resource(@StringRes val resId: Int) : Lexem {
        override fun resolve(context: Context): String =
            context.getString(resId)
    }
}
```

It provides us with an abstraction over textual information, and two simple implementations – one for actual Strings, the other for String resources. 

This is a useful approach for cases when you want to set a text from resource, but at the place of definition you don't have access to a `Context` that Android can provide, only later. Hence the `resolve(context: Context)` method.  


## A welcoming message

Let's start with the `HelloWorldView` interface. Its `ViewModel` contains a dummy integer field only:

```kotlin
data class ViewModel(
    val i: Int = 0
)
```

Let's change that so that we will give a `Lexem` to the view to render instead:

```kotlin
data class ViewModel(
    val welcomeText: Lexem
)
```

Let's scroll down to the Android view implementation. Notice how it already finds a reference to the welcome text view:
```kotlin
private val welcome: TextView by lazy { findViewById<TextView>(R.id.hello_world_welcome) }
```

So let's implement the `ViewModel` rendering:
```kotlin
override fun accept(vm: ViewModel) {
    welcome.text = vm.welcomeText.resolve(context)
}
```

Notice how we added `welcomeText` as a `Lexem`, which we could now resolve using the `Context` our view has access to.


## Feeding the view with data

Let's head to our Interactor in this RIB, `HelloWorldInteractor`, where we should put business logic.

We'll find an empty block, where we can put all view-related business logic, tied to the lifecycle of the view:

```kotlin
override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
    super.onViewCreated(view, viewLifecycle)
}
```

Just to try out what we did, we could do something like this to test out the parts we just wrote in `HelloWorldView`:

```kotlin
override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
    super.onViewCreated(view, viewLifecycle)
    view.accept(
        HelloWorldView.ViewModel(
            welcomeText = Lexem.Text("Does this work at all?")
        )
    )
}
```

Launch the app to verify that it indeed works.

![](https://i.imgur.com/maHH0pn.png)

Alright, now instead of manually fixing this, let's try and make this a dependency! 

Let's head to `HelloWorld`, the main interface of the RIB, which doesn't have any dependencies now:

```kotlin
interface HelloWorld : Rib {

    interface Dependency    
    
    // ...
}
```

Change it so that it looks like:

```kotlin
interface HelloWorld : Rib {

    interface Dependency {
        fun config(): Configuration
    }

    // It's a good idea to group all "simple data" dependencies into a Configuration 
    // object, instead of directly adding them to Dependency interface:
    data class Configuration(
        val welcomeMessage: Lexem
    )
    
    // ...
}    
```

Now if we try to build the project, Dagger will fail us, as we do not yet actually provide this dependency:

```
[Dagger/MissingBinding] com.badoo.ribs.tutorials.tutorial3.rib.hello_world.HelloWorld.Configuration cannot be provided without an @Inject constructor or an @Provides-annotated method.
public abstract interface GreetingsContainerComponent extends com.badoo.ribs.tutorials.tutorial3.rib.hello_world.HelloWorld.Dependency
```

## Provide dependency directly in the parent

In this case, our dependency is a simple setup configuration, so we could just satisfy it directly in the parent. 

Let's head to `GreetingsContainerModule`, the place where we add all `@Provides` configuration on the container level, and add this block to the bottom:

```kotlin
@GreetingsContainerScope
@Provides
@JvmStatic
internal fun helloWorldConfig(): HelloWorld.Configuration =
    HelloWorld.Configuration(
        welcomeMessage = Lexem.Resource(
            R.string.hello_world_welcome_text
        )
    )
```

Now the app should build, as we provide the dependency - other than that, nothing changed, since we are not using this configuration yet.

Let's correct that, and make use of it in the child `Interactor`.

Add the config to the constructor, and use it to construct the initial `ViewModel`:

```kotlin
class HelloWorldInteractor(
    private val config: HelloWorld.Configuration, // add this
    router: // ... remainder omitted
) {

    override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        view.accept(
            HelloWorldView.ViewModel(
                welcomeText = config.welcomeMessage // use it
            )
        )
    }
}

```

And change the actual construction of the Interactor in `HelloWorldModule`:

```kotlin
@HelloWorldScope
@Provides
@JvmStatic
internal fun interactor(
    config: HelloWorld.Configuration, // add this, Dagger will provide it automatically
    router: HelloWorldRouter
): HelloWorldInteractor =
    HelloWorldInteractor(
        config = config, // pass it to the Interactor 
        router = router
    )
```

Try it out, it should now display the text we passed in as a resource:

![](https://i.imgur.com/HTSayPD.png)


## Tutorial complete

Congratulations! You can advance to the next one.


## Summary

#### Adding new data to display in the view
1. Add new widget to xml
2. Find view by id and store reference in `ViewImpl`
3. Define new field in `ViewModel`
4. Modify `accept(vm: ViewModel)` method in `ViewImpl` to actually display data
5. In case of initial `ViewModel`: pass it from `Interactor`'s `onViewCreated` method (we'll cover dynamically changing data later)


#### Adding dependency to child from parent

1. Add it to child's `Dependency` interface
2. In parent's `Module` class, add new `@Provides` annotated block
3. Use dependency in child where it's needed
    1. e.g. add it to constructor of `Interactor`
    2. go to child's `Module` / provides function
    3. add it as a parameter to `@Provides` function - this will be provided by Dagger
    4. use it in constructing the object, e.g. the `Interactor`

