package com.badoo.ribs.example.auth

sealed class AuthState {
    data object Unauthenticated : AuthState()
    data object Anonymous : AuthState()
    data class Authenticated(val accessToken: String) : AuthState()
}
