package com.badoo.ribs.example.rib.test

import android.app.Activity.RESULT_OK
import android.app.Instrumentation
import android.content.Intent
import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.directory.Directory
import com.badoo.ribs.core.directory.ViewCustomisationDirectory
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
import com.badoo.ribs.example.rib.util.StaticViewFactory
import com.badoo.ribs.example.rib.util.TestDefaultDependencies
import com.badoo.ribs.example.rib.util.TestView
import com.badoo.ribs.example.rib.util.component
import com.badoo.ribs.example.rib.util.subscribeOnTestObserver
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

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
        rootRib.onAttach(null)
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
        SwitcherBuilder(object : Switcher.Dependency, Rib.Dependency by dependencies {

            override fun ribCustomisation(): Directory = ViewCustomisationDirectory().apply {
                put(Menu.Customisation::class, mock {
                    on { viewFactory } doReturn StaticViewFactory<MenuView>(menuView)
                })
                put(Switcher.Customisation::class, mock {
                    on { viewFactory } doReturn StaticViewFactory<SwitcherView>(switcherView)
                })
                put(DialogExample.Customisation::class, mock {
                    on { viewFactory } doReturn StaticViewFactory<DialogExampleView>(dialogExampleView)
                })
                put(FooBar.Customisation::class, mock {
                    on { viewFactory } doReturn StaticViewFactory<FooBarView>(fooBarView)
                })
                put(HelloWorld.Customisation::class, mock {
                    on { viewFactory } doReturn StaticViewFactory<HelloWorldView>(helloWorldView)
                })
            }

            override fun switcherInput(): ObservableSource<Switcher.Input> = Observable.empty()
            override fun switcherOutput(): Consumer<Switcher.Output> = Consumer { }

        }).build()

    class TestMenuView : TestView<MenuView.ViewModel, MenuView.Event>(), MenuView

    class TestFooBarView : TestView<FooBarView.ViewModel, FooBarView.Event>(), FooBarView

    class TestDialogExampleView : TestView<DialogExampleView.ViewModel, DialogExampleView.Event>(), DialogExampleView

    class TestHelloWorldView : TestView<HelloWorldView.ViewModel, HelloWorldView.Event>(), HelloWorldView

    class TestSwitcherView : TestView<SwitcherView.ViewModel, SwitcherView.Event>(), SwitcherView {
        override val menuContainer: ViewGroup = mock()
        override val blockerContainer: ViewGroup = mock()
        override val contentContainer: ViewGroup = mock()
    }
}
