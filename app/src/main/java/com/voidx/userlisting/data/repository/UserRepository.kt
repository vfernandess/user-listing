package com.voidx.userlisting.data.repository

import com.voidx.userlisting.data.model.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface UserRepository {

    fun listAllUsers(): Single<List<User>>

    fun saveUser(user: User): Completable

    fun removeUser(user: User): Single<Boolean>
}