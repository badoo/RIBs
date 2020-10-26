# Integration point

When you represent your application (or part of it) using a RIBs tree, you'll need to integrate its root to the rest of your application. This is required so that system events (Android lifecycle, back press, etc.) reach your components in the tree.

You only need to do this for the root of the tree though: all the rest is managed automatically inside the tree.

If you're using Activities to plug your tree into, you can use and extend
the ```RibActivity``` class found in the ```rib-base``` module.

It's ```abstract``` and will require you to:
  1. define a root ViewGroup (where to attach the root of your tree)
  2. implement building your root RIB.

```RibActivity``` does all the rest for you, and should provide an easy way to get started:

```kotlin
class YourActivity : RibActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.your_layout)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Rib =
        TODO("See next chapter")
}

```

You can plug a tree anywhere else other than Activities too, as long as you can forward the required lifecycle methods to your root node.
