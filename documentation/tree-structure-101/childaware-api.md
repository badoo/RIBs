# ChildAware API

The framework includes the `ChildAware` interface, which comes with a powerful API.

It allows you to scope communication with (or between) dynamically available children easily.


## Baseline

We can assume the following for the next examples:

1. Let's assume that `SomeInteractor` belongs to `SomeNode` (and is passed as a [Plugin](../basics/plugins.md) to it)
2. Let's assume `SomeNode` can host multiple children: `Child1`, `Child2`, etc.
3. We know that the `Interactor` base class in the framework implements `NodeLifecycleAware`, which makes sure we will receive the `onCreate` callback from the framework
4. We know that `Interactor` implements `ViewAware`, which means we will receive the `onViewCreated` callback too
5. `Interactor` also implements `ChildAware`, which unlocks the `childAware` DSL we'll see in the following snippets


## Single child scenario

```kotlin
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.clienthelper.childaware.childAware
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams

class SomeInteractor(
    buildParams: BuildParams<*>,
    // ...
) : Interactor<SomeRib, SomeRibView>(
    buildParams = buildParams
) {

    override fun onCreate(nodeLifecycle: Lifecycle) {
        super.onCreate(nodeLifecycle)

        childAware(nodeLifecycle) {
            whenChildAttached<Child1> { commonLifecycle, child1 ->
                // TODO e.g.
                //  - subscribe to child1.output
                //  - feed something to child1.input
                //  - use commonLifecycle for scoping reactive subscriptions
            }
        }
    }
}
```

Let's unpack what's happening here:

1. We pass `SomeNode`'s `nodeLifecycle` as the upper bound for lifecycle scoping
2. Using `whenChildAttached<Child1>` we tell the framework to execute the subsequent lambda whenever any child implementing the `Child1` interface is attached.
3. In the lambda, we receive `commonLifecycle`: This is the minimum common lifecycle of the parent and the child, and is safe to scope any subscriptions by.
4. Also in the lambda, we receive an instance of the attached child, here `child1`
5. We can then use these to communicate with `child1` for the duration it is attached to the parent
6. When scoped properly, all communication will be disposed of when the child is detached
7. The lambda will be invoked again automatically for all subsequent attaches of `Child1`


## Scoping for the view instead

```kotlin

class SomeInteractor(
    buildParams: BuildParams<*>,
    // ...
) : Interactor<SomeRib, SomeRibView>(
    buildParams = buildParams
) {

    override fun onViewCreated(view: SomeView, viewLifecycle: Lifecycle) {
        childAware(viewLifecycle) {
            whenChildAttached<Child1> { commonLifecycle, child1 ->
                // TODO e.g.
                //  - connect view to child1.input
                //  - connect child1.output to view
                //  - use commonLifecycle for scoping reactive subscriptions
            }
        }
    }
}
```

In this snippet:

- We pass the parent's `viewLifecycle` of `SomeNode` as the upper bound for lifecycle scoping
- This means that the `commonLifecycle` we receive in the lambda will be the minimum of:
  1. The view's lifecycle: if the view is destroyed, this lifecycle will move to a `DESTROYED` state too
  2. The lifecycle of `child1`: if the child is detached, this lifecycle will move to a `DESTROYED` state too
- (The above of course applies for start / stop / resume / pause lifecycle events and their corresponding states too!)



## Multiple children

```kotlin
class SomeInteractor(
    buildParams: BuildParams<*>,
    // ...
) : Interactor<SomeRib, SomeRibView>(
    buildParams = buildParams
) {

    override fun onCreate(nodeLifecycle: Lifecycle) {
        super.onCreate(nodeLifecycle)

        childAware(nodeLifecycle) {
            whenChildrenAttached<Child1, Child2> { commonLifecycle, child1, child2 ->
                // TODO e.g.
                //  - subscribe child1.output to child2.input with some mapping
                //  - use commonLifecycle for scoping reactive subscriptions
            }
        }
    }
}
```

In this snippet:

- We use `whenChildrenAttached` instead of `whenChildAttached`: this variant connects multiple children
- The framework will invoke our lambda every time two children of the given types are both attached to the parent
- The `commonLifecycle` we receive in the lambda will now be the minimum of:
  1. The parent's lifecycle
  2. The lifecycle of `child1`
  3. The lifecycle of `child2`
- Any of them e.g. is paused, the common lifecycle will also move back to the corresponding `STARTED` state
- Any of them e.g. is destroyed, the common lifecycle will also move to a `DESTROYED` state

You can,of course use this api scoped for the view lifecycle too similarly to the previous example.


## ChildAware API + MVICore bindings = even more power

[MVICore](https://github.com/badoo/MVICore) has some powerful tools like the `Binder` which gives you automatically managed, scoped reactive bindings.

To leverage them, you'll need to add dependency on:

MVICore / Binder
```groovy
implementation 'com.github.badoo.mvicore:binder:{latest-mvicore-version}'
```

The `:rib-mvicore` artifact from this library:
```groovy
implementation 'com.github.badoo.RIBs:rib-mvicore:{latest-RIBs-version}'
```

Then you can leverage the full power of their combined API:

```kotlin
import androidx.lifecycle.Lifecycle
import com.badoo.binder.using
import com.badoo.ribs.clienthelper.childaware.childAware
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.mvicore.createDestroy
import com.badoo.ribs.mvicore.resumePause
import com.badoo.ribs.mvicore.startStop

class SomeInteractor(
    buildParams: BuildParams<*>,
    // ...
) : Interactor<SomeRib, SomeRibView>(
    buildParams = buildParams
) {

    override fun onCreate(nodeLifecycle: Lifecycle) {
        super.onCreate(nodeLifecycle)

        childAware(nodeLifecycle) {
            createDestroy<Child1> { child1 ->
                bind(child1.output to someConsumer)
                bind(someSource to child1.input)
            }

            startStop<Child1, Child2> { child1, child2 ->
                bind(child1.output to child2.input using SomeMapper)
            }

            resumePause<Child1, Child2> { child1, child2 ->
                bind(child2.output to child1.input using SomeOtherMapper)
            }
        }
    }
}
```

Let's unpack what's going on here:

- `Binder` from MVICore gives us the `bind(A to B)` syntax
  - This creates a reactive subscription between A and B
  - This subscription is always scoped by a lifecycle
  - When that lifecycle is destroyed, so is the subscription
  - But should that lifecycle start again, `Binder` will automatically re-subscribe A to B

- The keywords you see:
  - `createDestroy` scopes a connection between create / destroy events of the lifecycle
  - `startStop` scopes a connection between start / stop events of the lifecycle
  - `resumePause` scopes a connection between resume / pause events of the lifecycle

- Also note how the `commonLifecycle` is no longer used explicitly, it's handled automatically under the hood

- Following from this, the last example in the snippet will:
  1. Create a subscriptions between `child2.output` and `child1.input` using a mapper to convert objects
  2. Automatically dispose of the subscription whenever either the parent, or `child1`, or `child2` gets paused
  3. Automatically re-subscribes again whenever all of them are in a resumed state again
  4. Points 2-3 can be repeated indefinitely
