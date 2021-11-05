# Permanent parts

Not every child you want to add needs the dynamism of the back stack though – some should always be added and never removed.

To hold routing configurations that should always be on screen (e.g. a Menu), you can use the `Permanent` routing source, which takes an `Iterable` of configurations:

```kotlin
class Permanent<C : Parcelable>(
    permanents: Iterable<C>
) : RoutingSource<C>
```

You can also use this helper methods to create one:

```kotlin
fun <C : Parcelable> permanent(vararg permanents: C) =
    Permanent(permanents.toSet())
```

And you can use operator overload to combine it with your back stack before passing it to your `Router`:

```kotlin
routingSource = backStack + permanent(Menu)
```

![](https://i.imgur.com/s7Am2to.png)

In the above screen, the bottom navigation menu is a permanent part that allows changing the content of the sibling view.
