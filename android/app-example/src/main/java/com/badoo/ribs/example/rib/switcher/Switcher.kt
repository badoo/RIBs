package com.badoo.ribs.example.rib.switcher

import com.badoo.ribs.android.CanProvideActivityStarter
import com.badoo.ribs.android.CanProvidePermissionRequester
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.directory.CanProvideRibCustomisation
import com.badoo.ribs.core.directory.RibCustomisation
import com.badoo.ribs.core.directory.inflateOnDemand
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.dialog.CanProvideDialogLauncher
import com.badoo.ribs.example.R

interface Switcher : Rib {

    interface Dependency :
        CanProvideActivityStarter,
        CanProvidePermissionRequester,
        CanProvideDialogLauncher,
        CanProvideRibCustomisation

    class Customisation(
        val viewFactory: ViewFactory<SwitcherView> = inflateOnDemand(
            R.layout.rib_switcher
        )
    ) : RibCustomisation
}
