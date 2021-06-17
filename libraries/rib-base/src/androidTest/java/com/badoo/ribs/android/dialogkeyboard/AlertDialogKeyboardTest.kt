package com.badoo.ribs.android.dialogkeyboard

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.platform.app.InstrumentationRegistry
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.pushOverlay
import com.badoo.ribs.test.RibsRule
import com.badoo.ribs.test.util.ribs.root.TestRoot
import com.badoo.ribs.test.util.ribs.root.TestRootRouter
import com.badoo.ribs.test.util.ribs.root.builder.TestRootBuilder
import com.badoo.ribs.test.util.runOnMainSync
import com.badoo.ribs.test.util.waitFor
import org.junit.Rule
import org.junit.Test
import java.util.UUID

class AlertDialogKeyboardTest {

    @get:Rule
    val ribsRule = RibsRule<Rib>()

    // https://github.com/badoo/RIBs/issues/263
    // Even after focusing and clicking to editText keyboard was hidden
    @Test
    fun keyboardCanBeShownForRIBDialogWithEditText() {
        setupRibs()

        onView(isAssignableFrom(EditText::class.java))
            .inRoot(isDialog())
            .perform(click())

        waitFor {
            val inputManager = InstrumentationRegistry
                .getInstrumentation()
                .targetContext
                .getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            val hasKeyboard = inputManager != null && inputManager.isActive && inputManager.isAcceptingText
            hasKeyboard
        }
    }

    private fun setupRibs() {
        val rootProvider = TestRoot.Provider(
            initialConfiguration = TestRootRouter.Configuration.Content.NoOp
        )

        var backStack: BackStack<TestRootRouter.Configuration>? = null
        ribsRule.start { activity, savedInstanceState ->
            val buildParams = BuildParams(
                payload = TestRootBuilder.Params(true),
                buildContext = BuildContext.root(savedInstanceState),
                identifier = Rib.Identifier(
                    uuid = UUID.randomUUID()
                )
            )

            // SameThreadVerifier will check if it was created on the same thread it will be used on
            backStack = BackStack(
                buildParams = buildParams,
                initialConfiguration = TestRootRouter.Configuration.Content.NoOp
            )

            rootProvider.create(
                buildParams = buildParams,
                dialogLauncher = activity.integrationPoint.dialogLauncher,
                savedInstanceState = savedInstanceState,
                routingSource = backStack!!
            )
        }

        runOnMainSync {
            backStack?.pushOverlay(TestRootRouter.Configuration.Overlay.AttachNode1AsOverlay)
        }
    }

}
