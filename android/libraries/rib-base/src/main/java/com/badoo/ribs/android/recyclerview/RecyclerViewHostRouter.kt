//package com.badoo.ribs.android.recyclerview
//
//import android.os.Parcelable
//import com.badoo.ribs.android.recyclerview.RecyclerViewHostRouter.Configuration
//import com.badoo.ribs.android.recyclerview.RecyclerViewHostRouter.Configuration.Content
//import com.badoo.ribs.android.recyclerview.RecyclerViewHostRouter.Configuration.Content.Default
//import com.badoo.ribs.android.recyclerview.client.RecyclerViewRibResolver
//import com.badoo.ribs.core.Router
//import com.badoo.ribs.core.builder.BuildParams
//import com.badoo.ribs.core.routing.action.RoutingAction
//import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
//import com.badoo.ribs.core.view.RibView
//import kotlinx.android.parcel.Parcelize
//import java.util.UUID
//
//internal class RecyclerViewHostRouter<T : Parcelable>(
//    buildParams: BuildParams<Nothing?>,
//    private val feature: RecyclerViewHostFeature<T>,
//    private val ribResolver: RecyclerViewRibResolver<T>
//): Router<Configuration>(
//    // FIXME
//) {
//
//    sealed class Configuration : Parcelable {
//        sealed class Content : Configuration() {
//            @Parcelize object Default : Content()
//            @Parcelize data class Item(val uuid: UUID): Content()
//        }
//    }
//
//    override fun resolve(routing: RoutingElement<Configuration>): RoutingAction =
//        when (routing.configuration) {
//            is Default -> noop()
//            is Content.Item -> resolve(configuration.uuid)
//        }
//
//    fun resolve(uuid: UUID): RoutingAction {
//        val entry = feature.state.items.find { it.uuid == uuid }!!
//        val element = entry.element
//        return ribResolver.resolve(element)
//    }
//}
