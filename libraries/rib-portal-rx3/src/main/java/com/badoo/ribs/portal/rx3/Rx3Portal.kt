package com.badoo.ribs.portal.rx3

import com.badoo.ribs.annotation.ExperimentalApi
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.portal.Portal
import io.reactivex.rxjava3.core.Single

@ExperimentalApi
interface Rx3Portal : Portal {

    fun showDefault(): Single<Rib>
    fun showInPortal(ancestryInfo: AncestryInfo): Single<Rib>
}
