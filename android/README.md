# RIBs
[![Version](https://jitpack.io/v/badoo/RIBs.svg)](https://jitpack.io/#badoo/RIBs)
[![Build Status](https://travis-ci.org/badoo/RIBs.svg?branch=master)](https://travis-ci.org/badoo/RIBs)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

## Why should I care about this?

Practical implementation of ideas presented in this DroidconUK 19 talk: [The immense benefits of not thinking in screens](https://badootech.badoo.com/the-immense-benefits-of-not-thinking-in-screens-6c311e3344a0)

### RIBs as a pattern
#### Simplicity
Break down the complexity of your project into manageable chunks. Compose them together to create abstractions on higher levels. Different levels - same approach.

#### Cohesive, reusable modules
RIB = business logic + UI + all related functionality packaged together, hiding all implementation details. 

#### Screen-agnostic modules
Use the same approach that works on any level of view-hierarchy:
- sections of the screen
- full-screen
- multi-screen flows
- pure business logic without any view

#### Reusability on steroids
Achieve bare minimum coupling: RIBs are not coupled to their parents or siblings, and are easy to move around.

Easily take any RIB and plug it to different screens / apps in a matter of minutes. 

Just provide dependencies to a **Builder**, and you are ready to go. No additional wiring or deep understanding of the internals of a RIB is needed.

#### Routing
Decentralised, local navigation. Every level in a RIB tree decides which subtree to attach. No need for global navigator approach.

#### Deep scope trees
Fine-grained control over object lifetimes. Want to go more specific than a single @Singleton and @Activity scope? RIBs tree lends a natural and easy way to do this.

#### Instumentation testing in isolation
RIBs can be tested in isolation even in instrumentation tests

#### Remote control
Workflows allow you to chain operations of individual RIBs together. You can then trigger workflows from e.g.:
- console
- push notifications from deep links

Use it to easily move your app to specific states.  
 

## About Badoo's fork
Badoo RIBs is an evolution of Uber RIBs, with notable differences and additions

#### Addressing key issues
- Tree structure is saved / restored automatically
- Reduced boilerplate code for `Router` by using routing actions
- Added back stack for routing with back stack operations (push, pop, replace, etc.)
- Back stack is saved / restored automatically
- Separated `View` lifecycle from logical `Node` lifecycle, which adds many new interesting possibilities (e.g. RIBs with business logic still alive in back stack, RIBs hosted in an AlertDialog)
- Support for Android lifecycle events

#### Making it even smoother
- Happily integrated with [MVICore](https://github.com/badoo/MVICore) 
- Enjoy full support for state management with async loading, one-time events, time-travel debugging, and more. Check [documentation](https://badoo.github.io/MVICore/) for a full list features.
- First-class support for unidirectional data-flow: one-liners for connecting endpoints
- Providing customisations for RIBs
- Template generator plugin compiled from live code templates
- Kotlin

## Roadmap to 1.0
[See here](https://github.com/badoo/RIBs/issues/96)

## Tutorials
[See here](tutorials/README.md). Check back later for more, as only basic functionality is covered as of yet. In the meantime, you can also check out the `app-example` module. 

## WIP warning
This is an in-progress, preview version. 

Bugs are probably less of an issue, as the framework is pretty well covered with unit tests, but API can change until 1.0.
