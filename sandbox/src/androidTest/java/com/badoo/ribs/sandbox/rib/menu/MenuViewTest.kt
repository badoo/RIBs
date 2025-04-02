package com.badoo.ribs.sandbox.rib.menu

import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.badoo.ribs.sandbox.rib.menu.Menu.MenuItem.FooBar
import com.badoo.ribs.sandbox.rib.menu.Menu.MenuItem.HelloWorld
import com.badoo.ribs.sandbox.rib.menu.element.MenuElement
import com.badoo.ribs.test.view.RibsViewRule
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Rule
import org.junit.Test

/**
 * UI test that checks only view part of menu RIB
 * It may be useful in case of complex view or if it is expensive
 * to cover all required cases for UI part with higher level
 * tests like UI integration test for the whole RIB
 * In this test we construct and apply a view model to the view and then
 * we make assertions on real UI elements using Espresso
 */
class MenuViewTest {

    @get:Rule
    val rule = RibsViewRule {
        MenuViewImpl.Factory()(null).invoke(it)
    }

    private val menu = MenuElement()

    @Test
    fun emptySelectItem_noSelectedElements() {
        runOnUiThread { rule.view.accept(MenuView.ViewModel(selected = null)) }

        menu.assertNothingSelected()
    }

    @Test
    fun viewModelWithSelectedItem_selectsItem() {
        runOnUiThread { rule.view.accept(MenuView.ViewModel(selected = HelloWorld)) }

        menu.helloItem.assertIsSelected()
    }

    @Test
    fun clickOnItem_doesNotSelectItem() {
        runOnUiThread { rule.view.accept(MenuView.ViewModel(selected = null)) }

        menu.dialogsItem.click()

        menu.dialogsItem.assertIsNotSelected()
    }

    @Test
    fun itemClick_producesSelectOutput() {
        val observer = rule.view.subscribeOnTestObserver()

        menu.fooItem.click()

        observer.assertValue(MenuView.Event.Select(FooBar))
    }

    @Test
    fun bindViewModelTwoTimes_displaysOnlyLastSelection() {
        runOnUiThread {
            rule.view.accept(MenuView.ViewModel(selected = HelloWorld))
            rule.view.accept(MenuView.ViewModel(selected = FooBar))
        }

        menu.helloItem.assertIsNotSelected()
        menu.fooItem.assertIsSelected()
    }

    private fun <T : Any> ObservableSource<T>.subscribeOnTestObserver() = TestObserver<T>().apply {
        subscribe(this)
    }
}
