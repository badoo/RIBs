package com.badoo.ribs.example.app_bar

import android.content.Context
import android.os.Bundle
import androidx.test.platform.app.InstrumentationRegistry
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.example.FakeUserRepository
import com.badoo.ribs.example.MY_ID
import com.badoo.ribs.example.MY_USER
import com.badoo.ribs.example.R
import com.badoo.ribs.example.element.AppBarElement
import com.badoo.ribs.example.repository.UserRepository
import com.badoo.ribs.example.rule.FakeImageLoaderRule
import io.reactivex.observers.TestObserver
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

class AppBarTest {

    private val fakeImageLoaderRule: FakeImageLoaderRule = FakeImageLoaderRule()

    private val ribsRule = RibsRule { activity, savedInstanceState ->
        buildRib(activity, savedInstanceState)
    }

    @get:Rule
    val ruleChain: RuleChain =
        RuleChain
            .outerRule(fakeImageLoaderRule)
            .around(ribsRule)

    private lateinit var outputTest: TestObserver<AppBar.Output>

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        AppBarBuilder(object : AppBar.Dependency {
            override val userRepository: UserRepository = FakeUserRepository.of(MY_USER)
        }).build(
            buildContext = root(savedInstanceState),
            payload = AppBarBuilder.Params(MY_ID)
        ).also {
            outputTest = it.output.test()
        }

    private val appBarElement: AppBarElement = AppBarElement()

    private val targetContext: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun checkTitleDisplayed() {
        appBarElement.checkTitle(targetContext.getString(R.string.app_name))
    }

    @Test
    fun whenAvatarClicked_verifyOutputGenerated() {
        appBarElement.clickAvatar()

        outputTest.assertValue(AppBar.Output.UserClicked)
    }

    @Test
    fun whenSearchClicked_verifyOutputGenerated() {
        appBarElement.clickSearch()

        outputTest.assertValue(AppBar.Output.SearchClicked)
    }
}
