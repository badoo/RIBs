package com.badoo.ribs.example

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.annotation.ExperimentalApi
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.example.auth.AuthStateStorage
import com.badoo.ribs.example.auth.PreferencesAuthStateStorage
import com.badoo.ribs.example.network.ApiFactory
import com.badoo.ribs.example.network.UnsplashApi
import com.badoo.ribs.example.root.Root
import com.badoo.ribs.example.root.builder.RootBuilder
import com.badoo.ribs.portal.Portal
import com.badoo.ribs.portal.PortalBuilder
import com.badoo.ribs.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.routing.action.RoutingAction

@ExperimentalApi
class RootActivity : RibActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Rib =
        PortalBuilder(
            object : Portal.Dependency {
                override fun defaultRoutingAction(): (Portal.OtherSide) -> RoutingAction =
                    { portal ->
                        attach { buildRootNode(portal, it) }
                    }

            }
        ).build(root(savedInstanceState))


    private fun buildRootNode(
        portal: Portal.OtherSide,
        buildContext: BuildContext
    ): Root =
        RootBuilder(
            object : Root.Dependency {
                override val api: UnsplashApi =
                    ApiFactory.api(BuildConfig.DEBUG, BuildConfig.ACCESS_KEY)
                override val authStateStorage: AuthStateStorage =
                    PreferencesAuthStateStorage(PreferenceManager.getDefaultSharedPreferences(this@RootActivity))
            }
        ).build(buildContext)

}
