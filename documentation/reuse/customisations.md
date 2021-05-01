# Customisations

To create truly reusable components, you might not want to hardwire everything. A component that can display a photo grid might offer some customisation on column size; you might want to override branding or other configuration too. Customisations offer a way to do just that.


## Customisations are different from dependencies

A key difference in their implementation:
- Customisations should always have sensible defaults (if one cannot be provided, it's already a dependency!)
- Customisations are overridden only on the application level, keeping your tree of dependencies cleaner


## Defining customisations

Add them in the main interface:

```kotlin
interface SomeRib : Rib {

    // ...remainder omitted...

    class Customisation(
        val customisationValue1: Something = TODO(),      // always set a default value!
        val customisationValue1: SomeOtherThing = TODO(), // always set a default value!
        // ...remainder omitted...
    ) : RibCustomisation                                  // don't forget to extend this marker interface

    // ...remainder omitted...
}

Then in the builder:

```kotlin
class SomeRibBuilder(
    private val dependency: SomeRib.Dependency
) : SimpleBuilder<SomeRib>() {

    override fun build(buildParams: BuildParams<Nothing?>): SomeRib {
        val customisation = buildParams.getOrDefault(SomeRib.Customisation())

        // use what you defined:
        // customisation.customisationValue1
        // customisation.customisationValue2
    }
}
```

What's happening here:
- The framework provides the customisations through ```buildParams``` automatically
- It will pass you the final (potentially overridden) instance, but just in case there isn't any, you need to pass a default instance
- The default instance here is just ```SomeRib.Customisation``` created with the no-arg constructor


### Good candidates for customisation

### Bad candidates for customisation

## Overriding customisations



## What's wrong with customisations as dependencies

While you can represent all the above with dependencies, it will be inconvenient in a larger tree structure. Since most of the customisations are decided on an application level, all those dependencies will "bubble up" in the tree. This will not only bloat your interfaces, but will hurt hiding details:

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

In the above example, ```B``` have to give all the details to ```D``` and ```E``` that they require.

But since ```B``` is also a shared component living in a shared module, it cannot know about application-level configuration, and cannot satisfy those dependencies directly. Its only choice is to bubble them up by adding them to its own dependencies too.

It's easy to see how ```A``` will get bloated, and will have to know about all the dependencies that ```B```, ```C```, ```D```, ```E```, ```F``` and ```G``` define:

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
- Details that should be kept hidden are exposed: ```A``` really shouldn't care about that there's a ```D``` or ```F``` down the tree. Its only concern should be about its direct children: ```B``` and ```C```. All the rest are implementation details of those, and should be kept as such.
