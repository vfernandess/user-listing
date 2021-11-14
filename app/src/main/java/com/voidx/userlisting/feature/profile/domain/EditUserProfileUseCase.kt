package com.voidx.userlisting.feature.profile.domain

import com.voidx.userlisting.data.model.User
import com.voidx.userlisting.data.repository.UserRepository
import io.reactivex.rxjava3.core.Single

interface EditUserProfileUseCase {

    operator fun invoke(user: User): Single<User>

    class Impl(
        private val repository: UserRepository,
        private val validator: UserValidator
    ): EditUserProfileUseCase {

        override fun invoke(user: User): Single<User> {
            val validationResult = validator.validate(user.name, user.age)
            if (validationResult != null) {
                return Single.error(validationResult)
            }

            return repository.saveUser(user).toSingle { user }
        }
    }
}