package com.badoo.ribs.sandbox.rib.menu

import android.os.Bundle
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.badoo.ribs.test.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.sandbox.rib.menu.Menu.Input.SelectMenuItem
import com.badoo.ribs.sandbox.rib.menu.Menu.MenuItem.FooBar
import com.badoo.ribs.sandbox.rib.menu.Menu.MenuItem.HelloWorld
import com.badoo.ribs.sandbox.rib.menu.element.MenuElement
import io.reactivex.Observable
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

    private lateinit var rib: Menu

    private val menu = MenuElement()

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
        val observer = rib.output.subscribeOnTestObserver()

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
        rib.input.accept(input)
    }

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        MenuBuilder(object : Menu.Dependency {
        }).build(root(savedInstanceState)).also {
            rib = it
        }

    private fun <T> Observable<T>.subscribeOnTestObserver() = TestObserver<T>().apply {
        subscribe(this)
    }
}
