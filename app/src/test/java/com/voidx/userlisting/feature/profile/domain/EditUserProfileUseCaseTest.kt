package com.voidx.userlisting.feature.profile.domain

import com.voidx.userlisting.UserFixture.createUser
import com.voidx.userlisting.data.repository.local.UserLocalRepository
import org.junit.Before
import org.junit.Test

class EditUserProfileUseCaseTest {

    private lateinit var useCase: EditUserProfileUseCase

    @Before
    fun setup() {
        useCase = EditUserProfileUseCase.Impl(UserLocalRepository(), UserValidator.Impl())
    }

    @Test
    fun `should save user`() {
        val user = createUser()

        useCase
            .invoke(user)
            .test()
            .assertNoErrors()
            .assertValueCount(1)
    }
}