# RIBs
[![Version](https://jitpack.io/v/badoo/RIBs.svg)](https://jitpack.io/#badoo/RIBs)
[![Build Status](https://travis-ci.org/badoo/RIBs.svg?branch=master)](https://travis-ci.org/badoo/RIBs)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

## Roadmap to 1.0
[See here](https://github.com/badoo/RIBs/issues/96)

## Documentation
[See here](documentation/index.md)

## What's this project about?

It's a practical implementation of the ideas presented in our DroidconUK 19 talk: 

[The immense benefits of not thinking in screens](https://medium.com/bumble-tech/the-immense-benefits-of-not-thinking-in-screens-6c311e3344a0)

### RIBs as a pattern
#### Single-activity approach
Your view hierarchy is already a tree - RIBs gives you a way to compose your application in a similar way.

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
Achieve bare minimum coupling: RIBs are not coupled to their parents or siblings, and are easy to move around. Just provide dependencies to a **Builder**, and you are ready to go.

#### Deep scope trees
Fine-grained control over object lifetimes. Want to go more specific than a single @Singleton and @Activity scope? RIBs tree lends a natural and easy way to do this.

#### Instumentation testing in isolation
RIBs can be tested in isolation even in instrumentation tests
 
## About our fork
Badoo RIBs is an evolution of Uber RIBs, with notable differences and additions

#### Addressing key issues
- Tree structure is saved / restored automatically
- Back stack with extensible operations (push, pop, replace, etc.)
- Separated `View` lifecycle from logical `Node` lifecycle

#### Making it even smoother
- Declarative routing
- Focusing directly on a closer Android integration
- Tons of great features. Have a look in the full [Documentation](documentation/index.md)

## WIP warning
This is an in-progress, preview version.

Bugs are probably less of an issue, as the framework is pretty well covered with unit tests, but API can change until 1.0.

