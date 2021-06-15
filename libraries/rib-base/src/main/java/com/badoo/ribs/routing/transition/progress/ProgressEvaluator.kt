package com.badoo.ribs.routing.transition.progress

import com.badoo.ribs.minimal.reactive.Source

interface ProgressEvaluator {
    /**
     * A value in the range of typically 0f to 1f.
     *
     * It cannot go below 0f, however, it can go temporarily over 1f e.g. when
     * using OvershootInterpolator. End result is expected to land at 1f.
     *
     * Don't rely on its value for determining whether we're running still, use [isPending] for that.
     *
     * This field is currently not used by the framework. It's kept here for potential support of
     * notifying child RibViews. It might be removed later if that functionality is dropped.
     */
    val progress: Float

    val isPending: Boolean

    val isPendingSource: Source<Boolean>

}
