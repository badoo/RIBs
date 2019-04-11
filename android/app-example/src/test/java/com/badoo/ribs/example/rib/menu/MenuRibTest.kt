package com.badoo.ribs.example.rib.menu

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.directory.Directory
import com.badoo.ribs.core.directory.ViewCustomisationDirectory
import com.badoo.ribs.example.rib.menu.builder.MenuBuilder
import com.badoo.ribs.example.rib.util.StaticViewFactory
import com.badoo.ribs.example.rib.util.TestDefaultDependencies
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

        menuInput.accept(Menu.Input.SelectMenuItem(Menu.MenuItem.HelloWorld))

        viewModelObserver.assertValue(MenuView.ViewModel(selected = Menu.MenuItem.HelloWorld))
    }

    @Test
    fun itemClickUiEvent_producesSelectOutput() {
        val outputObserver = menuOutput.subscribeOnTestObserver()

        menuView.uiEvents.accept(MenuView.Event.Select(Menu.MenuItem.FooBar))

        outputObserver.assertValue(Menu.Output.MenuItemSelected(Menu.MenuItem.FooBar))
    }

    @Test
    fun selectItemInputTwoTimes_viewModelContainsOnlyLastSelection() {
        val viewModelObserver = menuView.viewModel.subscribeOnTestObserver()

        menuInput.accept((Menu.Input.SelectMenuItem(Menu.MenuItem.HelloWorld)))
        menuInput.accept(Menu.Input.SelectMenuItem(Menu.MenuItem.FooBar))

        assertThat(viewModelObserver.values()).last().isEqualTo(MenuView.ViewModel(Menu.MenuItem.FooBar))
    }

    private fun buildRib() =
        MenuBuilder(object : Menu.Dependency, Rib.Dependency by TestDefaultDependencies() {
            override fun ribCustomisation(): Directory = ViewCustomisationDirectory().apply {
                put(Menu.Customisation::class, mock {
                    on { viewFactory } doReturn StaticViewFactory<MenuView>(menuView)
                })
            }

            override fun menuInput(): ObservableSource<Menu.Input> = menuInput
            override fun menuOutput(): Consumer<Menu.Output> = menuOutput
        }).build()

    class TestMenuView : TestView<MenuView.ViewModel, MenuView.Event>(), MenuView
}
