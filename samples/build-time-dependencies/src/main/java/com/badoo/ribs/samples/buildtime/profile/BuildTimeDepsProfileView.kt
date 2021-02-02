package com.badoo.ribs.samples.buildtime.profile

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.buildtime.R

interface BuildTimeDepsProfileView : RibView {

    interface Factory : ViewFactory<Nothing?, BuildTimeDepsProfileView>
}

class BuildTimeDepsProfileViewImpl private constructor(
    override val androidView: ViewGroup,
    profileId: Int
) : AndroidRibView(), BuildTimeDepsProfileView {

    private val profileLabel: TextView = androidView.findViewById(R.id.profile_label)

    init {
        profileLabel.text = "This is the profile RIB built for user: $profileId"
    }

    class Factory(
        private val profileId: Int,
        @LayoutRes private val layoutRes: Int = R.layout.rib_profile
    ) : BuildTimeDepsProfileView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> BuildTimeDepsProfileView = {
            BuildTimeDepsProfileViewImpl(
                androidView = it.inflate(layoutRes),
                profileId = profileId
            )
        }
    }
}
