package com.badoo.ribs.samples.buildtime.rib.profile

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.buildtime.R

interface ProfileView : RibView {

    interface Factory : ViewFactory<Nothing?, ProfileView>
}

class ProfileViewImpl private constructor(
    override val androidView: ViewGroup,
    profileId: Int
) : AndroidRibView(), ProfileView {

    private val profileLabel: TextView = androidView.findViewById(R.id.profile_label)

    init {
        profileLabel.text = context.getString(R.string.profile_label, profileId)
    }

    class Factory(
        private val profileId: Int,
        @LayoutRes private val layoutRes: Int = R.layout.rib_profile
    ) : ProfileView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> ProfileView = {
            ProfileViewImpl(
                androidView = it.inflate(layoutRes),
                profileId = profileId
            )
        }
    }
}
