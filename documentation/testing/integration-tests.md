# Integration tests

The framework provides multiple ways of doing integration testing

## Rib test

Running integration tests on emulators can take a lot of time and we want to avoid it as much as possible.
Most of `Rib` business logic can be tested without usage of emulators or `Robolectric`.
We can create an instance of a `Rib` by instantiating its `Node` with the help of associated `Builder` in test.
Then we can replace a `View` implementation of a `RIB` with a stub while keeping every other internal component intact.
We can also mock or stub all external dependencies of a `RIB` to make sure that we do not use any Android related classes.
In the end, we will be able to launch a `Rib` in JVM environment.
Because we do not have any integration point like `Activity` or `Fragment`, we need to control the lifecycle of `Rib` manually with help of `RibNodeTestHelper`.
The `View` implementation, that we excluded from a test, can be tested separately.

```kotlin
class SomeScreenNodeTest {

    // Test implementations of network and analytics components, that allow to stub results and verify function invocations
    private val testTracker = TestAnalyticsTracker()
    private val testNetwork = TestNetwork()

    // We can replace view implementation with a stub, so we can run this test without Robolectric
    // RibViewStub is suitable for MVI approach, but you can easily create your own View stub by implementing View interface
    private val view = object : RibViewStub<SomeScreenView.ViewModel, SomeScreenView.Event>(), SomeScreenView {}

    // Instantiate the node under test, with the help of its builder, and provide the test implementation dependencies
    private val node = SomeScreenBuilder(
        dependency = object : SomeScreen.Dependency {
            override val network: Network = testNetwork
            override val analyticsTracker: AnalyticsTracker = testTracker
        }
    ).build(
        buildContext = BuildContext.root(
            savedInstanceState = null,
            // Override the view implementation with our previously instantiated stub
            customisations = SomeScreen.Customisation { { view } }.toDirectory()
        )
    )

    // Utility helper that will allow you to invoke all required callbacks in order to setup the Node's state properly
    private val nodeTestHelper = RibNodeTestHelper(node)

    @Test
    fun `GIVEN not loading WHEN click button THEN display loading state`() {
        // Move Node to STARTED state
        nodeTestHelper.moveToStateAndCheck(Lifecycle.State.STARTED) {

            // Fake user actions on UI
            view.viewEventRelay.accept(SomeScreenView.Event.ButtonPressed)

            // Assert that View received correct state to render
            assertThat(view.lastViewModel).isEqualTo(SomeScreenView.ViewModel(isLoading = true))
        }
    }

    @Test
    fun `GIVEN not loading WHEN input SomeInput THEN output SomeOutput`() {
        // Observe outputs
        val outputTest = node.output.test()

        nodeTestHelper.moveToStateAndCheck(Lifecycle.State.STARTED) {
            // Send some input
            node.input.accept(SomeScreen.Input.SomeInput)

            // Verify outputs
            outputTest.assertValue(SomeScreen.Output.SomeOutput)
        }
    }

}
```

In case of `Rib` having children, we can replace child builders with stubs.
Create `@VisibleForTests` internal constructor and use it to replace child builders.

```kotlin
class SomeScreenBuilder @VisibleForTesting internal constructor(
    private val dependency: SomeScreen.Dependency,
    private val childBuilders: SomeScreenBuilders?
) : SimpleBuilder<SomeScreen>() {

    constructor(dependency: Tab1.Dependency) : this(
        dependency = dependency,
        childBuilders = null,
    )

    override fun build(buildParams: BuildParams<Nothing?>): SomeScreen {
        ...
        val router = SomeScreenRouter(
            buildParams = buildParams,
            routingSource = backStack,
            // Use real builders or provided by test ones
            builders = childBuilders ?: SomeScreenBuildersImpl(dependency)
        )
        ...
    }
```

Then use the same technique to create stubs and use them to verify inputs and to send outputs.

```kotlin
class SomeScreenNodeTest {

    ...

    // Use RibBuilderStub to return RibNodeStub
    private val child1Builder = RibBuilderStub<Child1Builder.Param, Child1> { params ->
        object : RibNodeStub<RibView>(params), Child1, Connectable<Child1.Input, Child1.Output> by NodeConnector()
    }    

    // Use internal testing constructor
    private val node = SomeScreenBuilder(
        dependency = object : SomeScreen.Dependency {
            override val network: Network = testNetwork
            override val analyticsTracker: AnalyticsTracker = testTracker
        },
        childBuilders = object : SomeScreenBuilders {
            override val child1 = child1Builder
        }
    ).build(
        buildContext = BuildContext.root(
            savedInstanceState = null,
            customisations = SomeScreen.Customisation { { view } }.toDirectory()
        )
    )

    ...

    @Test
    fun `GIVEN Child1 is attached WHEN Child1 THEN output SomeOutput`() {
        nodeTestHelper.moveToStateAndCheck(Lifecycle.State.STARTED) {
            val test = child1Builder.last?.input?.test()

            // Fake Child1 ouput
            child1Builder.last?.output?.accept(Child1.Output.SomeThing)

            // Verify Child1 input
            test.assertValue(Child1.Input.SomeThing)

            // Verify Child1 was dettached and destroyed
            child1Builder.assertLastNodeState(Lifecycle.State.DESTROYED)
        }
    }

}
```

## UI Tests

The framework also provides a way to write UI integration tests for `Node` implementation.
You can use `RibsRule` to write the same test as above, but with real view implementation.
You are free to use both fake and real children to test different levels of integration.
When you fake children, then use `RibBuilderStub` to send inputs and verify outputs.
When you use real children, then use `Espresso` and stubs to move both children and parent into desired state.

```kotlin
class SomeScreenTest {

    @get:Rule
    val ribsRule = RibsRule()

    // Send inputs to the Rib
    private val input = PublishRelay.create<EmptyScreen.Input>()
    // Verify outputs from the Rib
    private val output = PublishRelay.create<EmptyScreen.Output>()

    private fun buildRib(activity: RibTestActivity, bundle: Bundle?) =
        // We do not replace any children in this test, but you are free to do it
        SomeScreenBuilder(
            dependency = object : SomeScreen.Dependency {
                override val network: Network = ...
                override val analyticsTracker: AnalyticsTracker = ...
            },
        ).build(root(bundle)).also {
            // Wire inputs and outputs
            input.subscribe(it.input)
            it.output.subscribe(output)
        }

    @Before
    fun before() {
        ribsRule.start { activity, bundle -> buildRib(activity, bundle) }
    }

    @Test
    fun WHEN_press_refresh_button_THEN_send_SomeOuput() {
        val outputTest = output.test()

        onView(withId(R.id.title)).perform(click())

        outputTest.assertValue(SomeScreen.Output.SomeOutput)
    }

}
```
