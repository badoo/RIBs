## Problems

- Problems with multiple Activities
- Problems with Fragments
- Problems with opinionated frameworks

## About RIBs
- [incorporate problems here, maybe merge 1, 2, 3, 4]
- [1] Our approach
        ?? Unopinionated
        ?? Plugins-based
        ?? State persistence
        ?? Transitions
- [2] Your application as a tree 
- [3] Divide & conquer
- [4] Tree structure (Node, parent, child, sibling)
  - Complexity
  - DI
  - Testing in isolation
  - Reuse

## Setup

- [Module deps](setup/deps.md)
- [Integration point](setup/integrationpoint.md)

## Basics
- [Terminology](basics/terminology.md)
- [Nodes in detail](basics/nodes.md)
- [Plugins](basics/plugins.md)

## Hello world!
- [Hello world!](helloworld/hello-world.md)
- [Adding a view](helloworld/view.md)
- [Adding business logic](helloworld/business-logic.md)

## Tree structure 101

- [Routing](routing/routing.md)
- [Resolving routing](routing/resolving-routing.md)
- [Back stack](routing/back-stack.md)
- When the back stack is not enough
- Transitions
- Child builders & dependencies
- Communication between Nodes
    - when to handle locally / bubble up
- Deep link workflows

## Transition animations

- [Quick start](transitions/transitions-quick-start.md)
- [Custom transitions](transitions/custom-transitions.md)
- [Transition reversal](transitions/transition-reversal.md)

## Modality & reuse
- Build context
- Compile-time vs build-time dependencies
- Customisations

## Android support

- Dialogs
- Launching external Activities
- Permission requests

## Extras
- Bumble's approach
    - Unidirectional dataflow, Binder
    - Interactor
    - Reactive views
- Demo app
- Tooling
- Template plugin

## Testing

## Experimental features

- Portals
- RecyclerView hosting
- Jetpack Compose compatibility

## Demo app
