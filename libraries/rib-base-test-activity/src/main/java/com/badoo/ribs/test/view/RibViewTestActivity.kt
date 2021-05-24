package com.badoo.ribs.test.view

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity

class RibViewTestActivity : AppCompatActivity() {

    val rootView: ViewGroup
        get() = findViewById(android.R.id.content)

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME?.also { setTheme(it) }
        super.onCreate(savedInstanceState)
    }

    companion object {
        @StyleRes
        var THEME: Int? = null
    }

}