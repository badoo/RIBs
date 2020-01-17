//package com.badoo.ribs.example.rib.switcher.animator
//
//import android.view.Gravity
//import android.view.ViewGroup
//import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Animator
//import com.transitionseverywhere.AutoTransition
//import com.transitionseverywhere.ChangeBounds
//import com.transitionseverywhere.ChangeTransform
//import com.transitionseverywhere.Fade
//import com.transitionseverywhere.Slide
//import com.transitionseverywhere.Transition
//import com.transitionseverywhere.TransitionSet
//
//object SlideInSlideOut : Animator {
//
////    override fun onActivatedFirst(viewGroup: ViewGroup, parent: ViewGroup): Transition? =
////        Slide(Gravity.LEFT).apply {
////            duration = 500
////            addTarget(viewGroup)
////        }
//
//    override fun onRemoved(viewGroup: ViewGroup, parent: ViewGroup): Transition? =
//        AutoTransition().apply {
////            addTransition(AutoTransition())
//            addTransition(ChangeTransform())
////            addTransition(ChangeBounds())
//        }
////        Slide(Gravity.RIGHT).apply {
////            duration = 500
////            addTarget(viewGroup)
////        }
//}
