package com.badoo.ribs.example.repository

import com.badoo.ribs.example.network.model.User

interface UserRepository {
    fun getUserById(userId: String): User
}
