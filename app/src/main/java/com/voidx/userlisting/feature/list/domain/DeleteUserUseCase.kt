package com.voidx.userlisting.feature.list.domain

import com.voidx.userlisting.data.model.User
import com.voidx.userlisting.data.repository.UserRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface DeleteUserUseCase {

    operator fun invoke(user: User): Single<Boolean>

    class Impl(private val repository: UserRepository): DeleteUserUseCase {

        override fun invoke(user: User): Single<Boolean> {
            return repository.removeUser(user)
        }
    }
}