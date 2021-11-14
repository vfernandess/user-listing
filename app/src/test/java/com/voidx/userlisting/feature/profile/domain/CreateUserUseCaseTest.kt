package com.voidx.userlisting.feature.profile.domain

import com.voidx.userlisting.data.repository.local.UserLocalRepository
import org.junit.Before
import org.junit.Test

class CreateUserUseCaseTest {

    private lateinit var useCase: CreateUserUseCase

    @Before
    fun setup() {
        useCase = CreateUserUseCase.Impl(UserLocalRepository(), UserValidator.Impl())
    }

    @Test
    fun `should save user`() {
        useCase
            .invoke(name = "John doe", age = "1", avatar = null)
            .test()
            .assertNoErrors()
            .assertValueCount(1)
    }
}