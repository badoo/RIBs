# Node

```Nodes``` are the main structural element of a RIB tree. 
 
A ```Node``` is analogous to a Fragment in many ways: it might or might not have a view, and it has similar (although simplified) lifecycle events.


## Tree structure of Nodes
To support delegating responsibilities, ```Nodes``` can have children, which can have their own children, and so on. 

This is similar to nesting Fragments, however, the typical nesting level of ```Node``` tree is encouraged to be a lot deeper than it's usual with Fragments. Also, children are referenced directly instead of a FragmentManager-like mechanism).


## Base functionality
Node as a base class provides these functionalities:
- has a lifecycle
- tree operations (attach/detach child and child view)
- can host generic [Plugins] to extract all extra concerns to


## Client code specific functionality
Furthermore, your Node child class can build on top of the to implement any operations defined in the current RIB's interface. These operations are the public API of your RIB as a whole, hiding the implementation details inside. 

See more in [Workflows]().


## Responsibilities
To avoid creating a god object similar to what can happen with Activities or Fragments, we consciously try to keep all extra responsibilities out of them. (On the framework side this means they don't have direct access to ```Context``` among other things).

On the client code side we highly encourage to extract all extra concerns into either dedicated classes, or delegate them to children in the tree.

