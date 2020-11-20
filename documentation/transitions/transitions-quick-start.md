# Transitions

RIBs supports transition animations for routing changes.

It's as easy as passing an optional ```TransitionHandler``` to your ```Router```:

```kotlin
abstract class Router<C : Parcelable>(
    // ... remainder omitted ...
    private val transitionHandler: TransitionHandler<C>? = null,
    // ... remainder omitted ...
)
```

## Triggering transitions

The passed in ```TransitionHandler``` is automatically invoked upon every change in the current routing configuration. You don't need to do anything special!


## Quick start: framework provided transitions

There are framework provided implementations of ```TransitionHandler```. If you're looking for a quick start, it's literally a matter of seconds adding any of the below to your ```Router``` and seeing them in action.


### ```CrossFader```

Runs simultaneously:
- The ```View``` of the ```Node``` associated with the exiting configuration fades out
- The ```View``` of the ```Node``` associated with the entering configuration fades in

Customisation options:
- duration
- interpolator
- condition (which configurations to run it on, default: on all)


### ```Slider```
- The ```View``` of the ```Node``` associated with the exiting configuration slides out
- The ```View``` of the ```Node``` associated with the entering configuration slides in

Customisation options:
- gravity of the exiting transition (a reverse value is used for the entering transition automatically)
- animation container
- duration
- interpolator
- condition (which configurations to run it on, default: on all)


### ```TabSwitcher```
A derived version of the ```Slider``` which decides the sliding gravity dynamically based on whether the entering configuration is found before or after the exiting configuration in the provided list.

Customisation options:
- tabs order (list of configurations)
- animation container
- duration
- interpolator
- condition (which configurations to run it on, default: on all)


### ```SharedElements```
Implements a shared element transition between two ```Views``` associated with the exiting and entering configurations.

Customisation options:
- shared element configuration (```List<Param>```)
- condition (which configurations to run it on, default: on all)


## Multiple transitions

You can go for a combination of different ```TransitionHandlers``` too by using the ```TransitionHandler.Multiple```` class:

```kotlin
class MyCombinedTransitionHandler(
    duration: Long
): TransitionHandler.Multiple<Configuration>(
    listOf<TransitionHandler<Configuration>>(
        TabSwitcher(
            animationContainer = AnimationContainer.RootView,
            duration = duration,
            tabsOrder = listOf(
                Configuration.A,
                Configuration.B,
                Configuration.C
            )
        ),

        SharedElements(
            params = listOf(
                SharedElementTransition.Params(
                    duration = duration,
                    findExitingElement = { it.findViewById(R.id.sharedElement) },
                    findEnteringElement = { it.findViewById(R.id.sharedElement) },
                    translateXInterpolator = OvershootInterpolator(),
                    translateYInterpolator = OvershootInterpolator(14f),
                    rotation = SharedElementTransition.RotationParams(0.75f * 360)
                )
            )
        ),

        CrossFader(
            duration = duration
        )
    )
)
```

All the above ```TransitionHandlers``` are invoked simultaneously and run parallel. In this example, we are sliding and crossfading views while having a rotating shared element transition too.

You can see a sample of this working in the ```sandbox``` module.
