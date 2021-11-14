package com.voidx.userlisting.feature.profile.presentation

import com.voidx.userlisting.UserFixture.createUser
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserProfileStateMapperTest {

    private lateinit var mapper: UserProfileStateMapper

    @Before
    fun setup() {
        mapper = UserProfileStateMapper.Impl()
    }

    @Test
    fun `should map state to edit when is editing`() {
        val result = mapper.invoke(createUser(), true)

        assertTrue(result is UserProfileState.Edited)
    }

    @Test
    fun `should map state to save when is not editing`() {
        val result = mapper.invoke(createUser(), false)

        assertTrue(result is UserProfileState.Saved)
    }
}