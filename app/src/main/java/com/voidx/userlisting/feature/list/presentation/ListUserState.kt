package com.voidx.userlisting.feature.list.presentation

import com.voidx.userlisting.data.model.User

sealed class ListUserState {

    data class UserDeleted(
        val position: Int,
        val user: User,
        val hasNoMoreItems: Boolean
    ) : ListUserState()

    data class UserInserted(
        val position: Int,
        val user: User
    ) : ListUserState()

    data class UserModified(
        val user: User
    ) : ListUserState()
}
