# Screen history with a simple back stack

## Manipulating routing

Now that we've covered how to define and resolve routing configurations, it's time to start using them!

A simple tool to do it is the back stack.

## What's a back stack

A back stack at its heart is list of routing configurations.

Its rule is that the last element (and only the last element) is considered "on screen", so you can easily create a simple linear screen history just by manipulating the stack directly.

The back stack offers a set of base operations, while also allowing you to define your own.

Comparing this to the back stack management in Android, this one offers a synchronous, completely predictable, understandable, and flexible way to implement your screen history.


## Back stack operations

Letters represent configurations.

### .push()

```kotlin
// back stack == [A, B, C] 
backStack.push(D)
// back stack == [A, B, C, D] 
```

### .replace()

```kotlin
// back stack == [A, B, C] 
backStack.replace(D)
// back stack == [A, B, D] 
```


### .pop()

Base case: removes last element
```kotlin
// back stack == [A, B, C] 
backStack.pop()
// back stack == [A, B] 
```

The last element cannot be popped though:
```kotlin
// back stack == [A] 
backStack.pop()
// back stack == [A] 
```


### .newRoot()
```kotlin
// back stack == [A, B, C] 
backStack.newRoot(D)
// back stack == [D] 
```


### .singleTop()

Case: argument is not found in the back stack

Result: .singleTop() acts as .push()

```kotlin
// back stack == [A, B, C, D]
backStack.singleTop(E)
// back stack == [A, B, C, D, E]
```


Case: argument is found in the back stack

Result: .singleTop() acts as a sequence of .pop() operations, going back to the found element

```
// back stack == [A, B, C, D]
backStack.singleTop(B)
// back stack == [A, B]
```

Case: argument is found in the back stack by its type, but not equals

Result: .singleTop() acts as a sequence of .pop() operations, going back to the found element, followed by a .replace() operation to the argument value

```
// back stack == [A, B, C, D]
backStack.singleTop(B*)
// back stack == [A, B*]
```

## Custom back stack operations

You're free to add new operations on the back stack by implementing the ```BackStackOperation``` interface:

```kotlin
interface BackStackOperation<C : Parcelable> : (BackStack<C>) -> BackStack<C> {
    fun isApplicable(backStack: BackStack<C>): Boolean
}
```

Let's see the implementation of the ```Push``` operation to see how this goes:

```kotlin
data class Push<C : Parcelable>(
    private val configuration: C
) : BackStackOperation<C> {

    // The operation will only be executed if this returns true
    override fun isApplicable(backStack: BackStack<C>): Boolean =
        configuration != backStack.current?.configuration

    // Implement it in invoke so that it returns a new back stack 
    // with the operation applied.
    //
    // Note: since BackStack<C> is a typealias for 
    // List<RoutingHistoryElement<C>>,
    // you can use any list operation!
    override fun invoke(backStack: BackStack<C>): BackStack<C> =
        backStack + RoutingHistoryElement(Routing(configuration))

    // Helper method
    private val BackStack<C>.current: Routing<C>?
        get() = this.lastOrNull()?.routing
}

// Extension method for convenience
fun <C : Parcelable> BackStackFeature<C>.push(configuration: C) {
    accept(BackStackFeature.Operation(Push(configuration)))
}
```


## Usage example

So at this point, we have:

1. ```Router``` to define and resolve ```Configurations```
2. ```BackStackFeature``` to keep a list-based history of them, manipulatable by its operations

We'd also use a class dedicated for business logic (in our case, ```Interactor```, but you can go with ```Presenter``` or anything else too) to manipulate the back stack itself. (We won't implement it now, but you can imagine any business logic calling on the above described operations when it needs)

How do we connect the pieces?

```Router``` actually doesn't care whether we have a back stack kind of structure or something else. It requires a ```RoutingSource<C>``` argument in its constructor, which ```BackStackFeature``` already implements:

```kotlin
abstract class Router<C : Parcelable>(
    buildParams: BuildParams<*>,
    protected val routingSource: RoutingSource<C>,
	// ...remainder omitted...
)
```

So we could create an instance of ```BackStackFeature``` it in our ```Builder``` and just pass it to both our ```Router``` and our ```Interactor```

```kotlin
class SomeBuilder(
    private val dependency: SomeRib.Dependency
) : SimpleBuilder<SomeRib>() {

    override fun build(buildParams: BuildParams<Nothing?>): SomeRib {
        val backStack = BackStackFeature(
            buildParams = buildParams,
            initialConfiguration = SomeConfiguration
        )
        val interactor = SomeInteractor(
            buildParams = buildParams,
            backStack = backStack
        )
        val router = SomeRouter(
            buildParams = buildParams,
            routingSource = backStack
        )

        return SomeNode(
            buildParams = buildParams,
            viewFactory = SomeViewImpl.Factory().invoke(null),
            plugins = listOf(
                router,
                interactor
            )
        )
    }
}
```

As a result, ```Interactor``` and ```Router``` are decoupled: one is manipulating the back stack, the other reacts to changes in the back stack.


## Staying alive

One thing to know about the back stack, is that child ```Nodes``` which were created as a result of resolving ```Configurations``` stay alive until either:

- they associted ```Configuration``` is removed from the back stack by an operation (e.g. ```.pop()```, ```.replace()```, ```.newRoot()```, etc.), or
- the system goes through a save / restore cycle


Let's see some examples!


### Changes to Nodes after back stack manipulation

```
T0: [A] 
- Initial state, A is resolved and on screen

.push(B)
.push(C)

T1: [A, B, C]
- A is removed off the screen, but the associated Node is not removed from the tree
- B is now resolved, the associated Node is attached, but off screen
- C is now resolved, the associated Node is attached, and on screen

.pop()

T2: [A, B]
- A is unchanged
- B is now re-attached to the screen
- C is removed, the associated Node is removed from the tree and destroyed

.push(C)

T3: [A, B, C]
- Identical to T1, except C now results in a completely new Node being built from scratch

```

### Changes to Nodes after save / restore cycle

```
T0: [A, B, C, D] 
- State restored from Bundle
- A is NOT resolved, no Node is built / attached 
- B is NOT resolved, no Node is built / attached
- C is NOT resolved, no Node is built / attached
- D is now resolved, the associated Node is attached, and on screen

.pop()

T1: [A, B, C]
- A is NOT resolved, no Node is built / attached 
- B is NOT resolved, no Node is built / attached
- C is now resolved, the associated Node is attached, and on screen
- D is removed, the associated Node is destroyed
```






















