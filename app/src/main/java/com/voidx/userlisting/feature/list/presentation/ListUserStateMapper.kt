package com.voidx.userlisting.feature.list.presentation

import com.voidx.userlisting.data.model.User

interface ListUserStateMapper {

    fun mapDelete(position: Int, user: User, hasNoMoreItems: Boolean): ListUserState

    fun mapInsertion(position: Int, user: User): ListUserState

    fun mapModified(user: User): ListUserState

    class Impl : ListUserStateMapper {
        override fun mapDelete(position: Int, user: User, hasNoMoreItems: Boolean): ListUserState {
            return ListUserState.UserDeleted(position, user, hasNoMoreItems)
        }

        override fun mapInsertion(position: Int, user: User): ListUserState {
            return ListUserState.UserInserted(position, user)
        }

        override fun mapModified(user: User): ListUserState {
            return ListUserState.UserModified(user)
        }
    }
}