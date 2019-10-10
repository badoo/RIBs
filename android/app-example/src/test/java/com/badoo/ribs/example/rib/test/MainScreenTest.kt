package com.badoo.ribs.example.rib.test

import android.app.Activity.RESULT_OK
import android.app.Instrumentation
import android.content.Intent
import android.view.ViewGroup
import com.badoo.ribs.android.CanProvideActivityStarter
import com.badoo.ribs.android.CanProvidePermissionRequester
import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.portal.AncestryInfo
import com.badoo.ribs.customisation.CanProvidePortal
import com.badoo.ribs.customisation.RibCustomisationDirectory
import com.badoo.ribs.customisation.RibCustomisationDirectoryImpl
import com.badoo.ribs.dialog.CanProvideDialogLauncher
import com.badoo.ribs.example.app.OtherActivity
import com.badoo.ribs.example.rib.dialog_example.DialogExample
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView
import com.badoo.ribs.example.rib.foo_bar.FooBar
import com.badoo.ribs.example.rib.foo_bar.FooBarView
import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.hello_world.HelloWorldView
import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.menu.MenuView
import com.badoo.ribs.example.rib.switcher.Switcher
import com.badoo.ribs.example.rib.switcher.SwitcherView
import com.badoo.ribs.example.rib.switcher.builder.SwitcherBuilder
import com.badoo.ribs.example.rib.util.TestDefaultDependencies
import com.badoo.ribs.example.rib.util.TestView
import com.badoo.ribs.example.rib.util.component
import com.badoo.ribs.example.rib.util.subscribeOnTestObserver
import com.badoo.ribs.example.util.CoffeeMachine
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Integration test of multiple RIBs that doesn't include real UI
 * It is similar to single RIB integration test without real view implementation but includes RIB subtree.
 * It may be helpful if you want to test behaviour of multiple RIBs in composition.
 */
@RunWith(RobolectricTestRunner::class)
class MainScreenTest {

    private val dependencies = TestDefaultDependencies()

    private val menuView = TestMenuView()
    private val switcherView = TestSwitcherView()
    private val dialogExampleView = TestDialogExampleView()
    private val fooBarView = TestFooBarView()
    private val helloWorldView = TestHelloWorldView()

    private val rootRib: Node<SwitcherView> = buildRootRib()

    @Before
    fun setUp() {
        rootRib.onAttach()
        rootRib.attachToView(mock())
        rootRib.onStart()
        rootRib.onResume()

        dependencies
            .activityStarter
            .stubResponse(component<OtherActivity>(), Instrumentation.ActivityResult(RESULT_OK, null))
    }

    @Test
    fun openHelloSectionAndClickButton_launchesActivity() {
        menuView.uiEvents.accept(MenuView.Event.Select(Menu.MenuItem.HelloWorld))
        helloWorldView.uiEvents.accept(HelloWorldView.Event.ButtonClicked)

        dependencies.activityStarter.assertIntents {
            last().has(component<OtherActivity>())
        }
    }

    @Test
    fun openHelloSectionAndClickButton_displaysReturnedDataFromActivity() {
        val helloWorldViewModelObserver = helloWorldView.viewModel.subscribeOnTestObserver()
        dependencies.activityStarter.stubResponse(component<OtherActivity>(), Instrumentation.ActivityResult(
            RESULT_OK,
            Intent().apply { putExtra("foo", 1234) }
        ))

        menuView.uiEvents.accept(MenuView.Event.Select(Menu.MenuItem.HelloWorld))
        helloWorldView.uiEvents.accept(HelloWorldView.Event.ButtonClicked)

        assertThat(helloWorldViewModelObserver.values()).last().isEqualTo(HelloWorldView.ViewModel("Data returned: 1234"))
    }

    private fun buildRootRib() =
        SwitcherBuilder(object : Switcher.Dependency,
            CanProvideActivityStarter by dependencies,
            CanProvidePermissionRequester by dependencies,
            CanProvideDialogLauncher by dependencies,
            CanProvidePortal by dependencies {

            override fun coffeeMachine(): CoffeeMachine =
                mock()

            override fun ribCustomisation(): RibCustomisationDirectory = RibCustomisationDirectoryImpl().apply {
                put(Menu.Customisation::class, mock {
                    on { viewFactory } doReturn object : MenuView.Factory {
                        override fun invoke(deps: Nothing?): (ViewGroup) -> MenuView = {
                            menuView
                        }
                    }
                })
                put(Switcher.Customisation::class, mock {
                    on { viewFactory } doReturn object : SwitcherView.Factory {
                        override fun invoke(deps: SwitcherView.Dependency): (ViewGroup) -> SwitcherView = {
                            switcherView
                        }
                    }
                })
                put(DialogExample.Customisation::class, mock {
                    on { viewFactory } doReturn object : DialogExampleView.Factory {
                        override fun invoke(deps: Nothing?): (ViewGroup) -> DialogExampleView = {
                            dialogExampleView
                        }
                    }
                })
                put(FooBar.Customisation::class, mock {
                    on { viewFactory } doReturn object : FooBarView.Factory {
                        override fun invoke(deps: Nothing?): (ViewGroup) -> FooBarView = {
                            fooBarView
                        }
                    }
                })
                put(HelloWorld.Customisation::class, mock {
                    on { viewFactory } doReturn object : HelloWorldView.Factory {
                        override fun invoke(deps: Nothing?): (ViewGroup) -> HelloWorldView = {
                            helloWorldView
                        }
                    }
                })
            }
        }).build(
            BuildContext.Params(
                ancestryInfo = AncestryInfo.Root,
                savedInstanceState = null
            )
        )

    class TestMenuView : TestView<MenuView.ViewModel, MenuView.Event>(), MenuView

    class TestFooBarView : TestView<FooBarView.ViewModel, FooBarView.Event>(), FooBarView

    class TestDialogExampleView : TestView<DialogExampleView.ViewModel, DialogExampleView.Event>(), DialogExampleView

    class TestHelloWorldView : TestView<HelloWorldView.ViewModel, HelloWorldView.Event>(), HelloWorldView

    class TestSwitcherView : TestView<SwitcherView.ViewModel, SwitcherView.Event>(),
        SwitcherView
}
