# Preparations

To make testing easier, it is a good practice to inject abstractions (base classes or interfaces) to your classes' constructor, instead of implementations.
This will allow us to use fakes or test implementations instead of mocks.

```kotlin
class SomeScreenRouter(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<Configuration>,
    val child1Builder: Child1Builder,
) : Router<Configuration> {
    
    fun resolve(routing: Routing<Configuration>) =
        when (routing.configuration) {
            is Configuration.Content.Child -> child { child1Builder.build(Child1Builder.Param(true), it) }
        }

}
```

This is a simple `Router` that can create and attach one child.
There is one issue with it – we can't efficiently unit test it without `mockito-inline`, because (most likely) `Child1Builder` is a final class.
We can improve this code to use base abstract class.

```diff
class SomeScreenRouter(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<Configuration>,
--  val child1Builder: Child1Builder,
++  val child1Builder: Builder<Child1Builder.Param, Child1>,
) : Router<Configuration>(...)
```

This change does not affect the behaviour.
`Builder` still has strict generic types, so we won't pass some different builder as `child1Builder`.
Now we can replace builder implementation with fake, so we won't create any real instances of `Child1`.
