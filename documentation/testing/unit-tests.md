# Unit tests

`rib-base-test` module provides multiple helper classes to easily write Unit tests for different RIBs components.

## Router tests

The framework provides `RibRouterTestHelper` to test a router. It accepts `Router`, attaches it to `Node` and moves `Node` to the created state.
Now we can use `RibBuilderStub` instead of real `Child1Builder`, so it will help us to test the logic properly.

```kotlin
class SomeScreenRouterTest {

    // Use RibBuilderStub to return RibNodeStub
    private val child1Builder = RibBuilderStub<Child1Builder.Param, Child1> { params ->
        object : RibNodeStub<RibView>(params), Child1, Connectable<Child1.Input, Child1.Output> by NodeConnector()
    }
    
    // We can use real BackStack as routing source
    private val backStack = BackStack<Configuration>(
        initialConfiguration = Configuration.Content.Empty,
        buildParams = emptyBuildParams(),
    )

    // Router that we test
    private val router = SomeScreenRouter(
        buildParams = emptyBuildParams(),
        routingSource = backStack,
        child1Builder = child1Builder,
    )
    
    // Utility helper that will allow you to invoke all required callbacks in order to setup the Router's state properly
    private val routerTestHelper = RibRouterTestHelper(
        buildParams = emptyBuildParams(),
        router = router,
    )

    @AfterEach
    fun after() {
        routerTestHelper.close()
    }

    @Test
    fun `WHEN resolve Child THEN create Child1 and attach it`() {
        backStack.replace(Configuration.Content.Child)

        // Node was created and attached to the parent
        child1Builder.assertLastNodeState(Lifecycle.State.CREATED)
    }

    @Test
    fun `WHEN resolve Child THEN create Child1 with proper parameter`() {
        backStack.replace(Configuration.Content.Child)

        // Node creation was invoked with following parameter
        child1Builder.assertLastParam(Child1Builder.Param(true))
    }

}
```

Most of the time, `Router` is a pretty simple class that does not have any complex logic to test, but only simple mappings from configuration to builder invocation.
If it is so, then there is no need to cover this class with tests.

## Interactor tests

`Interactor` might have pretty complicated logic related to wiring components like operations on backstack, so it is a great idea to cover it with tests.
To help you with `Interactor` tests the framework provides `RibInteractorTestHelper`.

The following `Interactor` listens to UI events and child RIB output for routing coordination. 

```kotlin
class SomeScreenInteractor(
    buildParams: BuildParams<*>,
    private val backStack: BackStack<Configuration>
) : Interactor<SomeScreen, SomeScreenView>(
    buildParams = buildParams
) {

    override fun onViewCreated(view: SomeScreenView, viewLifecycle: Lifecycle) {
        view.subscribe { event ->
            when (event) {
                SomeScreenView.Event.OpenChildClicked -> backStack.replace(Configuration.Content.Child1)
            }
        }
    }

    override fun onChildBuilt(child: Node<*>) {
        when (child) {
            is Child1 ->
                child
                    .output
                    .subscribe { navigateUp() }
        }
    }

}
```

We can test the logic above with help of the following test.

```kotlin
class SomeScreenInteractorTest {

    // We can use real BackStack as routing source
    private val backStack = BackStack<Configuration>(Configuration.Content.Empty, BuildParams.Empty())
    
    // Interactor that we test
    private val interactor = SomeScreenInteractor(
        buildParams = BuildParams.Empty(),
        feature = feature,
        backStack = backStack,
    )

    // We can replace view implementation with a stub, so we can run this test without Robolectric
    // RibViewStub is suitable for MVI approach, but you can easily create your own View stub just by implementing View interface
    private val view = object : RibViewStub<SomeScreenView.ViewModel, SomeScreenView.Event>(), SomeScreenView {}

    // Utility helper that will allow you to invoke all required callbacks in order to setup the Interactor's state properly
    private val interactorTestHelper = RibInteractorTestHelper(
        interactor = interactor,
        // If it is hard to create an instance of SomeScreenNode, you can extend RibNodeStub and implement SomeScreen interface
        ribFactory = { SomeScreenNode(it, viewFactory = { view }, plugins = emptyList()) }
    )

    @Test
    fun `WHEN open child button is clicked THEN back stack has Child1 configuration`() {
        // Move Interactor to CREATED state
        interactorTestHelper.moveToStateAndCheck(Lifecycle.State.CREATED) {
            // Fake view event
            view.viewEventRelay.accept(SomeScreenView.Event.OpenChildClicked)

            // assertCurrentConfiguration is an extension function to assert current configuration
            backStack.assertCurrentConfiguration(Configuration.Content.Child1)
        }
    }

    @Test
    fun `WHEN child send output THEN navigate up`() {
        // Create stub of Child1 with help of RibNodeStub
        val child = object : RibNodeStub<RibView>(interactorTestHelper.createChildBuildParams()), 
            Child1,
            Connectable<Child1.Input, Child1.Output> by NodeConnector() { }

        interactorTestHelper.moveToStateAndCheck(Lifecycle.State.CREATED) {

            // Attach Child1 to Interactor, Child1 will be in the same lifecycle state as the parent
            interactorTestHelper.attachChild(child)

            // Fake Child1 output
            child.output.accept(Child1.Output.NewDataIsAvailable)

            // Verify that navigate up was requested
            interactorTestHelper.integrationPoint.assertNavigatedUp()
        }
    }

}
```

## View tests

We can also unit test `View` implementation.
In this test we should move `View` to some particular state and verify it by using `Espresso`.
After that we can invoke some actions on `View` and verify that view sends events or invokes callbacks as expected.
It can be easily done with the help of `RibViewTestRule`.

```kotlin
class SomeViewImplTest {

    @get:Rule
    val rule = RibViewTestRule { factoryContext ->
        SomeScreenViewImpl.Factory().invoke(/* dependency */ null).invoke(/* view factory context */ factoryContext)
    }

    @Test
    fun WHEN_has_no_data_THEN_displays_empty_state() {
        rule.view.accept(ViewModel(isEmpty = true))

        onView(withId(R.id.title)).check(matches(withText("No data available")))
    }

    @Test
    fun WHEN_press_refresh_button_THEN_send_refresh_event() {
        rule.view.accept(ViewModel(isEmpty = true))

        val test = rule.view.events.test()

        onView(withId(R.id.title)).perform(click())

        test.assertValue(Event.OpenChildClicked)
    }

}
```
