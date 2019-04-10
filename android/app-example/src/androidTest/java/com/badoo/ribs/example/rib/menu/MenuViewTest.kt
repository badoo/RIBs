package com.badoo.ribs.example.rib.menu

import android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import android.support.test.rule.ActivityTestRule
import android.view.LayoutInflater
import com.badoo.ribs.example.app.OtherActivity
import com.badoo.ribs.example.rib.menu.element.MenuElement
import com.badoo.ribs.example.R
import io.reactivex.ObservableSource
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MenuViewTest {

    @get:Rule
    val activityRule = ActivityTestRule<OtherActivity>(OtherActivity::class.java)

    private lateinit var menuView: MenuView
    private val menu = MenuElement()

    @Before
    fun setUp() {
        runOnUiThread {
            menuView = LayoutInflater.from(activityRule.activity).inflate(R.layout.rib_menu, null, false) as MenuViewImpl
            activityRule.activity.setContentView(menuView.androidView)
        }
    }

    @Test
    fun emptySelectItem_noSelectedElements() {
        menuView.accept(MenuView.ViewModel(selected = null))

        menu.assertNothingSelected()
    }

    @Test
    fun viewModelWithSelectedItem_selectsItem() {
        menuView.accept(MenuView.ViewModel(selected = Menu.MenuItem.HelloWorld))

        menu.helloItem.assertIsSelected()
    }

    @Test
    fun clickOnItem_doesNotSelectItem() {
        menuView.accept(MenuView.ViewModel(selected = null))

        menu.dialogsItem.click()

        menu.dialogsItem.assertIsNotSelected()
    }

    @Test
    fun itemClick_producesSelectOutput() {
        val observer = menuView.subscribeOnTestObserver()

        menu.fooItem.click()

        observer.assertValue(MenuView.Event.Select(Menu.MenuItem.FooBar))
    }

    @Test
    fun bindViewModelTwoTimes_displaysOnlyLastSelection() {
        menuView.accept(MenuView.ViewModel(selected = Menu.MenuItem.HelloWorld))
        menuView.accept(MenuView.ViewModel(selected = Menu.MenuItem.FooBar))

        menu.helloItem.assertIsNotSelected()
        menu.fooItem.assertIsSelected()
    }

    private fun <T> ObservableSource<T>.subscribeOnTestObserver() = TestObserver<T>().apply {
        subscribe(this)
    }
}
