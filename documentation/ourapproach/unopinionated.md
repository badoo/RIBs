# RIBs is unopinionated

```Node``` is really the only fundamental a building block of the framework – all other components are optional and up to you.

While we have our own taste how we like to build components, we believe you should have the freedom to decide what's best for you yourself, and not force any patterns on the client code.

As you will see in later chapters, we recommend using the RIB pattern (Router - Interactor - Builder). Inside Bumble, we also like to use MVICore and Binder for state updates and scoped reactive coupling of different parts (you can see that in the sandbox and app-example modules).

But we also wanted you to have the freedom to go with whatever is best for you: as much as the framework cares, you can go with any architectural patterns or other libraries. MVP or MVI or MVVM or MVXYZ, Binder or LiveData, RxJava or Coroutines, Dagger or Toothpick or manual DI – anything fits nicely. You can also use our Router for handling children easily, or you can manipulate them your own way.

For all that a ```Node``` cares, all of these parts are only ```Plugin```s, and you can extend the system freely.
