package com.badoo.ribs.example.root.routing

import com.badoo.ribs.example.logged_in_container.builder.LoggedInContainerBuilder
import com.badoo.ribs.example.logged_out_container.builder.LoggedOutContainerBuilder

internal class RootChildBuilders(
    val loggedInContainerBuilder: LoggedInContainerBuilder,
    val loggedOutContainerBuilder: LoggedOutContainerBuilder
)
