package com.voidx.userlisting.feature.list.presentation

import com.voidx.userlisting.UserFixture.createUser
import com.voidx.userlisting.data.model.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ListUserStateMapperTest {

    private lateinit var mapper: ListUserStateMapper

    @Before
    fun setup() {
        mapper = ListUserStateMapper.Impl()
    }

    @Test
    fun `should map to ShowEmptyState when there is no more items`() {
        val user = createUser()

        val result = mapper.mapDelete(-1, user, true)

        assertTrue(result is ListUserState.UserDeleted)
        assertTrue((result as ListUserState.UserDeleted).hasNoMoreItems)
    }

    @Test
    fun `should map to UserDelete when just an user is passed for mapping`() {
        val user = createUser()

        val result = mapper.mapDelete(-1, user, false)

        assertTrue(result is ListUserState.UserDeleted)
        assertFalse((result as ListUserState.UserDeleted).hasNoMoreItems)
    }
}