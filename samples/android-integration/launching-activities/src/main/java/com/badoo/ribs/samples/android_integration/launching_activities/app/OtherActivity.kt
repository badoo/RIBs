package com.badoo.ribs.samples.android_integration.launching_activities.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.badoo.ribs.samples.android_integration.launching_activities.R

class OtherActivity : AppCompatActivity() {
    companion object {
        const val KEY_INCOMING: String = "incoming"
        const val KEY_OUTGOING: String = "outgoing"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.other_activity)
        findViewById<TextView>(R.id.dataReceived).text = intent.getStringExtra(KEY_INCOMING)
        val editText = findViewById<EditText>(R.id.dataToReturn)
        findViewById<Button>(R.id.returnDataButton).setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().putExtra(KEY_OUTGOING, editText.text.toString()))
            finish()
        }
    }
}
