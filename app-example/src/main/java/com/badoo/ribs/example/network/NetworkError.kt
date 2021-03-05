package com.badoo.ribs.example.network

sealed class NetworkError {
    object Unauthorized: NetworkError()
}
