package com.badoo.ribs.example.rib.util

import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.Intent
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.core.Identifiable
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.assertj.core.api.ListAssert

class TestActivityStarter : ActivityStarter {

    private val intents: MutableList<Intent> = mutableListOf()
    private val responses: MutableList<Pair<Condition<Intent>, ActivityResult>> = mutableListOf()

    private val eventsRelay: Relay<Pair<Identifiable, ActivityStarter.ActivityResultEvent>> = PublishRelay.create()

    override fun startActivity(createIntent: Context.() -> Intent) {
        intents += testContext.createIntent()
    }

    override fun startActivityForResult(client: Identifiable, requestCode: Int, createIntent: Context.() -> Intent) {
        val intent = testContext.createIntent()

        intents += intent

        val result = responses.lastOrNull { (matcher, _) ->
            matcher.matches(intent)
        }?.second ?: throw IllegalStateException("No response found for intent $intent")

        eventsRelay.accept(client to ActivityStarter.ActivityResultEvent(requestCode, result.resultCode, result.resultData))
    }

    override fun events(client: Identifiable): Observable<ActivityStarter.ActivityResultEvent> =
        eventsRelay
            .filter { (identifiable, _) ->
                identifiable.id == client.id
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
