package com.badoo.ribs.example

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.example.auth.AuthStateStorage
import com.badoo.ribs.example.auth.PreferencesAuthStateStorage
import com.badoo.ribs.example.network.ApiFactory
import com.badoo.ribs.example.network.NetworkError
import com.badoo.ribs.example.network.UnsplashApi
import com.badoo.ribs.example.root.Root
import com.badoo.ribs.example.root.RootBuilder
import com.badoo.ribs.portal.Portal
import com.badoo.ribs.portal.PortalBuilder
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class RootActivity : RibActivity() {

    private val networkErrorsRelay = PublishRelay.create<NetworkError>()
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.example_activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Rib =
        PortalBuilder(
            object : Portal.Dependency {
                override fun defaultResolution(): (Portal.OtherSide) -> Resolution =
                    { portal ->
                        child { buildRootNode(portal, it) }
                    }

            }
        ).build(root(savedInstanceState))


    private fun buildRootNode(
        portal: Portal.OtherSide,
        buildContext: BuildContext
    ): Root =
        RootBuilder(
            object : Root.Dependency {
                override val api: UnsplashApi = api()
                override val authStateStorage: AuthStateStorage =
                    PreferencesAuthStateStorage(PreferenceManager.getDefaultSharedPreferences(this@RootActivity))
                override val networkErrors: Observable<NetworkError> =
                    networkErrorsRelay
                        .observeOn(AndroidSchedulers.mainThread())

                override fun portal(): Portal.OtherSide = portal
            }
        ).build(buildContext)

    private fun api(): UnsplashApi =
        ApiFactory.api(
            isDebug = BuildConfig.DEBUG,
            accessKey = BuildConfig.ACCESS_KEY,
            networkErrorConsumer = networkErrorsRelay
        )


}
