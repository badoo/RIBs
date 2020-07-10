package com.badoo.ribs.sandbox.rib.menu

import android.view.ViewGroup
import com.badoo.ribs.core.customisation.RibCustomisationDirectoryImpl
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.sandbox.rib.menu.Menu.Input.SelectMenuItem
import com.badoo.ribs.sandbox.rib.menu.Menu.MenuItem.FooBar
import com.badoo.ribs.sandbox.rib.menu.Menu.MenuItem.HelloWorld
import com.badoo.ribs.sandbox.rib.menu.Menu.Output.MenuItemSelected
import com.badoo.ribs.sandbox.rib.menu.MenuView.Event.Select
import com.badoo.ribs.sandbox.rib.util.TestView
import com.badoo.ribs.sandbox.rib.util.subscribeOnTestObserver
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
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

    /**
     * Customise viewFactory to return test view
     */
    private val customisation = RibCustomisationDirectoryImpl().apply {
        put(Menu.Customisation::class, mock {
            on { viewFactory } doReturn object : MenuView.Factory {
                override fun invoke(deps: Nothing?): (RibView) -> MenuView = {
                    menuView
                }
            }
        })
    }

    private val rib: Menu = buildRib(customisation)

    @Before
    fun setUp() {
        rib.node.onAttach()
        rib.node.attachToView(mock())
        rib.node.onStart()
        rib.node.onResume()
    }

    @Test
    fun selectItemInput_selectsItem() {
        val viewModelObserver = menuView.viewModel.subscribeOnTestObserver()

        rib.input.accept(SelectMenuItem(HelloWorld))

        viewModelObserver.assertValue(MenuView.ViewModel(selected = HelloWorld))
    }

    @Test
    fun itemClickUiEvent_producesSelectOutput() {
        val outputObserver = rib.output.subscribeOnTestObserver()

        menuView.uiEvents.accept(Select(FooBar))

        outputObserver.assertValue(MenuItemSelected(FooBar))
    }

    @Test
    fun selectItemInputTwoTimes_viewModelContainsOnlyLastSelection() {
        val viewModelObserver = menuView.viewModel.subscribeOnTestObserver()

        rib.input.accept((SelectMenuItem(HelloWorld)))
        rib.input.accept(SelectMenuItem(FooBar))

        assertThat(viewModelObserver.values()).last().isEqualTo(MenuView.ViewModel(FooBar))
    }

    private fun buildRib(customisation: RibCustomisationDirectoryImpl) =
        MenuBuilder(object : Menu.Dependency {}).build(
            root(
                savedInstanceState = null,
                customisations = customisation
            )
        )

    class TestMenuView : TestView<MenuView.ViewModel, MenuView.Event>(), MenuView
}
