package com.badoo.ribs.android.activitystarter

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

interface ActivityStarterHost {

    val context: Context

    fun startActivity(intent: Intent, requestCode: Int?)

    class ActivityHost(private val activity: Activity) : ActivityStarterHost {
        override val context: Context
            get() = activity

        override fun startActivity(intent: Intent, requestCode: Int?) {
            if (requestCode != null) {
                activity.startActivityForResult(intent, requestCode)
            } else {
                activity.startActivity(intent)
            }
        }
    }

    class FragmentHost(private val fragment: Fragment) : ActivityStarterHost {
        override val context: Context
            get() = fragment.requireContext()

        override fun startActivity(intent: Intent, requestCode: Int?) {
            if (requestCode != null) {
                fragment.startActivityForResult(intent, requestCode)
            } else {
                fragment.startActivity(intent)
            }
        }
    }

}