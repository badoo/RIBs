# Base concepts

## Terminology

This section contains the suggested knowledge before starting with the tutorials. Don't worry if not everything is clear, you will get the "Aha!" moment along the way.

### `RIB`
A self-contained black box of business logic along with its view. RIB as a word refers to this conceptual unit as a whole, not a class. On the practical level, RIBs have the following main components: `Builder`, `Node`, `Router`, `Interactor`, `View` (optional).

### `Builder`
A class responsible for the building operation: `(Dependencies) -> Node`.

### `Node`
The physical embodiment of a RIB, the actual result of the `Builder`'s build operation. A `Node` is responsible for:
- implementing the lifecycle of the RIB as a whole 
- keeping reference to its immediate child `Node`s + propagating lifecycle methods to them 
- implementing all tree operations (attach/detach child and child view) automatically

### `Router`
Implements high-level routing logic by:
- defining a set of possible configurations the RIB can be in
- resolving those configurations to `RoutingActions`, such as (but not limited to) attaching another `Node` as a child
- maintains an automatically persisted back stack of configurations, and offers and API to manipulate it (operations like push, pop, etc.)

### `Interactor`
Implements business logic:
- connects reactive pieces (e.g state machines, view, inputs and outputs of children)
- can set the RIB in any pre-defined configuration via calling on operations offered by `Router` 

### `View`
Reponsible for rendering a ViewModel and triggering events, nothing more. Since it's just an interface, can also be customised or mocked when testing.





