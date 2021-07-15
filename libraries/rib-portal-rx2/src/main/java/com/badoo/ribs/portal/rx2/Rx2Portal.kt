package com.badoo.ribs.portal.rx2

import com.badoo.ribs.annotation.ExperimentalApi
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.portal.Portal
import io.reactivex.Single

@ExperimentalApi
interface Rx2Portal : Portal {

    fun showDefault(): Single<Rib>
    fun showInPortal(ancestryInfo: AncestryInfo): Single<Rib>
}
