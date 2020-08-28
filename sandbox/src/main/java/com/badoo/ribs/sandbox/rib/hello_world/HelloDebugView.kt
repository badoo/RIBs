package com.badoo.ribs.sandbox.rib.hello_world

import android.view.View
import android.widget.Button
import android.widget.Toast
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.plugin.utils.debug.DebugControls
import com.badoo.ribs.sandbox.R
import io.reactivex.disposables.CompositeDisposable

class HelloDebugControls : DebugControls<HelloWorld>(
    label = "Hello world",
    viewFactory = { it.inflate(R.layout.debug_helloworld) }
) {
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onDebugViewCreated(debugView: View) {
        debugView.findViewById<Button>(R.id.workflow_hello).setOnClickListener {
            Toast.makeText(it.context, "Hello", Toast.LENGTH_SHORT).show()

            disposables.add(
                // Trigger workflow
                rib.somethingSomethingDarkSide().subscribe()
            )
        }
    }

    override fun onDebugViewDestroyed(debugView: View) {
        super.onDebugViewDestroyed(debugView)
        disposables.dispose()
    }
}
