package com.badoo.ribs.example.app_bar

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.example.image.ImageDownloader
import com.badoo.ribs.example.repository.UserRepository
import io.reactivex.functions.Consumer

interface AppBar : Rib {

    interface Dependency {
        val userRepository: UserRepository
        val appBarOutput: Consumer<Output>
        val imageDownloader: ImageDownloader
    }

    sealed class Output {
        object SearchClicked : Output()
        object UserClicked : Output()
    }

    class Customisation(
        val viewFactory: AppBarView.Factory = AppBarViewImpl.Factory()
    ) : RibCustomisation
}
