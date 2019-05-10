package com.badoo.ribs.example.rib.menu

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.customisation.CanProvideRibCustomisation
import com.badoo.ribs.core.customisation.RibCustomisationDirectory
import com.badoo.ribs.core.customisation.RibCustomisationDirectoryImpl
import com.badoo.ribs.example.rib.menu.Menu.Input.SelectMenuItem
import com.badoo.ribs.example.rib.menu.Menu.MenuItem.FooBar
import com.badoo.ribs.example.rib.menu.Menu.MenuItem.HelloWorld
import com.badoo.ribs.example.rib.menu.Menu.Output.MenuItemSelected
import com.badoo.ribs.example.rib.menu.MenuView.Event.Select
import com.badoo.ribs.example.rib.menu.builder.MenuBuilder
import com.badoo.ribs.example.rib.util.StaticViewFactory
import com.badoo.ribs.example.rib.util.TestView
import com.badoo.ribs.example.rib.util.subscribeOnTestObserver
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

/**
 * Integration test that checks composition of all RIB components except view
 * It is similar to UI integration test but instead of using real UI
 * we use test implementation: we do interactions using UI events and
 * assertions based on view model emissions
 * It runs a lot faster than UI test as it doesn't require real device or emulator
 */
class MenuRibTest {

    private val menuView = TestMenuView()

    private val menuInput: Relay<Menu.Input> = PublishRelay.create<Menu.Input>()
    private val menuOutput: Relay<Menu.Output> = PublishRelay.create<Menu.Output>()

    private val rib: Node<MenuView> = buildRib()

    @Before
    fun setUp() {
        rib.onAttach(null)
        rib.attachToView(mock())
        rib.onStart()
        rib.onResume()
    }

    @Test
    fun selectItemInput_selectsItem() {
        val viewModelObserver = menuView.viewModel.subscribeOnTestObserver()

        menuInput.accept(SelectMenuItem(HelloWorld))

        viewModelObserver.assertValue(MenuView.ViewModel(selected = HelloWorld))
    }

    @Test
    fun itemClickUiEvent_producesSelectOutput() {
        val outputObserver = menuOutput.subscribeOnTestObserver()

        menuView.uiEvents.accept(Select(FooBar))

        outputObserver.assertValue(MenuItemSelected(FooBar))
    }

    @Test
    fun selectItemInputTwoTimes_viewModelContainsOnlyLastSelection() {
        val viewModelObserver = menuView.viewModel.subscribeOnTestObserver()

        menuInput.accept((SelectMenuItem(HelloWorld)))
        menuInput.accept(SelectMenuItem(FooBar))

        assertThat(viewModelObserver.values()).last().isEqualTo(MenuView.ViewModel(FooBar))
    }

    private fun buildRib() =
        MenuBuilder(object : Menu.Dependency, CanProvideRibCustomisation {
            override fun ribCustomisation(): RibCustomisationDirectory = RibCustomisationDirectoryImpl().apply {
                put(Menu.Customisation::class, mock {
                    on { viewFactory } doReturn StaticViewFactory<MenuView>(menuView)
                })
            }

            override fun menuInput(): ObservableSource<Menu.Input> = menuInput
            override fun menuOutput(): Consumer<Menu.Output> = menuOutput
        }).build()

    class TestMenuView : TestView<MenuView.ViewModel, MenuView.Event>(), MenuView
}
