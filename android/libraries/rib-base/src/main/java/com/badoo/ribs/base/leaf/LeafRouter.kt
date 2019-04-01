package com.badoo.ribs.base.leaf

import android.os.Parcel
import android.os.Parcelable
import com.badoo.ribs.base.leaf.LeafRouter.Configuration
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.core.view.RibView

class LeafRouter<V: RibView>: Router<Configuration, V>(
    initialConfiguration = Configuration()
) {
    override fun resolveConfiguration(configuration: Configuration): RoutingAction<V> =
        noop()

    class Configuration : Parcelable {
        override fun writeToParcel(parcel: Parcel, flags: Int) { }
        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<Configuration> {
            override fun createFromParcel(parcel: Parcel): Configuration = Configuration()
            override fun newArray(size: Int): Array<Configuration?> = arrayOfNulls(size)
        }
    }
}
