package com.badoo.ribs.example.rib.menu

import android.support.annotation.IdRes
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.core.Rib
import com.badoo.ribs.example.matcher.withTextColor
import com.badoo.ribs.example.rib.menu.builder.MenuBuilder
import com.badoo.ribs.example.R
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import io.reactivex.observers.TestObserver
import org.junit.Rule
import org.junit.Test

class MenuTest {

    @get:Rule
    val ribsRule = RibsRule(this::buildRib)

    private val menu = MenuView()

    private val menuInput = PublishRelay.create<Menu.Input>()
    private val menuOutput = PublishRelay.create<Menu.Output>()

    @Test
    fun initialState_noElementsAreSelected() {
        menu.assertNothingSelected()
    }

    @Test
    fun selectItemInput_selectsItem() {
        acceptInput(Menu.Input.SelectMenuItem(Menu.MenuItem.HelloWorld))

        menu.helloItem.assertIsSelected()
    }

    @Test
    fun clickOnItem_doesNotSelectItem() {
        menu.dialogsItem.click()

        menu.dialogsItem.assertIsNotSelected()
    }

    @Test
    fun itemClick_producesSelectOutput() {
        val observer = menuOutput.subscribeOnTestObserver()

        menu.fooItem.click()

        observer.assertValue(Menu.Output.MenuItemSelected(Menu.MenuItem.FooBar))
    }

    @Test
    fun selectItemInputTwoTimes_displaysOnlyLastSelection() {
        acceptInput(Menu.Input.SelectMenuItem(Menu.MenuItem.HelloWorld))
        acceptInput(Menu.Input.SelectMenuItem(Menu.MenuItem.FooBar))

        menu.helloItem.assertIsNotSelected()
        menu.fooItem.assertIsSelected()
    }

    private fun acceptInput(input: Menu.Input) = runOnUiThread {
        menuInput.accept(input)
    }

    class MenuView {
        val helloItem = MenuItem(R.id.menu_hello)
        val fooItem = MenuItem(R.id.menu_foo)
        val dialogsItem = MenuItem(R.id.menu_dialogs)

        private val all = listOf(helloItem, fooItem, dialogsItem)

        fun assertNothingSelected() = all.forEach { it.assertIsNotSelected() }
    }

    class MenuItem(@IdRes id: Int) {
        private val matcher = withId(id)

        fun click() {
            onView(matcher).perform(ViewActions.click())
        }

        fun assertIsSelected() {
            onView(matcher).check(matches(withTextColor(R.color.material_blue_grey_950)))
        }
        fun assertIsNotSelected() {
            onView(matcher).check(matches(withTextColor(R.color.material_grey_600)))
        }
    }

    private fun buildRib(ribTestActivity: RibTestActivity) =
        MenuBuilder(object : Menu.Dependency, Rib.Dependency by ribTestActivity {
            override fun menuInput(): ObservableSource<Menu.Input> = menuInput
            override fun menuOutput(): Consumer<Menu.Output> = menuOutput
        }).build()


    private fun <T> Observable<T>.subscribeOnTestObserver() = TestObserver<T>().apply {
        subscribe(this)
    }
}
