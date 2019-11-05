# Tutorial #5

## The goal of this tutorial

Understanding how to dynamically switch between child RIBs


## Starting out

![](https://i.imgur.com/TIfGFtw.png)

Launch the **tutorial5** app, and you can see that there's a new button in the layout: **MORE OPTIONS**. 

If you look at the project structure, you can also find a new RIB in this module: `OptionSelector`. 

All it does is it renders a screen with text options and a confirm button. We will use that to update the button text in our `HelloWorld` rib, and also use it for the actual greeting shown in the Snackbar.

![](https://i.imgur.com/hz7ZXlQ.png)


## Hiererachy of multi-child RIBs

We had this hiearachy so far:

```
GreetingsContainer
└── HelloWorld
```

And now we'll add `OptionsSelector` as the second child of `GreetingsContainer`:

```
GreetingsContainer
├── HelloWorld
└── OptionsSelector
```

The idea how they will work together:

1. On `HelloWorld` screen, User presses **MORE OPTIONS**. Since it is beyond the responsibility of `HelloWorld` RIB, it reports it as `Output`
2. `GreetingsContainer` catches the output, and switches its routing from `HelloWorld` to `OptionsSelector`. Since we display the container on the whole screen, this results in a "new screen" effect.
3. `OptionsSelector` offers UI interaction to select something from a radio group. What should happen when a certain options is selected is beyond its responsibilities, so similarly as with `HelloWorld`, it reports it as `Output`.
4. `GreetingsContainer` catches the output, switches back its routing to `HelloWorld` again.
5. The text of the main button in `HelloWorld` should be updated to reflect the newly selected option. This can be done by via an `Input` command to `HelloWorld` which allows setting of the text from outside of the RIB.
 

## Test your knowledge

By now you should be able to:
1. Trigger a new event from the UI that reaches the parent as `Output`
    1. Add a new element to `Output` in `HelloWorld` called `ShowMoreOptions`
    2. Add a new element to `Event` in `HelloWorldView` called `MoreOptionsButtonClicked`
    3. In `HelloWorldView`, find the Button with `R.id.hello_world_more_options`, and set a click listener that will publish `MoreOptionsButtonClicked`
    4. Add the transformation between `Event` and `Output` in the `ViewEventToOutput` transformer
    5. React to this new output in `GreetingsContainerInteractor`. Leave the actual implementation a `TODO()`
2. Add `OptionSelector` RIB as a child of `GreetingsContainer`. This involves:
    1. a new Configuration added to its `GreetingsContainerRouter`
    2. resolving it to an `attach { moreOptionsBuilder.build() }` action
    3. providing `moreOptionsBuilder` to the `GreetingsContainerRouter`
 
For help with the above tasks, you can refer to:
- **tutorial1** / **Further reading** section on how to make a Button trigger an `Output`
- **tutorial2** / **Summary** section on how to add a child RIB to a parent
- **tutorial4** on commnunication with child RIBs, i.e. `Inputs` / `Outputs`


## Implement routing

Now that our new Button can signal the correct `Output`, and now that our container's Router can build the other RIB we need, the only thing we need is to connect the dots.

Business logic triggers routing:
1. in `GreetingsContainerInteractor` we consume the `Output` of `HelloWorld`
2. in the `when` branch for the new `Output` (where we added a `TODO()`) we want to tell `GreetingsContainerRouter` to switch to the Configuration representing `OptionSelector` RIB.

All we need to do is:

```kotlin
class GreetingsContainerInteractor
    // ...

    internal val helloWorldOutputConsumer: Consumer<HelloWorld.Output> = Consumer {
        when (it) {
            HelloThere -> output.accept(GreetingsSaid("Someone said hello"))
            ShowMoreOptions -> router.push(Configuration.MoreOptions)
        }
    }
}
```

Pressing the **MORE OPTIONS** button the app should display the new screen:

![](https://i.imgur.com/s0Udusa.png)

Try it!
 
Right now the only way of getting back to `HelloWorld` is to press back on the device. We'll address that soon.


## Explanation: the back stack

Why did the above work?

All `Routers` have a routing back stack. By default, this back stack has a single element:

```
back stack = [(default configuration)]
```

This is the one you set in your `Router`. In `GreetingsContainerRouter` this reads:

```kotlin
initialConfiguration = Configuration.HelloWorld
```

So our default back stack was in fact:
```
back stack = [*Configuration.HelloWorld] 
```

A simplified rule of the back stack is that the last configuration is active (in simple terms: it's on screen). We'll mark this with an asterisk (*) from now on.

`Router` offers you operations to manipulate this back stack.

```kotlin
fun push(configuration: Content)

fun replace(configuration: Content)

fun newRoot(configuration: Content)

fun popBackStack(): Boolean
```

We'll deal with other operations later, now what's important is that `push` adds a new element to the back stack.

So when we did ```router.push(Configuration.MoreOptions)```, this happened:

```
back stack 0 = [*Configuration.HelloWorld] 
// push
back stack 1 = [Configuration.HelloWorld, *Configuration.MoreOptions]
```

And because we just said that the last element in the back stack is on screen, this means that the view of `HelloWorld` gets detached, and `MoreOptions` gets created and attached.


## Back pressing and the back stack

The reverse is happening when we pressed back.

By default, back pressing is propagated to the deepest (active) levels of the RIB tree by default, where each RIB has a chance to react to it:

1. `Interactor` has a chance to override `handleBackPress()` to do something based on business logic
2. `Router` will be asked if it has back stack to pop. If yes, pop is triggered and nothing else is done.
3. If `Router` had only one more configuration left in its back stack, there's nothing more to remove. The whole thing starts bubbling up the RIB tree to higher levels until one of the levels can handle it (points 1 and 2).
4. If the whole RIB tree didn't handle the back press, then the last fallback is the Android environment (in practice this probably means that the hosting `Activity` finishes).

In our case, when we were on the `MoreOptions` screen, `GreetingsContainerRouter` had 2 elements in its back stack, so it could automatically handle the back press event by popping the latest:

```
back stack 0 = [*Configuration.HelloWorld] 
// push
back stack 1 = [Configuration.HelloWorld, *Configuration.MoreOptions]
// back press
back stack 2 = [*Configuration.HelloWorld]
```

And again, because last element in the back stack is on screen, this means that `MoreOptions` gets detached, and `HelloWorld` gets attached back to the screen again.


## Almost there: use the result from options screen

Of course there's no point of opening the second screen if we cannot interact with it and our only option is to press back.

So let's make it a bit more useful:
1. Add a new element to `Output` in `OptionsSelector`: ```data class LexemSelected(val lexem: Lexem) : Output()```
2. Add a new element to `Event` in `OptionsSelectorView`: ```data class ConfirmButtonClicked(val selectionIndex: Int) : Event()```
3. In `OptionsSelectorViewImpl`, add a click listener on `confirmButton` that will trigger this event. 
4. Go to `OptionSelectorInteractor`. Add the transformation between `Event` and `Output` in the `viewEventToOutput` transformer.
5. Go to `GreetingsContainerInteractor`, and add a branch to the `when` expression in `moreOptionsOutputConsumer`

What we want to do is:
- Take the `Lexem` that's coming in the `Output`
- Feed it to `HelloWorld` using an Input of `UpdateButtonText`
- Actually go back one screen = manually popping the back stack

This is how it might look:
```kotlin
internal val moreOptionsOutputConsumer: Consumer<Output> = Consumer {
    when (it) {
        is Output.LexemSelected -> {
            router.popBackStack()
            helloWorldInputSource.accept(
                UpdateButtonText(it.lexem)
            )
        }
    }
}
```

## Test run!

At this point we should be able to go to options selection screen, chose an item from the radio group, and pressing the confirm button we should land back at the Hello world! screen with the label of the hello button reflecting our choice.

![](https://i.imgur.com/j9ZDpDN.png)

Press the button! 

![](https://i.imgur.com/8HXzhog.png)


## Reflecting on what we just did: composing

We just created more complex functionality by a composition of individually simple pieces!

When we created our hierarchy like this, we kept the two children decoupled from each other:
```
GreetingsContainer
├── HelloWorld
└── OptionsSelector
```
Even though they work together as a system, `HelloWorld` and `OptionsSelector` has no dependency on each other at all.

This is actually beneficial, because:
- `OptionsSelector` is a generic screen (it renders *whatever* text options we build it with)
- From the perspective of `HelloWorld` it really shouldn't care where it gets its other greetings

Keeping them decoupled means:
- `OptionsSelector` can be reused freely elsewhere to render the same screen with other options
- `HelloWorld` can be reused with different implementation details how to provide more options to it

The combined functionality we just implemented emerges out of the composition of these pieces inside `GreetingsContainer`. Each level handles only its immediate concerns:
- `HelloWorld` implements hello functionality and can ask for more options
- `OptionsSelector` renders options and signals selection
- `GreetingsContainer` connects the dots and contains only coordination logic. All other things are delegated to child screens as implementation details.


## Tutorial complete
 
Congratulations! You can advance to the next one.


## Summary

**Dynamic routing**

1. Make the parent RIB be able to build a child RIB (as seen in **tutorial2**):
    1. Add configuration & routing action in Router
    2. Provide child `Builder` via DI
2. React to some event (usually to child `Output` as seen in **tutorial4**, but can be anything else) in `Interactor` of parent RIB by pushing new configuration to its `Router`
3. Use back press or `popBackStack()` programmatically to go back

**Composing functionality**
1. Instead of one messy RIB, map complex functionality to a composition of simple, single responsibility RIBs
2. When composing, keep parent levels simple. They should only coordinate between child RIBs by `Inputs`/`Outputs`, and delegate actual functionality to children as implementation details.
3. Sibling RIBs on the same level should not depend on each other, so that they can be easily reused elsewhere.
