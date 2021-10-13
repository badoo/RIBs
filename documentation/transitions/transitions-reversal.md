# Reversing transitions

A killer feature of the framework is that transitions can be automatically reversed. You don't need to do anything special! A reversal happens whenever this happens:

1. There's a configuration change from any ```X``` state to any ```Y``` state of the ```RoutingSource```
2. The transition animation is playing and hasn't settled yet
3. There's a configuration change from ```Y``` state back to ```X``` state.

Note hat states here don't reference individual configurations, but the whole state of the ```RoutingSource```!

For example, in the case of the back stack, the whole content of the back stack is considered its state, so these sequences would trigger transition & reversal:

```
T0: [A, B, C]       // element on screen: C
push(D)             // e.g. from business logic
T1: [A, B, C, D]    // transition plays: C exiting, D entering
pop()               // easily triggered by back press
T2: [A, B, C]       // transition reverses to starting point: C is restored, D exits
```

```
T0: [A, B, C]       // element on screen: C
replace(D)          // e.g. user selects a menu item to change current screen
T1: [A, B, D]       // transition plays: C exiting, D entering
replace(C)          // e.g. user selects the previous menu item
T2: [A, B, C]       // transition reverses to starting point: C is restored, D exits
```

But this would NOT work:

```
T0: [A, B, C]       // element on screen: C
push(D)             // e.g. from business logic
T1: [A, B, C, D]    // transition plays: C exiting, D entering
newRoot(C)
T2: [C]             // no reversal, even though C is the same as in T0 - the whole of the state is different
```

![](https://i.imgur.com/0aYzjUX.gif)

## Transition reversal is automatic

The coolest thing is probably that back press has no special role here at all! Transition reversal automatically works regardless of:
- what kind of routing source you have (back stack or not)
- what triggered the configuration change starting the initial transition
- what triggered the configuration change that would lead back to the original state

As long as there's a transition on the fly running between any ```X```→```Y``` states, any incoming transition of ```Y```→```X``` would reverse it automatically. At its core this is triggered by the simplest tool possible, an object equality check:

```kotlin
fun isReverseOf(other: TransitionDescriptor) =
    from == other.to && to == other.from
```
