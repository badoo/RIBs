package com.badoo.ribs.portal

import com.badoo.ribs.annotation.ExperimentalApi
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.AncestryInfo
import io.reactivex.Single

@ExperimentalApi
interface RxPortal : Portal {

    fun showDefault(): Single<Rib>
    fun showInPortal(ancestryInfo: AncestryInfo): Single<Rib>
}
