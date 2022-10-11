# Providing dependencies

RIBs is unopinionated about how you wish to provide dependencies. This chapter is here only to give you some ideas and client code patterns you might find useful.


## Main interface

A simple way to provide dependencies for a single Rib.

**Pattern**:

1. Every `Rib` to have a main interface with a nested `Dependency` interface

```kotlin
interface FooBar : Rib {

    interface Dependency {
        val dep1: Foo
        val dep2: Bar
    }
}
```

2. `Builder` of the `Rib` to receive an instance of it in its constructor
```kotlin
class FooBarBuilder(
    private val dependency: FooBar.Dependency
) : SimpleBuilder<FooBar>() {
    // ...
}
```

**Why we suggest this**:

- Self documenting code
- `Dependency` interface serves as single collection of all we'll need to build this Rib
- Main interface serves as an entry point, easy to find



## Child builders

A simple way to provide / propagate dependencies to children in the tree.

Pattern:

1. Create a new class `###ChildBuilders`

```kotlin
internal open class FooBarChildBuilders( // <- // Internal!
    dependency: FooBar.Dependency // <- It should include dependencies already available to the current `Rib`
) {
    // To satisfy all dependencies in the subtree
    class SubtreeDependency(
        dependency: Switcher.Dependency // <- Use as a starting point
    ) : Switcher.Dependency by dependency, // <- Extend Dependency interfaces for the parent and all children, 
        Child1.Dependency,                 // and also, if the child only uses a subset of parent deps, use parent to delegate.
        Child2.Dependency {
        // Implement those that you need to satisfy locally
        override val someDeps: Foo
            get() = TODO()
    }
    
    // Construct a single instance to pass to child builders
    private val subtreeDeps = SubtreeDependency(dependency)

    // Provide fields for easy access to all builders this Rib will directly use to construct children
    val child1 = Child1Builder(subtreeDeps) // <- subtreeDeps already satisfies Child1.Dependency
    val child2 = Child2Builder(subtreeDeps) // <- subtreeDeps already satisfies Child2.Dependency
}
```

2. Create an instance in the local `Builder`:

```kotlin
class FooBarBuilder(
    private val dependency: FooBar.Dependency
) : SimpleBuilder<FooBar>() {

    private val builders by lazy { SwitcherChildBuilders(dependency) }
    
    // TODO: when creating Router, pass builders to it
}
```

3. Use in `Router` for child creation:

```kotlin
class FooBarRouter internal constructor(
    // ...
    private val builders: FooBarChildBuilders
    // ...
): Router<Configuration>(/*...*/) {

    sealed class Configuration : Parcelable {
        @Parcelize object Child1 : Content()
        @Parcelize object Child2 : Content()
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        with(builders) { // <-- to access its fields for leaner client code
            when (routing.configuration) {
                is Child1 -> child { child1.build(it) } // <- child1 is directly available
                is Child2 -> child { child2.build(it) } // <- child2 is directly available
            }
        }
}
```

**Why we suggest this pattern**:
- Nicely separates construction of local objects (in `Builder`) and satisfying subtree dependencies
- Makes it easier to use child builders in `Router`
- Simple, no DI framework needed


## Dagger

Functionally equivalent to the `ChildBuilders` approach, but with more rituals. However, if Dagger is your go-to tool, and you're already familiar with it, using it in a tree structure will be already an improvement.

**Pattern to apply**:

1. Create a local `dagger.Module` in every `Rib`. This should be able to create all local objects (such as `Router`, `Interactor` or `Presenter`, etc.)
2. Create a local `dagger.Component`
3. Make this `dagger.Component` interface extend ALL child dependency interfaces. The trick here is that by extending those interfaces, Dagger will want to be able to satisfy all their public methods (as they are now the public API of the `dagger.Component` interface too, by extension). As a result, Dagger will force providing all subtree dependencies.
4. Make this `dagger.Component` have a method to build your `Node`. This should be feasible based on all the dependencies provided.
4. Pass the Rib's `Dependency` to `dagger.Component` when building it
5. Build the `dagger.Component` in the `Builder`, where you can provide `Dependency`. Use the method providing the `Node` to return from your `build` method.


You can find an example of this put to practice in our sample apps.


**We we suggest this pattern**:

- If you're already familiar with Dagger, you might not want to throw it away (we didn't at first, either). It feels it's your lifeline, and you might think it's crazy to get rid of it. That's alright!
- A first step is to use Dagger in a tree structure - it will make scoping so much easier!


**Why we actually don't suggest this over the `ChildBuilders` approach**:

- Dagger's main benefit is giving you a tool to rule over chaos and not go crazy. That chaos usually comes from bloated scopes and not well organised app-wide dependencies. In that environment, Dagger is a life saver, but with a different approach that solves the chaos in the first place, you might not need it at all.
- Dagger's scoping mechanism isn't needed here at all: the tree structure provides scoping by itself without any additional rituals
- Dagger Can make it more difficult to understand / back track where a dependency is coming from (or why one cannot be properly provided). This problem does not exist in the `ChildBuilders` approach, where you get all the benefits of simple, straightforward code.
- Additional code for no additional benefit in this case
- Additional build time
