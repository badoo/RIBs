package com.badoo.ribs.example.logged_out_container.routing

import com.badoo.ribs.example.logged_out_container.LoggedOutContainer
import com.badoo.ribs.example.login.Login
import com.badoo.ribs.example.login.LoginBuilder
import com.badoo.ribs.example.welcome.Welcome
import com.badoo.ribs.example.welcome.WelcomeBuilder

internal class LoggedOutContainerChildBuilders(
    val dependency: LoggedOutContainer.Dependency
) {
    private val subtreeDeps = SubtreeDeps(dependency)

    val welcomeBuilder: WelcomeBuilder = WelcomeBuilder(subtreeDeps)
    val loginBuilder: LoginBuilder = LoginBuilder(subtreeDeps)

    private class SubtreeDeps(
        dependency: LoggedOutContainer.Dependency
    ) : LoggedOutContainer.Dependency by dependency,
        Welcome.Dependency,
        Login.Dependency
}
