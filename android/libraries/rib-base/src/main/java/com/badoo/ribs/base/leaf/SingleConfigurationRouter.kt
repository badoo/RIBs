package com.badoo.ribs.base.leaf

import android.os.Parcel
import android.os.Parcelable
import com.badoo.ribs.base.leaf.SingleConfigurationRouter.Configuration
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.core.view.RibView

object SingleConfigurationRouter: Router<Configuration, Nothing>(
    initialConfiguration = Configuration()
) {
    override fun resolveConfiguration(configuration: Configuration): RoutingAction<Nothing> =
        noop()

    class Configuration : Parcelable {
        override fun writeToParcel(parcel: Parcel, flags: Int) { }
        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<Configuration> {
            override fun createFromParcel(parcel: Parcel): Configuration = Configuration()
            override fun newArray(size: Int): Array<Configuration?> = arrayOfNulls(size)
        }
    }

    internal fun <V: RibView> typedInstance(): Router<Configuration, V> =
        SingleConfigurationRouter as Router<Configuration, V>
}
