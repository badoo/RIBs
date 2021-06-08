# Custom transitions

The ```TransitionHandler``` interface defines a single operation to be implemented:

```kotlin
interface TransitionHandler<C> {

    fun onTransition(elements: List<TransitionElement<out C>>): TransitionPair
```

The list of ```TransitionElements``` contain all the information about the transition:

```kotlin
open class TransitionElement<C>(
    val configuration: C,
    val direction: TransitionDirection,
    val addedOrRemoved: Boolean,
    val identifier: Rib.Identifier,
    val view: View,
    val progressEvaluator: MultiProgressEvaluator = MultiProgressEvaluator()
)
```

Not how here you get access to the ```view``` being transitioned on or out.

The expected return type of ```TransitionPair``` is just:

```kotlin
data class TransitionPair(
    val exiting: Transition?,
    val entering: Transition?
)
```

Where ```Transition``` is:

```kotlin
interface Transition {

    fun start()

    fun end()

    fun reverse()
```

This practically means you are free to implement your transitions however you see fit.

You can check the framework provided implementations of ```TransitionHandler``` in the ```com.badoo.ribs.routing.transition.handler``` package to get some ideas:

- ```CrossFader```
- ```Slider```
- ```TabSwitcher```

These implementations mostly add some logic over simple effect elements defined in the ```com.badoo.ribs.routing.transition.effect``` package. Feel free to use them too!
