package com.badoo.ribs.sandbox.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.badoo.ribs.sandbox.R

class OtherActivity : AppCompatActivity() {

    companion object {
        const val KEY_INCOMING = "incoming"
        const val KEY_RETURNED_DATA = "foo"
        private const val RETURNED_DATA = 999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)
        setSupportActionBar(findViewById(R.id.toolbar))


        findViewById<TextView>(R.id.incoming_data).text = intent.extras?.getString(KEY_INCOMING)

        findViewById<View>(R.id.fab).setOnClickListener {
            setResult(
                Activity.RESULT_OK,
                Intent().apply { putExtra(KEY_RETURNED_DATA, RETURNED_DATA) })
            finish()
        }
    }
}
