package com.voidx.userlisting.feature.list.domain

import com.voidx.userlisting.data.model.User
import com.voidx.userlisting.data.repository.UserRepository
import io.reactivex.rxjava3.core.Single

interface ListAllUsersUseCase {

    operator fun invoke(): Single<List<User>>

    class Impl(private val repository: UserRepository): ListAllUsersUseCase {

        override fun invoke(): Single<List<User>> {
            return repository.listAllUsers()
        }
    }
}