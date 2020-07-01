package com.badoo.ribs.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.badoo.ribs.example.network.ApiFactory

class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.example_activity_root)
        val api = ApiFactory.api(BuildConfig.DEBUG, BuildConfig.ACCESS_KEY)
    }
}
