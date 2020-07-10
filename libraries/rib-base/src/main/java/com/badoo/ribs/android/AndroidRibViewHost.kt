package com.badoo.ribs.android

import android.view.ViewGroup
import com.badoo.ribs.core.view.AndroidRibView

class AndroidRibViewHost(
    host: ViewGroup
) : AndroidRibView() {

    override val androidView: ViewGroup =
        host
}
