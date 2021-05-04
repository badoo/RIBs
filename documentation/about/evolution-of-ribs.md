# Evolution of RIBs

This library was originally inspired by Uber's RIBs. However, it has evolved so much during the years, that it's now something distinctively different.

It would be easier to say what's still common than what's different:

- We also have `Builders` and `Routers` (though the latter in our case are very different)
- We also represent the application in a tree structure as a core idea

But that's probably about it.

If you liked Uber's RIBs (or if you didn't!), our library should be a meaningful evolution of it, with many appealing features:

- Agnostic of architectural patterns - even RIBs as a pattern (Router, Interactor, Builder) is not enforced
- Declarative routing
- Reversible transition animations run by the framework
- Tree structure is saved / restored automatically
- Back stack with extensible operations (push, pop, replace, etc.)
- Separated `View` lifecycle from logical `Node` lifecycle
- Focusing directly on a closer Android integration

We've put lots of love and effort into this framework. Check out all the great features in the [Documentation](../index.md) main page! 

