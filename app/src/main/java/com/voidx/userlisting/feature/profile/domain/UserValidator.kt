package com.voidx.userlisting.feature.profile.domain

import com.voidx.userlisting.feature.profile.domain.exception.InvalidAgeException
import com.voidx.userlisting.feature.profile.domain.exception.InvalidNameException

interface UserValidator {

    fun validate(
        name: String?,
        age: String?
    ): Throwable?

    class Impl : UserValidator {

        override fun validate(
            name: String?,
            age: String?
        ): Throwable? {
            return when {
                name.isNullOrBlank() -> InvalidNameException
                age.isNullOrBlank() -> InvalidAgeException
                else -> null
            }
        }
    }
}