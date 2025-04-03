package com.badoo.ribs.example.login

import io.reactivex.rxjava3.core.Observable

interface AuthCodeDataSource {
    fun getAuthCodeUpdates(): Observable<String>
}
