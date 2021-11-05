# Routing

## Suggested read

⚠️ Before you proceed, make sure you've read:
- [Happy little trees](happy-little-trees.md)

## What's routing?

In the previous section we discussed representing our application as a tree hierarchy of single-responsibility ```Nodes```:

![](https://i.imgur.com/PWhCnc1.png)

In all parent-child relationships in this diagram, it's the parent's responsibility to decide which of its children it wants to delegate the control flow to:

- Here, active ones are shown with filled yellow components
- All the others ones are in an inactive state, waiting to be activated

Based on business logic, whenever the parent ```Node``` wishes so, it can decide to activate another child. This switching mechanism is what we refer to as routing.


## Routing: a piece of navigation as a local concern

Routing is an alternative to a global navigation pattern: instead, navigation is broken up to "local pieces", without wanting to know too much about the rest of the application.

This, among other things, also solves an age old problem of navigation in a shared module world, where shared modules shouldn't have to know about the hosting application. If navigation is a local concern, then only the direct next "piece" of it needs to present itself as a dependency!


## Navigation represented as state 

Routing describes the state of local navigation in your tree of Nodes.

The combination of these local, stateful navigation concerns uniquely describes the state of your whole application. 

In RIBs, routing is both stateful and persistent, automatically handled by the framework.

