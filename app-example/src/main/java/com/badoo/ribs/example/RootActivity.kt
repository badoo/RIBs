package com.badoo.ribs.example

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.example.auth.AuthStateStorage
import com.badoo.ribs.example.auth.AuthStateStorageImpl
import com.badoo.ribs.example.auth.PreferencesAuthStatePersistence
import com.badoo.ribs.example.login.AuthCodeDataSource
import com.badoo.ribs.example.network.ApiFactory
import com.badoo.ribs.example.network.UnsplashApi
import com.badoo.ribs.example.root.Root
import com.badoo.ribs.example.root.RootBuilder
import com.badoo.ribs.portal.Portal
import com.badoo.ribs.portal.PortalBuilder
import com.badoo.ribs.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.routing.action.RoutingAction
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable

class RootActivity : RibActivity(), AuthCodeDataSource {

    private val authCodeRelay: Relay<String> = PublishRelay.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.example_activity_root)
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
    ): Root {
        val stateStorage = AuthStateStorageImpl(
            persistence = PreferencesAuthStatePersistence(
                PreferenceManager.getDefaultSharedPreferences(
                    this@RootActivity
                )
            )
        )

        return RootBuilder(
            object : Root.Dependency {
                override val api: UnsplashApi =
                    ApiFactory.api(BuildConfig.DEBUG, BuildConfig.ACCESS_KEY, stateStorage)

                override val authStateStorage: AuthStateStorage = stateStorage

                override val authCodeDataSource: AuthCodeDataSource = this@RootActivity

                override fun portal(): Portal.OtherSide = portal

                override fun activityStarter(): ActivityStarter = activityStarter
            }
        ).build(buildContext)
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.data?.getQueryParameter("code")?.let {
            authCodeRelay.accept(it)
        }
    }

    override fun getAuthCodeUpdates(): Observable<String> = authCodeRelay
}
