package com.voidx.userlisting.feature.profile.presentation

import com.voidx.userlisting.data.model.User

sealed class UserProfileState {

    object InvalidNameError: UserProfileState()
    object InvalidAgeError: UserProfileState()
    object UnknownError: UserProfileState()

    data class Saved(
        val user: User
    ): UserProfileState()

    data class Edited(
        val user: User
    ): UserProfileState()

    data class UserLoaded(
        val user: User
    ): UserProfileState()
}
