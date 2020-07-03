package com.badoo.ribs.example.login

import android.view.ViewGroup
import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.example.login.Login.Input
import com.badoo.ribs.example.login.Login.Output

class LoginNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((ViewGroup) -> LoginView?)?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<LoginView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Login, Connectable<Input, Output> by connector
