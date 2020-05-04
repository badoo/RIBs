package com.badoo.ribs.example.rib.menu

import android.os.Bundle
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.core.builder.BuildContext.Companion.root
import com.badoo.ribs.example.rib.menu.Menu.Input.SelectMenuItem
import com.badoo.ribs.example.rib.menu.Menu.MenuItem.FooBar
import com.badoo.ribs.example.rib.menu.Menu.MenuItem.HelloWorld
import com.badoo.ribs.example.rib.menu.element.MenuElement
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import io.reactivex.observers.TestObserver
import org.junit.Rule
import org.junit.Test

/**
 * UI integration test that checks all RIB components in isolation
 * It interacts with the RIB using real UI (button clicks/swipes) and RIB input,
 * it can make assertions based on UI state and produced output
 */
class MenuTest {

    @get:Rule
    val ribsRule = RibsRule(this::buildRib)

    private val menu = MenuElement()

    private val menuInput = PublishRelay.create<Menu.Input>()
    private val menuOutput = PublishRelay.create<Menu.Output>()

    @Test
    fun initialState_noSelectedElements() {
        menu.assertNothingSelected()
    }

    @Test
    fun selectItemInput_selectsItem() {
        acceptInput(SelectMenuItem(HelloWorld))

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

        observer.assertValue(Menu.Output.MenuItemSelected(FooBar))
    }

    @Test
    fun selectItemInputTwoTimes_displaysOnlyLastSelection() {
        acceptInput(SelectMenuItem(HelloWorld))
        acceptInput(SelectMenuItem(FooBar))

        menu.helloItem.assertIsNotSelected()
        menu.fooItem.assertIsSelected()
    }

    private fun acceptInput(input: Menu.Input) = runOnUiThread {
        menuInput.accept(input)
    }

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        MenuBuilder(object : Menu.Dependency {
            override fun menuInput(): ObservableSource<Menu.Input> = menuInput
            override fun menuOutput(): Consumer<Menu.Output> = menuOutput
        }).build(root(savedInstanceState))

    private fun <T> Observable<T>.subscribeOnTestObserver() = TestObserver<T>().apply {
        subscribe(this)
    }
}
