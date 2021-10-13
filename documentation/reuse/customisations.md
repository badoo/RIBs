# Customisations

To create truly reusable components, you might not want to hardwire everything. A component that can display a photo grid might offer some customisation on the number of columns; or, you simply might want to override branding-related looks or parameters. Customisations offer a way to do just that.


## Customisations are different from dependencies

A key difference in their implementation:
- Customisations should always have sensible defaults (if one cannot be provided, it's already a dependency!)
- Customisations are overridden only on the application level, keeping your tree of dependencies cleaner

![](https://i.imgur.com/vYR1qzh.png)
![](https://i.imgur.com/ge2a88g.png)

## Defining customisations

Add them in the main interface of your `Rib`:

```kotlin
interface SomeRib : Rib {

    // ...remainder omitted...

    class Customisation(
        val customisationValue1: Something = TODO(),      // always set a default value!
        val customisationValue1: SomeOtherThing = TODO(), // always set a default value!
        // ...remainder omitted...
    ) : RibCustomisation // <-- don't forget to extend this marker interface

    // ...remainder omitted...
}
```

Then in the builder:

```kotlin
class SomeRibBuilder(
    private val dependency: SomeRib.Dependency
) : SimpleBuilder<SomeRib>() {

    override fun build(buildParams: BuildParams<Nothing?>): SomeRib {
        val customisation = buildParams.getOrDefault(SomeRib.Customisation())

        // TODO use what you defined:
        // customisation.customisationValue1
        // customisation.customisationValue2
    }
}
```

What's happening here:
- The framework provides the customisations to your `Builder` through ```buildParams``` automatically
- It will pass you an instance that contains all applied overrides of defaults – but just in case there wasn't any overrides defined, you need to pass a default instance
- The default instance here is just ```SomeRib.Customisation``` created with the no-arg constructor


### Good candidates for customisation

- `ViewFactory`, so you can have different looks when reusing the same component (in a different app, or in the same app but in a different context), while also providing some default looks
- Any primitive configuration which isn't crucial from the perspective of the `Rib` (e.g. number of columns in a photo grid)

```kotlin
class Customisation(
    val viewFactory: FooBarView.Factory = FooBarViewImpl.Factory(), 
    val numberOfColumns: Int = 3
) : RibCustomisation
```

### Bad candidates for customisation

- Anything you can't provide a default value for. Those are hard dependencies.
- As a rule of a thumb, app-wide data sources, network clients, utilities, and similar. Those are better passed down as dependencies for compile-time safety too. In short, don't use customisations as a dependency injection mechanism.

## Customising views

Let's take another look at this:

```kotlin
class Customisation(
    val viewFactory: FooBarView.Factory = FooBarViewImpl.Factory(), 
) : RibCustomisation
```

Here, `FooBarViewImpl` is defined in the same Rib, and is providing default looks. As a useful pattern, we can make the constructor also have a default layout resource:

```kotlin
    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_foobar
    ) : FooBarView.Factory {
        // ...
    }
```

This way client code can decide on the level it wants to customise this:

- It can choose to create the same `FooBarViewImpl.Factory` but provide a different layout resource. Since it will be used by the same class, it's expected to contain all the same xml `id`s as a constraint. This is useful when you only want to apply slightly different looks, but overall rely on the same xml elements.
- Or it can create a completely different implementation of the interface. This is useful when you'd also like to have different elements / behavior.

## Defining customisations

Applying customisations should be done on the application level, and can be achieved with an implementation of `RibCustomisationDirectory`:

```kotlin
object AppRibCustomisations : RibCustomisationDirectory by customisations({
    // TODO
})
```

You have two options in `RibCustomisationDirectory`:

1. Define application-wide customisations
2. Have scoped customisations

Let's take a look at each.

## Application-wide customisations

```kotlin
object AppRibCustomisations : RibCustomisationDirectory by customisations({
    // Application-wide
    +FooBar.Customisation(
        viewFactory = FooBarViewImpl.Factory(R.layout.rib_foobar_override)
    )
})
```

What's happening here:

1. Using the unary plus `+` operator we add a new customisation for `FooBar`
2. Here we are reusing the default `Factory` defined by the `Rib`, only overriding the default layout resource
3. The framework will use this customisation everywhere where `FooBar` is constructed 


## Scoped customisations

The framework offers a dsl to define scoped customisations too. By using any other `Rib` class, you can narrow down the scope where a `Customisation` will be applied:

```kotlin
object AppRibCustomisations : RibCustomisationDirectory by customisations({

    // Narrow scope
    SomeOtherRib::class {
        YetAnotherRib::class {
            +FooBar.Customisation(
                viewFactory = SomethingCompletelyDifferent()
            )
        }
    }
    
    // Application-wide
    +FooBar.Customisation(
        viewFactory = FooBarViewImpl.Factory(R.layout.rib_foobar_override)
    )
})
```

What's happening here:

1. We tell the framework to use `SomethingCompletelyDifferent` as a `viewFactory` for `Foobar` whenever `Foobar` is nested under `YetAnotherRib`, which is nested under `SomeOtherRib`. Note that these are not direct parent-child relationships, and there can be any number of levels in the tree in-between them! This is similar to CSS rules from web dev.
2. The default `FooBar.Customisation` will be used in any other context when `FooBar` is created outside of the above scope.


## Supplying the definitions to the tree

The final piece of the puzzle is to pass your customisation definitions to the tree when you contruct your root:

```kotlin
    override fun createRib(savedInstanceState: Bundle?) =
        YourRootBuilder(
            object : YourRoot.Dependency {
                // TODO
            }
        ).build(root(
            savedInstanceState = savedInstanceState,
            customisations = AppRibCustomisations // <-- Pass them here
        ))
```


## Side-note: What's wrong with customisations as dependencies

While you can represent anything you put into customisations also with dependencies, it will be inconvenient in a larger tree structure. Since most of the customisations are decided on an application level, all those dependencies will "bubble up" in the tree. This will not only bloat your interfaces, but will hurt hiding details:

```
app
└── A
    ├── B
    │   └── D
    │   └── E
    │
    └── C
        └── F
        └── G
```

In the above example, `B` have to give all the details to `D` and `E` that they require.

But since `B` is also a shared component living in a shared module, it cannot know about application-level configuration, and cannot satisfy those dependencies directly. Its only choice is to bubble them up by adding them to its own dependencies too.

It's easy to see how `A` will get bloated, and will have to know about all the dependencies that `B`, `C`, `D`, `E`, `F` and `G` define:

```kotlin
interface A : Rib {
    interface Dependency {
        // Don't do this!
        val someCustomisationForF1: Unit
        val someCustomisationForF2: Unit
        val someCustomisationForG: Unit
        val someCustomisationForC: Unit
        val someCustomisationForE1: Unit
        val someCustomisationForE2: Unit
        val someCustomisationForE3: Unit
        val someCustomisationForD: Unit
        val someCustomisationForB: Unit
        val someCustomisationForA: Unit
    }
}
```

Imagine this with 5, 10, 20 levels in the tree.

Problems with this:
- Bloated interfaces
- It's more difficult to change the tree structure, since you need to update all the above levels
- Details that should be kept hidden are exposed: `A` really shouldn't care about that there's a `D` or `F` down the tree. Its only concern should be about its direct children: `B` and `C`. All the rest are implementation details of those, and should be kept as such.
