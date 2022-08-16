package com.badoo.ribs.android.activitystarter

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

interface ActivityStarterHost {

    val context: Context

    fun startActivity(intent: Intent, createOptions: OptionsCreator? = null)

    fun startActivityForResult(intent: Intent, requestCode: Int, createOptions: OptionsCreator? = null)

    class ActivityHost(private val activity: Activity) : ActivityStarterHost {
        override val context: Context
            get() = activity

        override fun startActivity(intent: Intent, createOptions: OptionsCreator?) {
            activity.startActivity(intent, createOptions?.invoke(activity))
        }

        override fun startActivityForResult(
            intent: Intent,
            requestCode: Int,
            createOptions: OptionsCreator?
        ) {
            activity.startActivityForResult(intent, requestCode, createOptions?.invoke(activity))
        }
    }

    class FragmentHost(private val fragment: Fragment) : ActivityStarterHost {
        override val context: Context
            get() = fragment.requireContext()

        override fun startActivity(intent: Intent, createOptions: OptionsCreator?) {
            fragment.startActivity(intent, createOptions?.invoke(fragment.requireActivity()))
        }

        override fun startActivityForResult(
            intent: Intent,
            requestCode: Int,
            createOptions: OptionsCreator?
        ) {
            fragment.startActivityForResult(intent, requestCode, createOptions?.invoke(fragment.requireActivity()))
        }
    }

}
