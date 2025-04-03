package com.badoo.ribs.example.network

sealed class NetworkError {
    data object Unauthorized : NetworkError()
}
