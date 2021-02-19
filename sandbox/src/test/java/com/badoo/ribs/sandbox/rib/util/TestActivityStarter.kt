package com.badoo.ribs.sandbox.rib.util

import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.Intent
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.requestcode.RequestCodeClient
import com.badoo.ribs.minimal.reactive.Relay
import com.badoo.ribs.minimal.reactive.Source
import com.badoo.ribs.minimal.reactive.filter
import com.badoo.ribs.minimal.reactive.map
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.assertj.core.api.ListAssert

class TestActivityStarter : ActivityStarter {

    private val intents: MutableList<Intent> = mutableListOf()
    private val responses: MutableList<Pair<Condition<Intent>, ActivityResult>> = mutableListOf()

    private val eventsRelay: Relay<Pair<RequestCodeClient, ActivityStarter.ActivityResultEvent>> = Relay()

    override fun startActivity(createIntent: Context.() -> Intent) {
        intents += testContext.createIntent()
    }

    override fun startActivityForResult(client: RequestCodeClient, requestCode: Int, createIntent: Context.() -> Intent) {
        val intent = testContext.createIntent()

        intents += intent

        val result = responses.lastOrNull { (matcher, _) ->
            matcher.matches(intent)
        }?.second ?: throw IllegalStateException("No response found for intent $intent")

        eventsRelay.emit(client to ActivityStarter.ActivityResultEvent(requestCode, result.resultCode, result.resultData))
    }

    override fun events(client: RequestCodeClient): Source<ActivityStarter.ActivityResultEvent> =
        eventsRelay
            .filter { (identifiable, _) ->
                identifiable.requestCodeClientId == client.requestCodeClientId
            }
            .map { it.second }

    fun stubResponse(matcher: Condition<Intent>, result: ActivityResult) {
        responses.add(matcher to result)
    }

    fun assertIntents(assertions: ListAssert<Intent>.() -> Unit) {
        assertThat(intents).assertions()
    }

    companion object {
        private val testContext: Context = mock {
            on { packageName } doReturn "com.test"
        }
    }
}
