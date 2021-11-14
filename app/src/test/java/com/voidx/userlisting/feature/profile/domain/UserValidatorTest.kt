package com.voidx.userlisting.feature.profile.domain

import com.voidx.userlisting.feature.profile.domain.exception.InvalidAgeException
import com.voidx.userlisting.feature.profile.domain.exception.InvalidNameException
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserValidatorTest {

    private lateinit var validator: UserValidator

    @Before
    fun setup() {
        validator = UserValidator.Impl()
    }

    @Test
    fun `should throw exception for invalid name`() {
        val result = validator.validate(name = null, age = "1")

        assertEquals(InvalidNameException, result)
    }

    @Test
    fun `should throw exception for invalid age`() {
        val result = validator.validate(name = "John Doe", age = null)

        assertEquals(InvalidAgeException, result)
    }
}