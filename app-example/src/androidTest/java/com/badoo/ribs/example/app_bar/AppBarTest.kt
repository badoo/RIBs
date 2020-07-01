package com.badoo.ribs.example.app_bar

import android.content.Context
import android.os.Bundle
import androidx.test.platform.app.InstrumentationRegistry
import com.badoo.common.ribs.RibsRule
import com.badoo.mvicore.extension.asConsumer
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.example.*
import com.badoo.ribs.example.element.AppBarElement
import com.badoo.ribs.example.image.ImageDownloader
import com.badoo.ribs.example.repository.UserRepository
import io.reactivex.functions.Consumer
import io.reactivex.observers.TestObserver
import org.junit.Rule
import org.junit.Test

class AppBarTest {

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

    private val outputTest: TestObserver<AppBar.Output> = TestObserver()

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        AppBarBuilder(object : AppBar.Dependency {
            override val userRepository: UserRepository = FakeUserRepository.of(MY_USER)
            override val appBarOutput: Consumer<AppBar.Output> = outputTest.asConsumer()
            override val imageDownloader: ImageDownloader = FakeImageDownloader()
        }).build(
            buildContext = root(savedInstanceState),
            payload = AppBarBuilder.Params(MY_ID)
        )

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
