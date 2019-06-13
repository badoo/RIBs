# RIBs
## About Badoo's fork
Badoo RIBs is an evolution of Uber RIBs, with notable differences and additions:
- Tree structure is saved / restored automatically
- Reduced boilerplate code for `Router` by using routing actions
- Added back stack for routing with back stack operations (push, pop, replace, etc.)
- Back stack is saved / restored automatically
- Separated `View` lifecycle from logical `Node` lifecycle, which adds many new interesting possibilities (e.g. RIBs with business logic still alive in back stack, RIBs hosted in an AlertDialog)
- Support for Android lifecycle events
- Happily integrated with [MVICore](https://github.com/badoo/MVICore) for state management and reactive bindings
- No more MVP, opinionated towards MVI 
- `Consumer`s and `ObservableSource`s instead of `Listener`s bound with one-liner bindings
- RIB customisations on application-level for shared RIBs that need to look / behave differently
- Template generator plugin compiled from live code templates
- Kotlin

## Tutorials
[See here](tutorials/README.md)

## WIP
This is a heavily in-progress, preview version.

Tutorials are in progress. In the meantime, you can also check out the `app-example` module. 
