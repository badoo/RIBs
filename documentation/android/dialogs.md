# Dialogs

## Dialog as routing

Let's approach dialogs from a different perspective: instead of thinking of launching a dialog as an action, think of them as state. If there's a dialog shown, it's a state of routing. To add or remove the dialog, we would manipulate the routing state itself.

You could represent dialogs as routing configurations:

```kotlin
sealed class Configuration : Parcelable {
    sealed class Content : Configuration() {
        @Parcelize object SomeChild : Content()
    }
    sealed class Overlay : Configuration() {
        @Parcelize object SomeDialog : Overlay()
    }
}
```

If you remember the [Back stack](../tree-structure-101/back-stack.md), it has different operations for content types (to be shown in themselves) and overlay types (to be shown on top of a content type). So here, we'll be leveraging exactly that - we'll want dialogs to be overlays. The nested sealed classes are only for self documenting purposes though – you can omit them if you wish.

The framework comes with a dedicated routing `Resolution`:

```kotlin
override fun resolve(routing: Routing<Configuration>): Resolution =
    when (routing.configuration) {
        is Content.SomeChild -> child { /*...*/ }
        is Overlay.SomeDialog -> showDialog(
            routingSource,          // available in Router
            routing.identifier,     // available from method's argument
            dialogLauncher,         // TODO
            someDialog              // TODO
        )
    }
```

Here the first two routing-related arguments will help the framework to automatically remove the current configuration when the user triggers closing the dialog on the UI.

The `dialogLauncher` argument is available from `IntegrationPoint`. Similar to `ActivityLauncher` and `PermissionRequester`, you can grab them in your root `RibActivity`, or through `node.integrationPoint.dialogLauncher` directly.

The last argument, `someDialog` is what we'll define ourselves.


## Dialog DSL

Most of the dsl should be pretty self-explanatory:

```kotlin
class SimpleDialog : Dialog<Dialog.Event>({
    // Inject (Smart)Resources in constructor to resolve texts if needed
    title = Text.Plain("Simple dialog")
    message = Text.Plain("Lorem ipsum dolor sit amet")
    buttons {
        positive(Text.Plain("Yay!"), Positive)   // links it to event type: Positive
        negative(Text.Plain("No way"), Negative) // links it to event type: Negative
        neutral(Text.Plain("Meh?"), Neutral)     // links it to event type: Neutral
    }

    themeResId = R.style.AppTheme_ThemedDialog

    cancellationPolicy = Cancellable(
        event = Event.Cancelled, // links it to event type: Cancelled
        cancelOnTouchOutside = false
    )
})
```

This will be rendered as a simple `AlertDialog` when launched.

One key point here is that the `Dialog` itself uses our [Minimal reactive API](../extras/minimal-reactive-api.md), and is an observable source of dialog events.

The framework default set of events in the `Dialog` class:

```kotlin
sealed class Event {
    object Positive : Event()
    object Negative : Event()
    object Neutral : Event()
    object Cancelled : Event()
}
```

But of course, you can define your own set of events - just define something else for the `<T>` type:

```kotlin
class SimpleDialog : Dialog<YourEventType>
```

When using a different type, remember to update the `buttons` section in the dialog dsl to link to your events, and you should be ready to go.


## Rib in a dialog

You can also render another `Rib` in the dialog:

```kotlin
class RibDialog(
    someChildBuilder: SomeChildBuilder
) : Dialog<Dialog.Event>({
    title = Text.Resource(R.string.a_title_you_wish)
    ribFactory {
        someChildBuilder.build(it)
    }
    buttons {
        positive(Text.Plain("Ok"), Positive)
        negative(Text.Plain("Cancel"), Negative)
    }
})
```

The `Rib` will be created and destroyed along with the dialog automatically.


## Dialog events

You can read the documentation for [Minimal reactive API](../extras/minimal-reactive-api.md) if you haven't already to see how to react to dialog events. 

One thing to pay attention to though, is to inject the same dialog object to both your `Router` and your `Interactor` (or any other component where you handle business logic).
