package com.voidx.userlisting.feature.profile.domain

import com.voidx.userlisting.data.model.User
import com.voidx.userlisting.data.repository.UserRepository
import io.reactivex.rxjava3.core.Single
import java.util.*

interface CreateUserUseCase {

    operator fun invoke(
        name: String?,
        age: String?,
        avatar: String?
    ): Single<User>

    class Impl(
        private val repository: UserRepository,
        private val validator: UserValidator
    ): CreateUserUseCase {

        override fun invoke(name: String?, age: String?, avatar: String?): Single<User> {
            val validationResult = validator.validate(name, age)
            if (validationResult != null) {
                return Single.error(validationResult)
            }

            val user = User(
                id = UUID.randomUUID().toString(),
                name = name,
                age = age,
                avatar = avatar
            )

            return repository
                .saveUser(user)
                .toSingle { user }
        }
    }
}