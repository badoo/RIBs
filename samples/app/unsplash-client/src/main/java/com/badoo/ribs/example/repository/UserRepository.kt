package com.badoo.ribs.example.repository

import com.badoo.ribs.example.network.model.User
import io.reactivex.rxjava3.core.Maybe

interface UserRepository {
    fun getUserById(userId: String): Maybe<User>
}
