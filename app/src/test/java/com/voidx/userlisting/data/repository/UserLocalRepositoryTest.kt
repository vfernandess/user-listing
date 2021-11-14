package com.voidx.userlisting.data.repository

import com.voidx.userlisting.UserFixture.createUser
import com.voidx.userlisting.data.model.User
import com.voidx.userlisting.data.repository.local.UserLocalRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.UUID
import kotlin.collections.LinkedHashMap

class UserLocalRepositoryTest {

    private lateinit var repository: UserRepository

    private  lateinit var usersCache: MutableMap<String, User>

    @Before
    fun setup() {
        usersCache = LinkedHashMap()
        repository = UserLocalRepository(usersCache)
    }

    @Test
    fun `should insert new users`() {
        val user = createUser()

        repository
            .saveUser(user)
            .test()
            .assertComplete()

        assertEquals(1, usersCache.size)
    }

    @Test
    fun `should remove user properly`() {
        val user = createUser()

        repository
            .saveUser(user)
            .test()
            .assertComplete()

        repository
            .removeUser(user)
            .test()
            .assertComplete()

        assertEquals(0, usersCache.size)
    }

    @Test
    fun `should list all users`() {

        val user1 = createUser()
        usersCache[user1.id] = user1

        val user2 = createUser()
        usersCache[user2.id] = user2

        repository
            .listAllUsers()
            .test()
            .assertNoErrors()
            .assertValue {
                it.size == 2
            }
    }

    @Test
    fun `should edit user properly`() {
        val user = createUser()

        repository
            .saveUser(user)
            .test()
            .assertComplete()

        user.name = "Joane Doe"

        repository
            .saveUser(user)
            .test()
            .assertComplete()

        assertEquals("Joane Doe", usersCache[user.id]?.name)
    }
}