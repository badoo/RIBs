//package com.badoo.ribs.example.rib.switcher.animator
//
//import android.view.ViewGroup
//import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Animator
//import com.transitionseverywhere.AutoTransition
//import com.transitionseverywhere.ChangeBounds
//import com.transitionseverywhere.ChangeTransform
//import com.transitionseverywhere.Fade
//import com.transitionseverywhere.Transition
//import com.transitionseverywhere.TransitionSet
//
//object FadeInFadeOut : Animator {
//
////    override fun onActivatedFirst(viewGroup: ViewGroup, parent: ViewGroup): Transition? =
////        TransitionSet().apply {
////            addTransition(ChangeTransform())
////            addTransition(ChangeBounds())
////        }
////        Fade(Fade.IN).apply {
////            duration = 500
////            addTarget(viewGroup)
////        }
//
//    override fun onRemoved(viewGroup: ViewGroup, parent: ViewGroup): Transition? =
//        TransitionSet().apply {
//            addTransition(ChangeTransform())
//            addTransition(TransitionSet().apply {
//                addTransition(Fade(Fade.OUT))
//                addTransition(Fade(Fade.OUT))
//            })
////            addTransition(ChangeBounds())
//        }
////        Fade(Fade.OUT).apply {
////            duration = 500
////            addTarget(viewGroup)
////        }
//}
