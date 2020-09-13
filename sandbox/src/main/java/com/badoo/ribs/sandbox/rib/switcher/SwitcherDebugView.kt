package com.badoo.ribs.sandbox.rib.switcher

import android.view.View
import android.widget.Button
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.plugin.utils.debug.DebugControls
import com.badoo.ribs.sandbox.R
import io.reactivex.disposables.CompositeDisposable

class SwitcherDebugControls : DebugControls<Switcher>(
    label = "Switcher",
    viewFactory = { it.inflate(R.layout.debug_switcher) }
) {
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onDebugViewCreated(debugView: View) {
        debugView.findViewById<Button>(R.id.workflow_hello).setOnClickListener {
            disposables.add(
                // Trigger workflow
                rib.attachHelloWorld().subscribe()
            )
        }

        debugView.findViewById<Button>(R.id.workflow_foo).setOnClickListener {
            disposables.add(
                // Trigger workflow
                rib.attachFooBar().subscribe()
            )
        }
    }

    override fun onDebugViewDestroyed(debugView: View) {
        super.onDebugViewDestroyed(debugView)
        disposables.dispose()
    }
}
