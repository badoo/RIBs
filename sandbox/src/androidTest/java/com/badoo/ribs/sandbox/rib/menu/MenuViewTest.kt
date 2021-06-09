package com.badoo.ribs.sandbox.rib.menu

import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.rule.ActivityTestRule
import com.badoo.ribs.android.AndroidRibViewHost
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.sandbox.app.OtherActivity
import com.badoo.ribs.sandbox.rib.menu.Menu.MenuItem.FooBar
import com.badoo.ribs.sandbox.rib.menu.Menu.MenuItem.HelloWorld
import com.badoo.ribs.sandbox.rib.menu.element.MenuElement
import io.reactivex.ObservableSource
import io.reactivex.observers.TestObserver
import org.junit.Before
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
    val activityRule = ActivityTestRule<OtherActivity>(OtherActivity::class.java)

    private lateinit var menuView: MenuView
    private val menu = MenuElement()

    @Before
    fun setUp() {
        runOnUiThread {
            menuView = MenuViewImpl.Factory()(null).invoke(
                ViewFactory.Context(
                    parent = AndroidRibViewHost(
                        activityRule.activity.findViewById(android.R.id.content)
                    ),
                    lifecycle = activityRule.activity.lifecycle
                )
            )
            activityRule.activity.setContentView(menuView.androidView)
        }
    }

    @Test
    fun emptySelectItem_noSelectedElements() {
        runOnUiThread { menuView.accept(MenuView.ViewModel(selected = null)) }

        menu.assertNothingSelected()
    }

    @Test
    fun viewModelWithSelectedItem_selectsItem() {
        runOnUiThread { menuView.accept(MenuView.ViewModel(selected = HelloWorld)) }

        menu.helloItem.assertIsSelected()
    }

    @Test
    fun clickOnItem_doesNotSelectItem() {
        runOnUiThread { menuView.accept(MenuView.ViewModel(selected = null)) }

        menu.dialogsItem.click()

        menu.dialogsItem.assertIsNotSelected()
    }

    @Test
    fun itemClick_producesSelectOutput() {
        val observer = menuView.subscribeOnTestObserver()

        menu.fooItem.click()

        observer.assertValue(MenuView.Event.Select(FooBar))
    }

    @Test
    fun bindViewModelTwoTimes_displaysOnlyLastSelection() {
        runOnUiThread {
            menuView.accept(MenuView.ViewModel(selected = HelloWorld))
            menuView.accept(MenuView.ViewModel(selected = FooBar))
        }

        menu.helloItem.assertIsNotSelected()
        menu.fooItem.assertIsSelected()
    }

    private fun <T> ObservableSource<T>.subscribeOnTestObserver() = TestObserver<T>().apply {
        subscribe(this)
    }
}
