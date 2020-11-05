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

All blocks are using pseudo code. Letters represent configurations.

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

## Usage example
Router
Interactor
Back stack

## Back stacks are nested

## Staying alive