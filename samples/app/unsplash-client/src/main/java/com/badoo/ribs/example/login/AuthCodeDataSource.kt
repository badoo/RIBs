package com.badoo.ribs.example.login

import io.reactivex.Observable

interface AuthCodeDataSource {
    fun getAuthCodeUpdates(): Observable<String>
}
