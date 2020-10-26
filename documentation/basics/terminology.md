# Terminology

### RIB
A self-contained black box of business logic along with its view. RIB as a word refers to this conceptual unit as a whole, not a class.

### Node
The main structural element of a RIB tree. It's also the only fix element of the framework, all other elements are optional.
 
A ```Node``` is analogous to a Fragment in many ways: it might or might not have a view, and it has similar (although simplified) lifecycle events.

#### Tree structure of Nodes
To support delegating responsibilities, ```Nodes``` can have children, which can have their own children, and so on. 

This is similar to Fragments, however, the typical nesting level of ```Node``` tree is encouraged to be a lot deeper than it's usual with Fragments. Also, children are referenced directly instead of a FragmentManager-like mechanism).

#### Root
The topmost ```Node``` in the tree.

#### Leaf Node
A ```Node``` without any further children.

### Plugin
A generic piece of functionality that can be plugged into a Node. 

Typically you will want to implement any architectural patterns and other moving parts as plugins. See more in [Plugins]  

### Builder
A class to build a ```Node``` from its dependencies.

See more in [Building]

### View
Reponsible for rendering a ViewModel and triggering events, nothing more. Since it's just an interface, can also be customised or mocked when testing.

### Interactor
An opinionated (and completely optional) ```Plugin``` to implement business logic in a RIB pattern.

Usually a class to facilitate interaction:
- connects other decoupled pieces (e.g state machines, view)
- reacts to child Node outputs / feeds input to child ```Nodes```
- controls routing 

### Router
An optional but handy ```Plugin``` to manipulate which child ```Nodes``` should be active. 

The combination of the routing states of individual ```Nodes``` describe the state of the application on a high level.

See more in [Routing] 






