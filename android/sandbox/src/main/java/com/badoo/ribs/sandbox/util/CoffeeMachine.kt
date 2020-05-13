package com.badoo.ribs.sandbox.util

import android.content.Context
import android.widget.Toast

interface CoffeeMachine {
    fun makeCoffee(context: Context)
}

class StupidCoffeeMachine : CoffeeMachine {
    override fun makeCoffee(context: Context) {
        Toast.makeText(context, "Coffee machine brewing coffee...", Toast.LENGTH_SHORT).show()
    }
}
