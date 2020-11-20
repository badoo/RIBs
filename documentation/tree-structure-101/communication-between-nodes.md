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

To use it, you would first define these types in your main interface:

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

## Start listening even before the child is ready

## Start sending only after the child is ready

## Handling child output

### When to handle locally? 
### When to bubble up?