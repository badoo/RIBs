# Hello World! 

## Suggested read

⚠️ Before you proceed, make sure you've read:
- [How to integrate a RIB into your app](../setup/integrationpoint.md)
- [What a Node is](nodes.md)

## Creating a RIB – Shortcut: template generation

RIBs are intended to represent a reusable component in the app hierarchy, and as such, can have many different moving parts.

In real life circumstances, you can save a lot of time by using the IntelliJ template generator plugin you can find in this repo: after picking the skeleton for the module, it will generate all the required classes with some basic wiring.

## Creating a RIB – Step by step

For better understanding, let's see how a RIB is built up using its base building blocks, without shortcuts.


## Create your RIB's main interface

A handy way to approach writing new components is to have a public main interface. Let's create ```HelloWorld``` in a new package:


```kotlin
interface HelloWorld : Rib {

    interface Dependency {
        // we'll add some later
    }
}
```

The main interface has the same name as the component itself, and will both:
- serve as a documentation entry point (first thing to check to quickly understand what it's about)
- serve as public API


## Creating a ```Node``` in the wild

Let's create a ```HelloWorldNode``` class:

```kotlin
internal class HelloWorldNode() : Node<Nothing>(
    buildParams = BuildParams.Empty(),
    viewFactory = null
), HelloWorld {

}
```

What's happening here:
- we're extending the ```Node``` class
- the ```Nothing``` type says we don't have a view (yet)
- buildParams: we'll look at this in detail later 
- ```viewFactory = null```: as we don't have a view (yet)

It's also key that we're implementing our main ```HelloWorld``` interface. No methods are defined in that interface (yet), but it's important that if we add any later, we'll have to implement them here. ```HelloWorldNode``` now becomes the embodiment of our component as a black box to the outside world.


## Creating a Builder

As our implementation gets more complex and have more classes, it's essential to keep the callsite lean by not having to create all of those (also, they should be internal implementation details).

To achieve this, we'll have the single entry point of building our ```Nodes``` in ```Builders```:

```kotlin
class HelloWorldBuilder(
    private val dependency: HelloWorld.Dependency
) : SimpleBuilder<HelloWorld>() {

    override fun build(buildParams: BuildParams<Nothing?>): HelloWorld =
        HelloWorldNode()
}
```

What's happening here:
- ```HelloWorldBuilder``` is taking an instance of all the dependencies defined in our main interface
- currently we're not using any dependencies, but it's worth to set this up right from the start
- we're referring to the type of the main interface, ```HelloWorld```, and keeping ```HelloWorldNode``` as an implementation detail


## Building our RIB as a root

Wherever we want to build ```HelloWorld```, now we can use an instance of our new ```HelloWorldBuilder```. Let's use it to plug it into our integration point!

```kotlin
class HelloRibsActivity : RibActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.your_layout)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    private val deps = object : HelloWorld.Dependency {}

    override fun createRib(savedInstanceState: Bundle?): Rib {
        val helloWorldBuilder: HelloWorldBuilder = HelloWorldBuilder(deps)
        val helloWorld: HelloWorld = helloWorldBuilder.build(
            BuildContext.root(savedInstanceState)
        )

        return helloWorld
    }
}

```

What's happening here:
- We're creating an instance of the dependencies (so far it's empty)
- We're creating an instance of the builder using that
- We're calling the ```.build()``` method on the builder and get an instance of ```HelloWorld``` as a result
- We're using it in our integration point, ```HelloRibsActivity``` as the root RIB

Also worth noting:
- The ```.build()``` method takes a ```BuildContext``` parameter too. More about that later.
- This will be also simplified when we start building a tree.


## Saying hello!

To test our code, add these lines to ```HelloWorldNode```, then launch your app:

```kotlin
internal class HelloWorldNode() : Node<Nothing>(
    buildParams = BuildParams.Empty(),
    viewFactory = null
), HelloWorld {
    
    override fun onCreate() {
        super.onCreate()
        Log.d("RIBs", "Hello world!")
    }
}
```

Worth noting:
- This is only for testing purposes now – DON'T get used to adding random business logic to the Node's ```onCreate()``` method!
- We'll see how to add extra concerns without polluting our Node shortly.


## Dependencies

To see how to add some dependencies to our RIB, first let's touch the main interface:

```kotlin
interface HelloWorld : Rib {

    interface Dependency {
        val name: String // <--
    }
}
```

Now we need to satisfy this when we create an instance of ```HelloWorld.Dependency```:

```kotlin
class HelloRibsActivity : RibActivity() {

    // ... remainder the same ...

    private val deps = object : HelloWorld.Dependency {
        override val name: String = "World" // <--
    }

    // ... remainder the same ...
}
```

We can use this in our ```HelloWorldBuilder```:

```kotlin
class HelloWorldBuilder(
    private val dependency: HelloWorld.Dependency
) : SimpleBuilder<HelloWorld>() {

    override fun build(buildParams: BuildParams<Nothing?>): HelloWorld =
        HelloWorldNode(
            greeting = "Hello ${deps.name}!" // <--
        )
}
```

Let's update ```HelloWorldNode``` accordingly:

```kotlin
internal class HelloWorldNode(
    private val greeting: String // <--
) : Node<Nothing>(
    buildParams = BuildParams.Empty(),
    viewFactory = null
), HelloWorld {
    
    override fun onCreate() {
        super.onCreate()
        Log.d("RIBs", greeting) // <--
    }
}
```


## Final notes: black box approach

It's a good practice to keep only two things public:
- The main interface (here ```HelloWorld```)
- The builder (here ```HelloWorldBuilder```)

As you might have noticed, ```HelloWorldNode``` has ```internal``` visibility, and we'll do the same with all components that we add later. This way, our RIB can be a black box, hiding its implementation details. This will help to avoid unnecessary coupling.

















