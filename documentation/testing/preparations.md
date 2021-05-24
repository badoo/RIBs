# Preparations

To make testing easier it is a good practice to extract base classes and interfaces and use them as dependencies.
It will help to avoid `mockito` usages and replace implementation with testing ones.
A great example of this approach may be `Router` component.

```kotlin
class SomeScreenRouter(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<Configuration>,
    val child1Builder: Child1Builder,
) : Router<Configuration>(...) {
    
    fun resolve(routing: Routing<Configuration>) =
        when (routing.configuration) {
            is Configuration.Content.Child -> child { child1Builder.build(Child1Builder.Param(true), it) }
        }

}
```

This is a simple `Router` that can create and attach one child.
But there is one issue with it.
We can't efficiently unit test it without `mockito-inline`, because (most likely) `Child1Builder` is a final class.
We can improve this code.

```diff
class SomeScreenRouter(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<Configuration>,
--  val child1Builder: Child1Builder,
++  val child1Builder: Builder<Child1Builder.Param, Child1>,
) : Router<Configuration>(...)
```

This change does not affect behaviour.
`Builder` still has strict generic types, so we won't pass some different builder as `child1Builder`.
But it helps to efficiently unit test this class.
