# Tutorial #3

## The goal of this tutorial

To see how to provide dependencies to a RIB


## Starting out

Compiling and launching **tutorial3**, this is what we see:

![](https://i.imgur.com/Ja45nRm.png)

We will provide text to the placeholders first manually, then as a dependency.

Have a brief look at the `Text` interface found in the library, as we will use it in the example:

```kotlin
package com.badoo.ribs.android

import android.content.Context

/**
 * An abstraction over text, so that you can provide it from different sources.
 *
 * In case the default implementations are not good enough, feel free to implement your own.
 */
interface Text {

    fun resolve(context: Context): String

    class Plain(private val string: String): Text {
        override fun resolve(context: Context): String =
            string
    }

    class Resource(private val resId: Int, private vararg val formatArgs: Any) : Text {
        override fun resolve(context: Context): String =
            context.resources.getString(resId, formatArgs)
    }
}

```

It provides us with an abstraction over textual information, and two simple implementations – one for actual Strings, the other for String resources. 

This is a useful approach for cases when you want to set a text from resource, but at the place of definition you don't have access to a `Context` that Android can provide, only later. Hence the `resolve(context: Context)` method.  


## A welcoming message

Let's start with the second placeholder first!

Check the `HelloWorldView` interface. Its `ViewModel` contains a dummy integer field only:

```kotlin
data class ViewModel(
    val i: Int = 0
)
```

Let's change that so that we will give a `Text` to the view to render instead:

```kotlin
data class ViewModel(
    val welcomeText: Text
)
```

Let's scroll down to the Android view implementation. Notice how it already finds a reference to the welcome text view:
```kotlin
private val welcome: TextView = androidView.findViewById(R.id.hello_world_welcome)
```

So let's implement the `ViewModel` rendering:
```kotlin
override fun accept(vm: ViewModel) {
    welcome.text = vm.welcomeText.resolve(androidView.context)
}
```

Notice how we added `welcomeText` as a `Text`, which we could now resolve using the `Context` our view has access to.


## Feeding the view with data

Let's head to our Interactor in this RIB, `HelloWorldInteractor`, where we should put business logic.

We'll find an empty block, where we can put all view-related business logic, tied to the lifecycle of the view:

```kotlin
override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
    super.onViewCreated(view, viewLifecycle)
}
```

Just to try out what we did, we could do something like this to test out the parts we just implemented in `HelloWorldView`:

```kotlin
override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
    super.onViewCreated(view, viewLifecycle)
    view.accept(initialViewModel)
}

private val initialViewModel =
    HelloWorldView.ViewModel(
        welcomeText = Text.Plain("Does this work at all?")
    )
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
        fun config(): Config
    }

    // It's a good idea to group all "simple data" dependencies into a Config 
    // object, instead of directly adding them to Dependency interface:
    data class Config(
        val welcomeMessage: Text
    )
    
    // ...
}    
```

Now if we try to build the project, Dagger will fail us, as we do not yet actually provide this dependency:

```
[Dagger/MissingBinding] com.badoo.ribs.tutorials.tutorial3.rib.hello_world.HelloWorld.Config cannot be provided without an @Inject constructor or an @Provides-annotated method.
public abstract interface GreetingsContainerComponent extends com.badoo.ribs.tutorials.tutorial3.rib.hello_world.HelloWorld.Dependency
```

## Provide dependency directly in the parent

In this case, our dependency is simple configuration data, so we could just satisfy it directly in the parent. 

Let's head to `GreetingsContainerModule`, the place where we add all the `@Provides` DI definitions on the container level, and add this block to the bottom:

```kotlin
@GreetingsContainerScope
@Provides
@JvmStatic
internal fun helloWorldConfig(): HelloWorld.Config =
    HelloWorld.Config(
        welcomeMessage = Text.Resource(
            R.string.hello_world_welcome_text
        )
    )
```

Now the app should build, as we provide the dependency - other than that, nothing changed, since we are not using this config yet.

Let's correct that, and make use of it in the child `Interactor`.

Add the config to the constructor, and use it to construct the initial `ViewModel`:

```kotlin
class HelloWorldInteractor(
    config: HelloWorld.Config, // add this
    router: // ... remainder omitted
) {

    override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        view.accept(initialViewModel)
    }

    private val initialViewModel =
        HelloWorldView.ViewModel(
            welcomeText = config.welcomeMessage // use it
        )
}

```

And change the actual construction of the Interactor in `HelloWorldModule`:

```kotlin
@HelloWorldScope
@Provides
@JvmStatic
internal fun interactor(
    config: HelloWorld.Config, // add this, Dagger will provide it automatically
    router: HelloWorldRouter
): HelloWorldInteractor =
    HelloWorldInteractor(
        config = config, // pass it to the Interactor 
        router = router
    )
```

Try it out, it should now display the text we passed in as a resource:

![](https://i.imgur.com/HTSayPD.png)


## Section summary

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


## Next task: greet the user

We are still left with a placeholder text to be filled with text.

Let's imagine this application has some kind of `User` object representing the current logged in user, and we want this placeholder to greet the user like: **"Hello User!"**, with their actual name.

(There's a `User` interface included in this module, check it out)

Obviously, the `HelloWorld` RIB cannot fill in the name of the user, and will need an instance of `User` as a dependency.
 
 In the case of the welcome text, we could provide the dependency directly. Problem is, finding an instance of `User` is not the responsibility of `HelloWorld`, but it's also not the responsibility of the parent RIB, the `GreetingsContainer`.

So in this case, we will need to bubble up this dependency until at some level we can grab an instance of the current `User`, and pass it down.

(As we do not have proper logged out / logged in handling yet, we wil just pass in a dummy User object from the Activity level)

## Test your knowledge!

Based on the previous sections, you should be able to:
1. Add a new field to `HelloWorldViewImpl` that holds a reference to the `TextView` for the other placeholder, found in `rib_hello_world.xml` with the id `@+id/hello_world_title`
2. Add a new field to `HelloWorldView.ViewModel`, named `titleText`, type `Text`
3. Set the text of the `TextView` by resolving the `Text` from `titleText` whenever the `ViewModel` is rendered in `HelloWorldViewImpl`
4. Set a fixed value for this field from `HelloWorldInteractor` just to test it out.

Now let's make it more dynamic. You should also be able to:
1. Add an instance of `user: User` as a constructor dependency to `HelloWorldInteractor`
2. Use `user.name()` to construct: `Text.Resource(R.string.hello_world_title, user.name())`, and pass it as the value for `titleText` when creating the `ViewModel`
3. Add the instance of `User` as a dependency for the creation of `HelloWorldInteractor` in the respective `@Provides` function in `HelloWorldModule` 
4. Add `User` to `HelloWorld.Dependency` interface to say that `HelloWorld` RIB needs this from the outside

If you feel stuck, you can refer to the previous sections for help, or have a peek at [solutions](solutions.md).

Building the project at this point, Dagger should give you:  

`Dagger/MissingBinding] com.badoo.ribs.tutorials.tutorial3.util.User cannot be provided without an @Provides-annotated method.`


## Bubbling up dependencies

This will be super easy, if you got this far. The only difference is that if we cannot provide a dependency directly, we also add it to the `Dependency` interface of parent. We can keep doing this further until on some level we can actually provide it.

Really, just a one-liner.

Try it:

```kotlin
interface GreetingsContainer : Rib {

    interface Dependency {
        // Add this, and you are done on this level - Dagger will provide
        // it further down to children automatically:
        fun user(): User 
        fun greetingsContainerOutput(): Consumer<Output>
    }

    // ... remainder omitted
}
```
At this point we reached the root level. `RootActivity` creates an anonymous object actually providing dependencies to `GreetingsContainer`, so we have a compilation error there until we actually implement the newly added `user()` method:
 
```kotlin
/** The tutorial app's single activity */
class RootActivity : RibActivity() {

    // ... remainder omitted
    
    override fun createRib(savedInstanceState: Bundle?): Node<*> =
        GreetingsContainerBuilder(
            object : GreetingsContainer.Dependency {
                // add this block:
                override fun user(): User =
                    User.DUMMY

                override fun greetingsContainerOutput(): Consumer<GreetingsContainer.Output> =
                
                // ... remainder omitted
            }
        ).build(savedInstanceState)
}
```

The application should now build, and this is what you should see when launching:

![](https://i.imgur.com/4i2lQ1o.png)

## Tutorial complete

Congratulations! You can advance to the next one.
