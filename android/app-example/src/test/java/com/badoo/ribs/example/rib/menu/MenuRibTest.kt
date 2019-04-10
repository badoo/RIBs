package com.badoo.ribs.example.rib.menu

import android.view.ViewGroup
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.android.PermissionRequester
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.directory.Directory
import com.badoo.ribs.core.directory.ViewCustomisationDirectory
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.rib.menu.builder.MenuBuilder
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import io.reactivex.observers.TestObserver
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class MenuRibTest {

    private val menuViewModel = PublishRelay.create<MenuView.ViewModel>()
    private val menuUiEvents = PublishRelay.create<MenuView.Event>()

    private val menuView: MenuView = object : MenuView,
        Consumer<MenuView.ViewModel> by menuViewModel,
        ObservableSource<MenuView.Event> by menuUiEvents {

        override val androidView: ViewGroup = mock()
    }

    private val menuInput = PublishRelay.create<Menu.Input>()
    private val menuOutput = PublishRelay.create<Menu.Output>()

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
        val viewModelObserver = menuViewModel.subscribeOnTestObserver()

        acceptInput(Menu.Input.SelectMenuItem(Menu.MenuItem.HelloWorld))

        viewModelObserver.assertValue(MenuView.ViewModel(selected = Menu.MenuItem.HelloWorld))
    }

    @Test
    fun itemClickUiEvent_producesSelectOutput() {
        val outputObserver = menuOutput.subscribeOnTestObserver()

        menuUiEvents.accept(MenuView.Event.Select(Menu.MenuItem.FooBar))

        outputObserver.assertValue(Menu.Output.MenuItemSelected(Menu.MenuItem.FooBar))
    }

    @Test
    fun selectItemInputTwoTimes_viewModelContainsOnlyLastSelection() {
        val viewModelObserver = menuViewModel.subscribeOnTestObserver()

        acceptInput(Menu.Input.SelectMenuItem(Menu.MenuItem.HelloWorld))
        acceptInput(Menu.Input.SelectMenuItem(Menu.MenuItem.FooBar))

        assertThat(viewModelObserver.values()).last().isEqualTo(MenuView.ViewModel(Menu.MenuItem.FooBar))
    }

    private fun acceptInput(input: Menu.Input) = menuInput.accept(input)

    private fun buildRib() =
        MenuBuilder(object : Menu.Dependency, Rib.Dependency {
            override fun ribCustomisation(): Directory = ViewCustomisationDirectory().apply {
                put(Menu.Customisation::class, mock {
                    on { viewFactory } doReturn object : ViewFactory<MenuView> {
                        override fun invoke(param: ViewGroup): MenuView = menuView
                    }
                })
            }

            override fun activityStarter(): ActivityStarter = mock()
            override fun permissionRequester(): PermissionRequester = mock()
            override fun dialogLauncher(): DialogLauncher = mock()

            override fun menuInput(): ObservableSource<Menu.Input> = menuInput
            override fun menuOutput(): Consumer<Menu.Output> = menuOutput
        }).build()

    private fun <T> Observable<T>.subscribeOnTestObserver() = TestObserver<T>().apply {
        subscribe(this)
    }
}
