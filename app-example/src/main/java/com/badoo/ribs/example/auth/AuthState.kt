package com.badoo.ribs.example.auth

sealed class AuthState {
    object Unauthenticated : AuthState()
    object Anonymous : AuthState()
    data class Authenticated(val accessToken: String) : AuthState()
}
