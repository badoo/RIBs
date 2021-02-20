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
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.plugin.utils.debug.DebugControlsHost
import com.badoo.ribs.core.plugin.utils.debug.GrowthDirection
import com.badoo.ribs.debug.TreePrinter
import com.badoo.ribs.example.auth.AuthStateStorage
import com.badoo.ribs.example.auth.AuthStateStorageImpl
import com.badoo.ribs.example.auth.PreferencesAuthStatePersistence
import com.badoo.ribs.example.login.AuthCodeDataSource
import com.badoo.ribs.example.network.ApiFactory
import com.badoo.ribs.example.network.NetworkError
import com.badoo.ribs.example.network.UnsplashApi
import com.badoo.ribs.example.root.Root
import com.badoo.ribs.example.root.RootBuilder
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class RootActivity : RibActivity(), AuthCodeDataSource {

    private val authCodeRelay: Relay<String> = PublishRelay.create()
    private val networkErrorsRelay = PublishRelay.create<NetworkError>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.example_activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Rib =
        buildRootNode(root(
            savedInstanceState = savedInstanceState,
            defaultPlugins = { node ->
                if (BuildConfig.DEBUG) {
                    listOfNotNull(
                        if (node.isRoot) createDebugControlHost() else null
                    )
                } else emptyList()
            }
        ))


    private fun buildRootNode(
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
                override val api: UnsplashApi = api(stateStorage)
                override val authStateStorage: AuthStateStorage = stateStorage
                override val authCodeDataSource: AuthCodeDataSource = this@RootActivity
                override val activityStarter: ActivityStarter = integrationPoint.activityStarter
                override val networkErrors: Observable<NetworkError> =
                    networkErrorsRelay
                        .observeOn(AndroidSchedulers.mainThread())
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
    private fun api(authStateStorage: AuthStateStorage): UnsplashApi =
        ApiFactory.api(
            isDebug = BuildConfig.DEBUG,
            accessKey = BuildConfig.ACCESS_KEY,
            networkErrorConsumer = networkErrorsRelay,
            authStateStorage = authStateStorage
        )


    private fun createDebugControlHost(): Plugin =
        DebugControlsHost(
            viewGroupForChildren = { findViewById(R.id.debug_controls_host) },
            growthDirection = GrowthDirection.BOTTOM,
            defaultTreePrinterFormat = TreePrinter.FORMAT_SIMPLE
        )

}
