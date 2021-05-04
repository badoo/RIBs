# Your application as a tree

Your view hierarchy is already a tree - RIBs gives you a way to compose your application in a similar way.

Break down the complexity of your project into manageable chunks. Compose them together to create abstractions on higher levels. Different levels - same approach.


## Divide & conquer

When your code gets too complex, you extract methods; when your classes get overloaded with responsibilities, you extract other classes from them. Repeating this pattern we effectively split up code to manageable chunks, and build a tree structure of delegation. Divide & conquer!

Yet we don't usually do the same with "components" (if we can even agree on the definition of them), and as a result, end up with bloated monoliths that are hard to move, maintain, or reuse.

By applying the same divide & conquer approach on components as with methods and classes, we can tame complexity, and keep individual units simple and easy to reuse.


## Activity is the wrong abstraction

An `Activity` always means a full screen worth of stuff – not more, not less. Even if they start simple, they can too complex over time. 

When they do, you can't use divide & conquer on them without breaking the application flow: if you extract an `Activity` from another one, now all that stuff lives on a completely different screen – which is not what you want in this case.


## Similar problems - universal tool

Looking at these use cases, are they really so different?

- Switching from a logged out state to an authenticated one
- Switching to an entirely different screen
- Swiping a view pager
- Swiping an embedded carousel

By abstracting away the notion of screens, from an application flow standpoint all of the above can be represented by containers that directly manipulate their immediate contents. 

So why use completely different tools for them?


## Screen agnostic 

You can think of this library as a universal tool for all the above similar scenarios. It works with the same approach on any level of the view-hierarchy:

- Sections of the screen
- Full-screen components
- Multi-screen flows
- Pure business logic without any view


## Cohesive, reusable modules

Every level in the app tree is its own standalone unit:

RIB = business logic + UI + all related functionality packaged together, hiding all implementation details.


## Reusability on steroids

Achieve bare minimum coupling: RIBs are not coupled to their parents or siblings, and are easy to move around. Just provide dependencies to a **Builder**, and you are ready to go.


## Deep scope trees

Fine-grained control over object lifetimes. Want to go more specific than a single @Singleton and @Activity scope? RIBs tree lends a natural and easy way to do this.


## Navigation as a local concern

If you have a shared modules architecture, navigation as a global concern can be problematic. The concept of routing in a tree structure makes navigation a local concern instead of a global one, so it can remain decoupled and encapsulated. 


## Builders

Compile-time safety over runtime crashes: you construct RIBs yourself, and can leverage constructor dependency injection.


## Instrumentation testing in isolation

RIBs can be tested in isolation even in instrumentation tests.


## Our approach

RIBs as a framework provides you all the fundamental tools you need to benefit from creating tree-structure-based applications, plus lots of nice extras. Meanwhile, it makes no assumption about architectural patterns you'd like to use inside any given RIB: MVP? MVVM? MVI? Something completely different? Your choice.


## The immense benefits of not thinking in screens

You can find more information in our related Droidcon UK '19 talk:

[The immense benefits of not thinking in screens](https://medium.com/bumble-tech/the-immense-benefits-of-not-thinking-in-screens-6c311e3344a0)
