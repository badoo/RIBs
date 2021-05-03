# Communication between Nodes

While RIBs give you complete freedom over how you want to achieve communication between your different ```Nodes```, the framework also comes with some utilities to make it easier.

## Connectable

This interface defines input and output channels of generic types:

```kotlin
interface Connectable<Input, Output> {
    val input: Relay<Input>
    val output: Relay<Output>
}
```

Inputs are messages the current RIB can take and interpret as commands.

Outputs are messages the current RIB can produce.

Note: These interfaces rely on our [Minimal reactive API](../extras/minimal-reactive-api.md), which you can convert to some other reactive type if you wish. You can also find an RxJava2 equivalent of them in the `rib-rx2` package.

To use them, you would first define these types in your main interface:

```kotlin
import somepackage.SomeRib.Input
import somepackage.SomeRib.Output

interface SomeRib : Rib, Connectable<Input, Output> {

    sealed class Input {
        object SomeInput1 : Input()
        data class SomeInput2(val payload: Unit) : Input()
    }

    sealed class Output {
        object SomeOutput1 : Output()
        object SomeOutput2 : Output()
    }
}
```

Here we say that ```SomeRib``` should be ```Connectable<Input, Output>``` – and since our ```SomeNode``` is an implementation of this interface, we'll need to implement it there too.

Worry not, we can do it by simply delegating it to the ```NodeConnector``` class coming with the framework:


```kotlin
import somepackage.SomeRib.Input
import somepackage.SomeRib.Output

class SomeNode(
    // ...remainder omitted...
    connector: NodeConnector<Input, Output> = NodeConnector() // <--
) : Node<HelloWorldView>(
    // ...remainder omitted...
), SomeRib, 
   Connectable<Input, Output> by connector { // <--

}
```

This is useful, because now whenever we have a reference to ```SomeRib```, we can expect to communicate with it:

```kotlin
// Dummy example of communication in client code
val someRib: SomeRib = TODO()
someRib.input.accept(Input.SomeInput1)
someRib.output.subscribe {
    when (it) {
        is Output.SomeOutput1 -> TODO()
        is Output.SomeOutput2 -> TODO()
    }
}
```



## Inputs down, outputs up

As we've seen in [Happy little trees](happy-little-trees.md), visibility is best limited to parents seeing their children, while children are agnostic of the parent that built them.

This suggests an information flow where:
- Parent sends input commands to child, child reacts to them (without knowing where they come from)
- Child sends output signals (without knowing where they go), parent observes them and reacts

This helps with keeping things decoupled.


## Start listening even before the child is ready

Business logic in any child can potentially mean that the child starts sending some outputs right after it's wired up. To not miss those messages, it's best to start listening to them before that. The ```onChildBuilt``` callback method in the ```SubtreeChangeAware``` interface allows just that:

```kotlin
class SomeBusinessLogic : SubtreeChangeAware {

	// Called by the framework as soon as the child is built, 
	// before it receives any wakeup calls
    override fun onChildBuilt(child: Node<*>) {
        when (child) {
            // Use interface names:
            is SomeRib -> TODO("setup listening to child.output")
            is SomeOtherRib -> TODO("setup listening to child.output")
        }
    }
}
```

Since we earlier marked ```SomeRib``` as ```Connectable<Input, Output>```, we can now directly use ```child.output``` with type information available.


(Don't forget to pass this class as a ```Plugin``` to your ```Node```! See [Plugins](../basics/plugins.md) for more info)


## Start sending only after the child is ready

When we want to send inputs to a child, we need to do the opposite of what we did with outputs: we meed to make sure to only send them after the child is ready, so it doesn't miss any. We can use the ```onChildAttached``` method for this:

```kotlin
class SomeBusinessLogic : SubtreeChangeAware {

	// Called by the framework after the child is ready
    override fun onChildAttached(child: Node<*>) {
        when (child) {
            // Use interface names:
            is SomeRib -> TODO("setup comms that use child.input")
        }
    }
}
```

## Creating, handling, bubbling up output

So when should you create an output at all? Outputs offer you non-local handling of business events by delegating the decision what to do with them to the parent. The trade-off is that there's slightly more code needed than if the child handles the business event locally.

A rule of a thumb is:

- If you _definitely know_ in your ```RIB``` what needs to be done, handle it locally.
- If you _cannot know_ what needs to be done, send an output
- Also, if you want to offer some flebility, also rather send an output


Some examples to illustrate this:

1. A generic user list component would send an output when an item is selected. Whether that should open a chat with that user, or show their profile, etc. depends completely on the parent that built this, so it's not its concern.

2. Let's say we need to show a confirmation dialog to the user. We can go both ways, but with not opening the dialog locally but sending an output we can offer some flexibility: will it be a system dialog? a bottom sheet? or do we start a completely new screen / flow?

3. In any other case where it's reasonable to handle locally (as the added flexibility is not required), do so.


