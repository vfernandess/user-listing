package com.voidx.userlisting.feature.profile.presentation

import com.voidx.userlisting.data.model.User
import com.voidx.userlisting.feature.profile.domain.exception.InvalidAgeException
import com.voidx.userlisting.feature.profile.domain.exception.InvalidNameException

interface UserProfileStateMapper {

    operator fun invoke(throwable: Throwable): UserProfileState

    operator fun invoke(user: User, isEditing: Boolean): UserProfileState

    class Impl : UserProfileStateMapper {
        override fun invoke(throwable: Throwable): UserProfileState {
            return when (throwable) {
                is InvalidNameException -> UserProfileState.InvalidNameError
                is InvalidAgeException -> UserProfileState.InvalidAgeError
                else -> UserProfileState.UnknownError
            }
        }

        override fun invoke(user: User, isEditing: Boolean): UserProfileState {
            return if (isEditing) {
                UserProfileState.Edited(user)
            } else {
                UserProfileState.Saved(user)
            }
        }
    }
}