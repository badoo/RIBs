package com.badoo.ribs.example.root.routing

import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.logged_in_container.LoggedInContainer
import com.badoo.ribs.example.logged_in_container.LoggedInContainerBuilder
import com.badoo.ribs.example.logged_out_container.LoggedOutContainer
import com.badoo.ribs.example.logged_out_container.LoggedOutContainerBuilder
import com.badoo.ribs.example.login.Login
import com.badoo.ribs.example.login.LoginBuilder
import com.badoo.ribs.example.root.Root
import com.badoo.ribs.portal.Portal
import com.badoo.ribs.portal.PortalBuilder
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution

internal class RootChildBuilders(
    dependency: Root.Dependency,
    authDataSource: AuthDataSource
) {
    private val subtreeDeps = SubtreeDependency(dependency, authDataSource)

    val loggedOutContainerBuilder: LoggedOutContainerBuilder =
        LoggedOutContainerBuilder(
            subtreeDeps
        )

    val loggedInContainerBuilder: PortalBuilder =
        PortalBuilder(
            object : Portal.Dependency {
                override val defaultResolution: (Portal.OtherSide) -> Resolution =
                    { portal ->
                        child { loggedInContainerBuilder(authDataSource, portal).build(it) }
                    }
            }
        )
    val loginBuilder: LoginBuilder = LoginBuilder(subtreeDeps)

    private fun loggedInContainerBuilder(
        authDataSource: AuthDataSource,
        portal: Portal.OtherSide
    ): LoggedInContainerBuilder {
        return LoggedInContainerBuilder(
            dependency = object : Root.Dependency by subtreeDeps,
                LoggedInContainer.Dependency {
                override val authDataSource: AuthDataSource = authDataSource
                override val portal: Portal.OtherSide = portal
            }
        )
    }

    private class SubtreeDependency(
        dependency: Root.Dependency,
        override val authDataSource: AuthDataSource
    ) : Root.Dependency by dependency,
        LoggedOutContainer.Dependency,
        Login.Dependency
}
