package com.badoo.ribs.example

import com.badoo.ribs.example.network.model.User
import com.badoo.ribs.example.repository.UserRepository
import io.reactivex.Maybe

class FakeUserRepository(
        private val users: List<User>
) : UserRepository {

    override fun getUserById(userId: String): Maybe<User> =
        users
            .find { it.id == userId }
            ?.let {
                Maybe.just(it)
            }
            ?: Maybe.empty()

    companion object {
        fun of(vararg users: User): UserRepository = FakeUserRepository(users.asList())
    }
}
