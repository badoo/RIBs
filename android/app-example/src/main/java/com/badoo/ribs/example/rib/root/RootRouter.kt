package com.badoo.ribs.example.rib.root

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Portal
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.example.rib.root.RootRouter.Configuration
import com.badoo.ribs.example.rib.root.RootRouter.Configuration.Content
//import com.badoo.ribs.example.rib.root.RootRouter.ShowInPortalRoutingAction.Companion.showInPortal
import com.badoo.ribs.example.rib.switcher.builder.SwitcherBuilder
import kotlinx.android.parcel.Parcelize

class RootRouter(
    savedInstanceState: Bundle?,
    private val switcherBuilder: SwitcherBuilder
): Router<Configuration, Nothing, Content, Nothing, Nothing>(
    savedInstanceState = savedInstanceState,
    initialConfiguration = Content.Default,
    permanentParts = emptyList()
), Portal.Sink {

    interface ResolverChain {
        val next: ResolverChain?
    }


    sealed class Configuration() : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
//            @Parcelize data class Portal(val resolverChain: ResolverChain) : Content()
        }

//        constructor(parcel: Parcel) : this() {}
//        override fun writeToParcel(parcel: Parcel, flags: Int) {}
//        override fun describeContents(): Int = 0
//        @SuppressLint("ParcelCreator")
//        companion object CREATOR : Parcelable.Creator<Configuration> {
//            override fun createFromParcel(parcel: Parcel): Configuration = Content.Default
//            override fun newArray(size: Int): Array<Configuration?> = arrayOfNulls(size)
//        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        newRoot(Content.Default)
        super.onSaveInstanceState(outState)
    }

//    override fun accept(command: Portal.Sink.Command) {
//        when (command) {
//            is Portal.Sink.Command.Add -> push(Content.Portal(command.node))
//            is Portal.Sink.Command.Remove -> popBackStack()
//        }
//    }

    override val remoteRoutingAction: ((Bundle?) -> Node<*>) -> RoutingAction<out RibView> = {
        ShowInPortalRoutingAction(it)
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<Nothing> =
        when (configuration) {
            is Content.Default -> attach { switcherBuilder.build(it) }
//            is Content.Portal -> RoutingAction.noop()
//                showInPortal(node, configuration.node)
        }

    private inner class ShowInPortalRoutingAction<V : RibView>(
        private val childBuilder: (Bundle?) -> Node<*>
    ) : RoutingAction<V> {

        override fun parentNode(): Node<*>? {
            return node
        }

        override fun buildNodes(bundles: List<Bundle?>): List<Node.Descriptor> =
            listOf(
                Node.Descriptor(
                    node = childBuilder.invoke(bundles.firstOrNull()),
                    viewAttachMode = Node.AttachMode.PARENT // this works because this node will be the parent
                )
            )

        override fun onAttach(nodes: List<Node.Descriptor>) {
            node.addManaged(nodes.first().node)
        }

        override fun onDetach(nodes: List<Node.Descriptor>) {
            node.removeManaged(nodes.first().node)
        }
//
//        override fun execute(nodes: List<Node.Descriptor>) {
////            push(Content.Portal(nodes.first().node))
//            node.addManaged(nodes.first().node)
////            node.attachChildView(nodes.first().node)
//        }
//
//        override fun cleanup(nodes: List<Node.Descriptor>) {
////            node.detachChildView(nodes.first().node)
//            node.removeManaged(nodes.first().node)
////            popBackStack()
//        }

//        companion object {
//            fun <V : RibView> showInPortal(
//                parentNode: Node<*>,
//                childBuilder: (Bundle?) -> Node<*>
//            ): RoutingAction<V> =
//                ShowInPortalRoutingAction(parentNode, childBuilder)
//        }
    }
}
