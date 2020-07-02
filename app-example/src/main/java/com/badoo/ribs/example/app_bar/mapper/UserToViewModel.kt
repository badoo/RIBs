package com.badoo.ribs.example.app_bar.mapper

import com.badoo.ribs.example.app_bar.AppBarView
import com.badoo.ribs.example.network.model.User

object UserToViewModel : (User) -> AppBarView.ViewModel {

    override fun invoke(user: User): AppBarView.ViewModel =
        AppBarView.ViewModel(
            profileImageUrl = user.profileImage.thumb ?: user.profileImage.small
        )

}
