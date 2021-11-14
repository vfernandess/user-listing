package com.voidx.userlisting.data.repository.local

import com.voidx.userlisting.data.model.User
import com.voidx.userlisting.data.repository.UserRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class UserLocalRepository(
    private val userCache: MutableMap<String, User> = LinkedHashMap(0, 0.75f, false)
) : UserRepository {

    override fun listAllUsers(): Single<List<User>> {
        return Single.create { emitter ->
            emitter.onSuccess(userCache.values.toList())
        }
    }

    override fun saveUser(user: User): Completable {
        return Completable.create { emitter ->
            userCache[user.id] = user

            emitter.onComplete()
        }
    }

    override fun removeUser(user: User): Single<Boolean> {
        return Single.create { emitter ->
            userCache.remove(user.id)

            emitter.onSuccess(userCache.isEmpty())
        }
    }
}