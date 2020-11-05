# Routing

## What's routing?

[screenshot – card swiping game]()

In this example, we have a bottom menu on this screenwith three options: ```My profile```, ```Card swiping game```, ```Messages```. Pressing any of them would show the corresponding screen in the content area. Since we're building our application by building a tree of single-responsibility ```Nodes```, we can represent this with the following structure:

[diagram – container]()

At any given moment, only one of them can be active though:

[diagram – container / anim]()

We can go deeper too! The ```Messages``` screen displays either a ```Conversation list```, or – after selecting one of them – a single ```Chat```:

[screenshot – connections / list]()

[screenshot – connections / chat]()

We can represent this with the following ```Node``` structure:

[diagram – messages / container]()

Using containers in both the above examples allow us to follow a true single responsibility approach: while implementing actual "screens" is delegated to other ```Nodes```, containers keep a single one: deciding when to switch between them.

This switching mechanism is what we refer to as routing.


## Combining individual routings

The two examples above don't exist in their own vacuum though: they are directly connected. We can only see any of the ```Conversation list``` / ```Chat``` when one level higher, the main screen container switches its routing to show: ```Messages```

[diagram - combined]()


In fact, this pattern can be repeated in both directions indefinitely: we can always break up any level to more standalone pieces, and we can put any of the levels as a child under another one serving as a parent:

[diagram - app]()

Routing a pattern really starts to shine when we start to describe the whole application state with it.


## Routing: a piece of navigation as a local concern

Routing is an alternative to a global navigation pattern: instead, navigation is broken up to "local pieces", without wanting to know too much about the rest of the application.

This, among other things, also solves an age old problem of navigation in a shared module world, where shared modules shouldn't have to know about the hosting application. If navigation is a local concern, then only the direct next "piece" of it needs to present itself as a dependency!


## Navigation represented as state 

Routing describes the state of local navigation in your tree of Nodes.

The combination of these local, stateful navigation concerns uniquely describe the state of your whole application. 

In RIBs, routing is both stateful and persistent, automatically handled by the framework.

