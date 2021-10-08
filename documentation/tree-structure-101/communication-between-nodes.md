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


